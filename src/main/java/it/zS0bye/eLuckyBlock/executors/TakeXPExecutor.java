package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.entity.Player;

public class TakeXPExecutor extends Executors {

    private final String execute;
    private final Player player;

    public TakeXPExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    @Override
    protected String getType() {
        return "[TAKE_XP] ";
    }

    @Override
    protected void apply() {

        final int level = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[0]);

        final int xp = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[1]);

        if(xp != 0) {
            int save = player.getTotalExperience();
            player.setTotalExperience(0);
            player.setLevel(0);
            player.setExp(0);
            player.giveExp(save - xp);
        }

        if(level != 0) player.setLevel(player.getLevel() - level);
    }

}
