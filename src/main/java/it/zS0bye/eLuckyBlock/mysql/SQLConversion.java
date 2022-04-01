package it.zS0bye.eLuckyBlock.mysql;

import org.bukkit.Location;

public class SQLConversion {

    public static String convertLoc(final Location loc) {
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return world + ", " + x + ", " + y + ", " + z;
    }


}
