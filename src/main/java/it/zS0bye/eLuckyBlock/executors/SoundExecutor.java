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