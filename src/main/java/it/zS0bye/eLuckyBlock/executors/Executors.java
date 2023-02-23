package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class Executors {

    protected void startTask(final Player player, final String animation) {}

    protected void startTask(final Player player, final String animation, final String color, final String style, final double progress, final int times) {}

    protected void startTask(final Player player, final String animation, final String subtitle, final int fadein, final int stay, final int fadeout) {}

    protected void startTask(final String type, final List<String> colors, final int delay, final int times) {}

    protected abstract String getType();

    protected abstract void apply();

}
