package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.entity.Player;

public class GiveXPExecutor extends Executors {

    private final String execute;
    private final Player player;

    public GiveXPExecutor(final String execute, final Player player) {
        this.execute = execute;
        this.player = player;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    @Override
    protected String getType() {
        return "[GIVE_XP] ";
    }

    @Override
    protected void apply() {

        final int level = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[0]);

        final int xp = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[1]);

        if(level != 0) player.giveExpLevels(level);
        if(xp != 0) player.giveExp(xp);
    }

}
