package it.zS0bye.eLuckyBlock.executors;

import org.bukkit.entity.Player;

public abstract class Executors {

    protected void startAnimations(final String getAnimation) {}

    protected void startAnimations(final String getAnimation, final Player players) {}

    protected void startAnimations(final String getAnimation, final String color, final String style, final double progress, final int times) {}

    protected void startAnimations(final String getAnimation, final Player players, final String color, final String style, final double progress, final int times) {}

    protected void startAnimations(final String getAnimation, final String subtitle, final int fadein, final int stay, final int fadeout) {}

    protected void startAnimations(final String getAnimation, final Player players, final String subtitle, final int fadein, final int stay, final int fadeout) {}

    abstract String getType();

    abstract void apply();

}
