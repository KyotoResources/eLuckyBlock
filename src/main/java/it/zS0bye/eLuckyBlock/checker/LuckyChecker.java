package it.zS0bye.eLuckyBlock.checker;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import it.zS0bye.eLuckyBlock.utils.ItemUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Objects;

public class LuckyChecker {

    private Player player;
    private final Block block;
    private final Material material;
    private final String world;
    private final org.bukkit.Location region;
    private final String luckyblock;

    public LuckyChecker(final Player player, final Block block, final String world, final org.bukkit.Location region, final String luckyblock) {
        this.player = player;
        this.block = block;
        this.material = block.getType();
        this.world = world;
        this.region = region;
        this.luckyblock = luckyblock;
    }

    public LuckyChecker(final Block block, final String world, final org.bukkit.Location region, final String luckyblock) {
        this.block = block;
        this.material = block.getType();
        this.world = world;
        this.region = region;
        this.luckyblock = luckyblock;
    }

    public boolean check() {

        if(VersionChecker.getV1_8()
                || VersionChecker.getV1_9()
                || VersionChecker.getV1_10()
                || VersionChecker.getV1_11()
                || VersionChecker.getV1_12()) {
            if(checkLegacyMaterial())
                return true;

        }else {
            if(checkMaterial())
                return true;
        }

        if(checkWorlds())
            return true;

        if(this.player != null)
        if(checkGameModes())
            return true;

        if(Config.HOOKS_WORLDGUARD.getBoolean())
            return checkRegion();


        return false;
    }

    private boolean checkMaterial() {
        String material = Lucky.MATERIAL.getString(this.luckyblock);

        if(this.material == Material.PLAYER_HEAD) {
            Skull skull = (Skull) this.block.getState();

            if(material.startsWith("player-"))
                return !Objects.equals(skull.getOwner(), material.split("player-")[1]);

            if(material.startsWith("base64-")) {
                if(ItemUtils.hasCustomSkull(skull))
                    return !ItemUtils.isSkullCorrect(skull, material.split("base64-")[1]);
            }
        }

        return this.material != ConvertUtils.getMaterial(material);
    }

    private boolean checkLegacyMaterial() {
        String material = Lucky.MATERIAL.getString(this.luckyblock);

        if(this.material == Material.valueOf("SKULL")
        && this.material.getId() == (short) 3) {
            Skull skull = (Skull) this.block.getState();

            if(material.startsWith("player-"))
                return !Objects.equals(skull.getOwner(), material.split("player-")[1]);

            if(material.startsWith("base64-")) {
                if(ItemUtils.hasCustomSkull(skull))
                    return !ItemUtils.isSkullCorrect(skull, material.split("base64-")[1]);
            }

        }else {
            if (NumberUtils.isNumber(material)) {
                if (this.material.getId() == Integer.parseInt(material)) {
                    if (Lucky.DATA.contains(this.luckyblock)) {
                        return this.block.getData() != Lucky.DATA.getInt(this.luckyblock);
                    } else {
                        return false;
                    }
                }
            } else {
                if (this.material == ConvertUtils.getMaterial(material)) {
                    if (Lucky.DATA.contains(this.luckyblock)) {
                        return this.block.getData() != Lucky.DATA.getInt(this.luckyblock);
                    } else {
                        return false;
                    }
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
                if (Lucky.PREVENT_BLOCKED_REGIONS_TYPE.getString(luckyblock)
                        .equalsIgnoreCase("blacklist")) {
                    return Lucky.PREVENT_BLOCKED_REGIONS.getStringList(luckyblock).contains(regions.getId());
                } else if (Lucky.PREVENT_BLOCKED_REGIONS_TYPE.getString(luckyblock)
                        .equalsIgnoreCase("whitelist")) {
                    return !Lucky.PREVENT_BLOCKED_REGIONS.getStringList(luckyblock).contains(regions.getId());
                }
            }
        }
        return false;
    }

    private boolean checkWorlds() {
        if(Lucky.PREVENT_BLOCKED_WORLDS_TYPE.getString(luckyblock)
                .equalsIgnoreCase("blacklist")) {
            return Lucky.PREVENT_BLOCKED_WORLDS.getStringList(luckyblock).contains(world);
        }else if(Lucky.PREVENT_BLOCKED_WORLDS_TYPE.getString(luckyblock)
                .equalsIgnoreCase("whitelist")) {
            return !Lucky.PREVENT_BLOCKED_WORLDS.getStringList(luckyblock).contains(world);
        }
        return false;
    }

    private boolean checkGameModes() {
        if(this.player.hasPermission("eluckyblock.bypass.gamemode"))
            return false;

        List<String> gmList = Lucky.PREVENT_GAMEMODES.getStringList(luckyblock);
        if(gmList.contains("*"))
            return false;

        return !gmList.contains(this.player.getGameMode().name());
    }
}
