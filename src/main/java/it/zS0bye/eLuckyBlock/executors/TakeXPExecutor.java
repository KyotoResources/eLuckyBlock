package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.entity.Player;

public class TakeXPExecutor extends Executors {

    private final Player player;
    private String execute;

    public TakeXPExecutor(final String execute, final Player player) {
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected String getType() {
        return "[TAKE_XP] ";
    }

    @Override
    protected void apply() {

        final int level = Integer.parseInt(execute.split(";")[0]);
        final int xp = Integer.parseInt(execute.split(";")[1]);

        if (level != 0) player.setLevel(player.getLevel() - level);
        if (xp == 0) return;
        int save = player.getTotalExperience();
        player.setTotalExperience(0);
        player.setLevel(0);
        player.setExp(0);
        player.giveExp(save - xp);
    }

}
