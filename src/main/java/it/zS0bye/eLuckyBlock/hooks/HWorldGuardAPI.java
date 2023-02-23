package it.zS0bye.eLuckyBlock.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class HWorldGuardAPI {

    private final Location location;
    public static StateFlag luckyflag;

    public HWorldGuardAPI(final Location location) {
        this.location = location;
    }

    public void loadFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("eluckyblock", true);
            registry.register(flag);
            luckyflag = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("eluckyblock");
            if (existing instanceof StateFlag) {
                luckyflag = (StateFlag) existing;
            }

        }
    }

    public ApplicableRegionSet applicableRegions() {
        final Plugin worldguard = Bukkit.getPluginManager().getPlugin("WorldGuard");
        final String version = worldguard != null && worldguard.isEnabled() ? worldguard.getDescription().getVersion() : null;
        if(version == null || !version.startsWith("7")) return null;
        com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(location);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.getApplicableRegions(loc);
    }
}
