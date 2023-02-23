package it.zS0bye.eLuckyBlock.reflections;

import it.zS0bye.eLuckyBlock.utils.VersionUtils;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class ActionField {

    private final Player player;
    private final String msg;

    public ActionField(final Player player, final String msg) {
        this.player = player;
        this.msg = msg;
        this.send();
    }

    @SneakyThrows
    private void send() {

        if (!VersionUtils.checkVersion(1.8, 1.9, 1.10)) {
            this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.msg));
            return;
        }

        final Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
        final Class<?> chatComponent = getNMSClass("IChatBaseComponent");
        final Class<?> packetActionbar = getNMSClass("PacketPlayOutChat");

        Constructor<?> ConstructorActionbar;
        ConstructorActionbar = packetActionbar.getDeclaredConstructor(chatComponent, byte.class);

        final Object actionbar = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" + this.msg + "\"}");
        final Object packet = ConstructorActionbar.newInstance(actionbar, (byte) 2);
        this.sendPacket(packet);
    }

    @SneakyThrows
    private void sendPacket(final Object packet) {
        Object handle = this.player.getClass().getMethod("getHandle").invoke(this.player);
        Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
        playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
    }

    @SneakyThrows
    private Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

}
