/*
 * The LuckyBlock Plugin you needed! - https://github.com/KyotoResources/eLuckyBlock
 * Copyright (c) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zS0bye.eLuckyBlock.reflections;

import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
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
            this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.msg));
            return;
        }

        Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
        Class<?> chatComponent = getNMSClass("IChatBaseComponent");
        Class<?> packetActionbar = getNMSClass("PacketPlayOutChat");

        try {

            Constructor<?> ConstructorActionbar;
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
