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

package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.logging.Level;

public class SoundExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private Sound sound;

    public SoundExecutor(final String execute, final Player player) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        if (this.execute.startsWith(getType()))
            apply();
    }

    protected String getType() {
        return "[SOUND] ";
    }

    protected void apply() {

         try {
             sound = Sound.valueOf(execute
                     .replace(getType(), "")
                     .split(";")[0]);
         }catch (IllegalArgumentException e) {
             this.plugin.getLogger().log(Level.SEVERE, "The sound you entered in the configuration is not compatible with the server version, or it is not an existing sound! Reason: " + e.getMessage());
         }

        int acute = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[1]);
        int volume = Integer.parseInt(execute
                .replace(getType(), "")
                .split(";")[2]);

        new BukkitRunnable() {
            @Override
            public void run() {
                    player.playSound(player.getLocation(), sound, volume, acute);
            }
        }.runTaskLaterAsynchronously(ELuckyBlock.getInstance(), 1L);

    }
}