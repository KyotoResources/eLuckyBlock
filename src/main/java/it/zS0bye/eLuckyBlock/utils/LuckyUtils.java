package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.database.SQLLuckyBlocks;
import it.zS0bye.eLuckyBlock.database.SQLLuckyBreaks;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class LuckyUtils extends FileUtils {

    private final eLuckyBlock plugin;
    private final FileConfiguration lucky;
    private final FileConfiguration rewardsFile;
    private final Map<String, Integer> luckyBreaks;
    private final SQLLuckyBreaks sqlLuckyBreaks;
    private final SQLLuckyBlocks sqlLuckyBlocks;
    private final Set<Player> checkUnique = new HashSet<>();
    private final String material;
    private final String data;
    private final String instant_break;
    private final String unique_check_enabled;
    private final String unique_check_displayName;
    private final String unique_check_lore;
    private final String rewards;
    private final String deny_pickup;
    private final String deny_absorb;
    private final String perms_required;
    private final String perms_required_enabled;
    private final String perms_required_chat_enabled;
    private final String perms_required_chat;
    private final String perms_required_title_enabled;
    private final String perms_required_title;
    private final String perms_required_title_subtitle;
    private final String perms_required_title_fadein;
    private final String perms_required_title_stay;
    private final String perms_required_title_fadeout;
    private final String perms_required_action_enabled;
    private final String perms_required_action;
    private final String blocked_worlds_type;
    private final String blocked_worlds;
    private final String blocked_regions_type;
    private final String blocked_regions;
    private final String allowed_gamemodes;

    public LuckyUtils(final String luckyblock) {
        this.plugin = eLuckyBlock.getInstance();
        this.lucky = plugin.getLucky().getConfig();
        this.luckyBreaks = plugin.getLuckyBreaks();
        this.sqlLuckyBreaks = plugin.getSqlLuckyBreaks();
        this.sqlLuckyBlocks = plugin.getSqlLuckyBlocks();
        this.rewardsFile = plugin.getRewards().getConfig();
        this.material = luckyblock + ".material";
        this.data = luckyblock + ".data";
        this.instant_break = luckyblock + ".instant_break";
        this.unique_check_enabled = luckyblock + ".unique_check.enabled";
        this.unique_check_displayName = luckyblock + ".unique_check.displayName";
        this.unique_check_lore = luckyblock + ".unique_check.lore";
        this.rewards = luckyblock + ".rewards";
        this.deny_pickup = luckyblock + ".prevent.deny_pickup";
        this.deny_absorb = luckyblock + ".prevent.deny_absorb";
        this.perms_required = luckyblock + ".permission_required.permission";
        this.perms_required_enabled = luckyblock + ".permission_required.enabled";
        this.perms_required_chat_enabled= luckyblock + ".permission_required.modules.chat";
        this.perms_required_chat = luckyblock + ".permission_required.chat";
        this.perms_required_title_enabled = luckyblock + ".permission_required.modules.title";
        this.perms_required_title = luckyblock + ".permission_required.title.title";
        this.perms_required_title_subtitle = luckyblock + ".permission_required.title.subtitle";
        this.perms_required_title_fadein = luckyblock + ".permission_required.title.fade-in";
        this.perms_required_title_stay = luckyblock + ".permission_required.title.stay";
        this.perms_required_title_fadeout = luckyblock  + ".permission_required.title.fade-out";
        this.perms_required_action_enabled = luckyblock + ".permission_required.modules.action";
        this.perms_required_action = luckyblock + ".permission_required.action";
        this.blocked_worlds_type = luckyblock + ".prevent.blocked_worlds.type";
        this.blocked_worlds = luckyblock + ".prevent.blocked_worlds.worlds";
        this.blocked_regions_type = luckyblock + ".prevent.blocked_regions.type";
        this.blocked_regions = luckyblock + ".prevent.blocked_regions.regions";
        this.allowed_gamemodes = luckyblock + ".prevent.allowed_gamemodes";
    }

    protected String getPrefix() {
        return ConfigUtils.SETTINGS_PREFIX.getString();
    }

    public String getString(final String path) {
        return ColorUtils.getColor(this.lucky.getString(path));
    }

    public boolean contains(final String path) {
        return this.lucky.contains(path);
    }

    public boolean equals(final String path, final String equals) {
        return this.lucky.getString(path).equalsIgnoreCase(equals);
    }

    public List<String> getStringList(final String path) {
        List<String> list = new ArrayList<>();
        for(String setList : this.lucky.getStringList(path)) {
            list.add(ColorUtils.getColor(setList));
        }
        return list;
    }

    public boolean getBoolean(final String path) {
        return this.lucky.getBoolean(path);
    }

    public int getInt(final String path) {
        return this.lucky.getInt(path);
    }

    public ItemStack give(final int amount) {
        int data;
        String material = getString(getMaterial());
        String displayName = getString(getUnique_check_displayName());
        List<String> lore = getStringList(getUnique_check_lore());

        if(NumberUtils.isNumber(material)) {
            int id = Integer.parseInt(material);
            if(contains(getData())) {
                data = getInt(getData());
                return ItemUtils.createItem(id, (byte) data, amount, displayName, lore);
            }
            return ItemUtils.createItem(id, amount, displayName, lore);
        }

        if(contains(getData())) {
            data = getInt(getData());
            return ItemUtils.createItem(material, (short) data,amount, displayName, lore);
        }

        return ItemUtils.createItem(material, amount, displayName, lore);
    }

    public void open(final String luckyblocks, final Player player, final Location loc, final Cancellable e) {

        if(!getBoolean(getUnique_check_enabled())) {
            luckyblock(player, loc, e);
            return;
        }

        String convertLoc = this.sqlLuckyBlocks.convertLoc(loc);
        this.sqlLuckyBlocks.getLuckyBlock(convertLoc).thenAccept(luckyLoc -> {
            if(luckyblocks.equals(luckyLoc)) {
                checkUnique.add(player);
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                if(checkUnique.contains(player)) {
                    luckyblock(player, loc, e);
                    checkUnique.remove(player);
                }
            }
        }.runTaskLater(this.plugin, 1L);
    }

    public void open(final String luckyblocks, final Player player, final Block block, final Cancellable e) {
        Location loc = block.getLocation();

        if(!getBoolean(getUnique_check_enabled())) {
            block.setType(Material.AIR);
            luckyblock(player, loc, e);
            return;
        }

        String convertLoc = this.sqlLuckyBlocks.convertLoc(loc);
        this.sqlLuckyBlocks.getLuckyBlock(convertLoc).thenAccept(luckyLoc -> {
            if(luckyblocks.equals(luckyLoc)) {
                checkUnique.add(player);
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                if(checkUnique.contains(player)) {
                    block.setType(Material.AIR);
                    luckyblock(player, loc, e);
                    checkUnique.remove(player);
                }
            }
        }.runTaskLater(this.plugin, 1L);
    }


    private void luckyblock(final Player player, final Location loc, final Cancellable e) {

        if (getBoolean(getPerms_required_enabled())) {

            Permission perm = new Permission(getString(getPerms_required()), PermissionDefault.OP);
            perm.addParent("luckyblock.*", true);

            if (!player.hasPermission(perm)) {
                e.setCancelled(true);

                if (getBoolean(getPerms_required_chat_enabled())) {
                    send(player, getPerms_required_chat());
                }
                if (getBoolean(getPerms_required_title_enabled())) {
                    new TitleField(player,
                            getString(getPerms_required_title()),
                            getString(getPerms_required_title_subtitle()),
                            getInt(getPerms_required_title_fadein()),
                            getInt(getPerms_required_title_stay()),
                            getInt(getPerms_required_title_fadeout()));
                }
                if (getBoolean(getPerms_required_action_enabled())) {
                    new ActionField(player,
                            getString(getPerms_required_action())
                                    .replace("%prefix%", ConfigUtils.SETTINGS_PREFIX.getString()));
                }
                return;
            }
        }

        if(luckyBreaks.containsKey(player.getName())) {
            luckyBreaks.put(player.getName(), luckyBreaks.get(player.getName()) + 1);
            this.sqlLuckyBreaks.addLuckyBreaks(player.getName(), luckyBreaks.get(player.getName()));
        }else {
            this.sqlLuckyBreaks.getLuckyBreaks(player.getName()).thenAccept(getLuckyBreaks -> {
                luckyBreaks.put(player.getName(), getLuckyBreaks + 1);
                this.sqlLuckyBreaks.addLuckyBreaks(player.getName(), getLuckyBreaks + 1);
            });
        }


        ThreadLocalRandom random = ThreadLocalRandom.current();
        String LuckyReward = getString(getRewards());
        RewardUtils rewardUtils;
        for (String reward : this.rewardsFile.getConfigurationSection(LuckyReward).getKeys(false)) {
             rewardUtils = new RewardUtils(LuckyReward, reward);
            if (reward.equalsIgnoreCase("nothing"))
                continue;

            int chance = rewardUtils.getInt(rewardUtils.getChance());

            if (chance >= random.nextInt(100)) {
                rewardUtils.sendReward(player, loc);
                return;
            }

        }

        rewardUtils = new RewardUtils(LuckyReward, "nothing");
        rewardUtils.sendReward(player, loc);

    }
}
