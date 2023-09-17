package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.entity.Player;

public class GiveXPExecutor extends Executors {

    private final Player player;
    private String execute;

    public GiveXPExecutor(final String execute, final Player player) {
        this.player = player;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    @Override
    protected String getType() {
        return "[GIVE_XP] ";
    }

    @Override
    protected void apply() {

        final int level = Integer.parseInt(execute.split(";")[0]);
        final int xp = Integer.parseInt(execute.split(";")[1]);

        if(level != 0) player.giveExpLevels(level);
        if(xp != 0) player.giveExp(xp);
    }

}
