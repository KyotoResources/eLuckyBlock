package it.zS0bye.eLuckyBlock.reflections;

import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import it.zS0bye.eLuckyBlock.utils.VersionChecker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class ActionField {

    private final Player player;
    private String msg;

    public ActionField(final Player player, final String msg) {
        this.player = player;
        this.msg = msg;
        send();
    }

    public void send() {

        if (!VersionChecker.getV1_8()
        && !VersionChecker.getV1_9()
        && !VersionChecker.getV1_10()) {
            String msg = ColorUtils.getColor(this.msg);
            this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
            return;
        }

        this.msg = ColorUtils.getColor(this.msg);

        Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
        Class<?> chatComponent = getNMSClass("IChatBaseComponent");
        Class<?> packetActionbar = getNMSClass("PacketPlayOutChat");

        try {

            Constructor<?> ConstructorActionbar = null;
            ConstructorActionbar = packetActionbar.getDeclaredConstructor(chatComponent, byte.class);

            Object actionbar = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" + this.msg + "\"}");
            Object packet = ConstructorActionbar.newInstance(actionbar, (byte) 2);
            sendPacket(player, packet);

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object packet) {

        try {

            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {

        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {

            return Class.forName("net.minecraft.server." + version + "." + name);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            return null;
        }
    }


}
