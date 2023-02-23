package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.logging.Level;

public class SoundExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private Sound sound;

    public SoundExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[SOUND] ";
    }

    protected void apply() {

         try {
             this.sound = Sound.valueOf(execute
                     .replace(this.getType(), "")
                     .split(";")[0]);
         }catch (IllegalArgumentException e) {
             this.plugin.getLogger().log(Level.SEVERE, "The sound you entered in the configuration is not compatible with the server version, or it is not an existing sound! Reason: " + e.getMessage());
         }

        final int acute = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[1]);
        final int volume = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[2]);

        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> this.player.playSound(this.player.getLocation(), this.sound, volume, acute), 1L);
    }
}