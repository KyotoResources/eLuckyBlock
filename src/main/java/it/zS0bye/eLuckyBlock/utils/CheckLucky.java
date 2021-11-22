package it.zS0bye.eLuckyBlock.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class CheckLucky {

    private final Block block;
    private final Material material;
    private final String world;
    private final org.bukkit.Location region;
    private final LuckyUtils utils;

    public CheckLucky(final Block block, final String world, final org.bukkit.Location region, final String luckyblock) {
        this.block = block;
        this.material = block.getType();
        this.world = world;
        this.region = region;
        this.utils = new LuckyUtils(luckyblock);
    }

    public boolean check() {

        if(VersionChecker.getV1_8()
                || VersionChecker.getV1_9()
                || VersionChecker.getV1_10()
                || VersionChecker.getV1_11()
                || VersionChecker.getV1_12()) {
            if(checkLegacyMaterial()) {
                return true;
            }
        }else {
            if(checkMaterial()) {
                return true;
            }
        }

        if(checkWorlds()) {
            return true;
        }


        if(ConfigUtils.HOOKS_WORLDGUARD.getBoolean()) {
            return checkRegion();
        }


        return false;
    }

    private boolean checkMaterial() {
        String material = utils.getString(utils.getMaterial());
        return this.material != ConvertUtils.getMaterial(material);
    }

    private boolean checkLegacyMaterial() {
        String material = utils.getString(utils.getMaterial());
        if (NumberUtils.isNumber(material)) {
            if (this.material.getId() == Integer.parseInt(material)) {
                if (utils.contains(utils.getData())) {
                    return this.block.getData() != utils.getInt(utils.getData());
                } else {
                    return false;
                }
            }
        } else {
            if (this.material == ConvertUtils.getMaterial(material)) {
                if (utils.contains(utils.getData())) {
                    return this.block.getData() != utils.getInt(utils.getData());
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkRegion() {
        final Plugin p = Bukkit.getPluginManager().getPlugin("WorldGuard");
        final String version = p != null && p.isEnabled() ? p.getDescription().getVersion() : null;
        if(version != null && version.startsWith("7")) {
            Location loc = BukkitAdapter.adapt(region);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(loc);
            for (ProtectedRegion regions : set) {
                if (utils.equals(utils.getBlocked_regions_type(), "blacklist")) {
                    return utils.getStringList(utils.getBlocked_regions()).contains(regions.getId());
                } else if (utils.equals(utils.getBlocked_regions_type(), "whitelist")) {
                    return !utils.getStringList(utils.getBlocked_regions()).contains(regions.getId());
                }
            }
        }
        return false;
    }

    private boolean checkWorlds() {
        if(utils.equals(utils.getBlocked_worlds_type(), "blacklist")) {
            return utils.getStringList(utils.getBlocked_worlds()).contains(world);
        }else if(utils.equals(utils.getBlocked_worlds_type(), "whitelist")) {
            return !utils.getStringList(utils.getBlocked_worlds()).contains(world);
        }
        return false;
    }
}
