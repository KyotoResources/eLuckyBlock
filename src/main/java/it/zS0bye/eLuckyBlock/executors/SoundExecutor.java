package it.zS0bye.eLuckyBlock.executors;

import com.cryptomorin.xseries.XSound;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SoundExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final Player player;
    private String execute;

    public SoundExecutor(final ELuckyBlock plugin, final String execute, final Player player) {
        this.plugin = plugin;
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[SOUND] ";
    }

    protected void apply() {

        final String sound = this.execute.split(";")[0];
        final int acute = Integer.parseInt(this.execute.split(";")[1]);
        final int volume = Integer.parseInt(this.execute.split(";")[2]);

        if(sound.startsWith("@")) {
            Bukkit.getOnlinePlayers().forEach(players -> this.run(players, sound, volume, acute));
            return;
        }

        this.run(this.player, sound, volume, acute);
    }

    private void run(final Player player, final String sound, final int volume, final int acute) {
        final Optional<XSound> xsound = XSound.matchXSound(sound);
        if(!xsound.isPresent() || !xsound.get().isSupported()) return;
        final Sound playsound = xsound.get().parseSound();
        if(playsound == null) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () ->
                this.player.playSound(player.getLocation(), playsound, volume, acute), 1L);
    }
}