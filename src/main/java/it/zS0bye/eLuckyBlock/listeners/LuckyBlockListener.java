package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.database.SQLLuckyBlock;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import it.zS0bye.eLuckyBlock.executors.*;
import it.zS0bye.eLuckyBlock.utils.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LuckyBlockListener extends LuckyUtils implements Listener {

    private final FileConfiguration rewards;
    private final String luckyblocks;
    private final Map<String, Integer> luckyBreaks;

    public LuckyBlockListener(final eLuckyBlock plugin, final String luckyblocks) {
        super(luckyblocks);
        this.rewards = plugin.getRewards().getConfig();
        this.luckyBreaks = plugin.getLuckyBreaks();
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        String world = p.getWorld().getName();
        Location region = p.getLocation();

        if (new CheckLucky(block, world, region, luckyblocks)
                .check()) {
            return;
        }

        if (e.isCancelled()) {
            return;
        }


        if (getBoolean(getDeny_pickup())) {

            if (VersionChecker.getV1_8()
                    || VersionChecker.getV1_9()
                    || VersionChecker.getV1_10()
                    || VersionChecker.getV1_11()) {
                e.getBlock().setType(Material.AIR);
            } else {
                e.setDropItems(false);
            }

        }

        if (getBoolean(getPerms_required_enabled())) {

            Permission perm = new Permission(getString(getPerms_required()), PermissionDefault.OP);
            perm.addParent("luckyblock.*", true);

            if (!p.hasPermission(perm)) {
                e.setCancelled(true);

                if (getBoolean(getPerms_required_chat_enabled())) {
                    send(p, getPerms_required_chat());
                }
                if (getBoolean(getPerms_required_title_enabled())) {
                    new TitleField(p,
                            getString(getPerms_required_title()),
                            getString(getPerms_required_title_subtitle()),
                            getInt(getPerms_required_title_fadein()),
                            getInt(getPerms_required_title_stay()),
                            getInt(getPerms_required_title_fadeout()));
                }
                       if (getBoolean(getPerms_required_action_enabled())) {
                           new ActionField(p,
                                   getString(getPerms_required_action())
                                           .replace("%prefix%", ConfigUtils.SETTINGS_PREFIX.getString()));
                       }
                       return;
                   }
        }

        p.playEffect(block.getLocation(),
                ConvertUtils.getParticles(getString(getParticles())),
                ConvertUtils.getParticles(getString(getParticles())).getData());

        if(luckyBreaks.containsKey(p.getName())) {
            luckyBreaks.put(p.getName(), luckyBreaks.get(p.getName()) + 1);
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        String LuckyReward = getString(getRewards());
        for (String reward : this.rewards.getConfigurationSection(LuckyReward).getKeys(false)) {
            RewardUtils rewardUtils = new RewardUtils(LuckyReward, reward);
            if (reward.equalsIgnoreCase("nothing"))
                continue;

            int chance = rewardUtils.getInt(rewardUtils.getChance());

            if (chance >= random.nextInt(100)) {
                sendReward(LuckyReward, reward, p, e);
                return;
            }
        }

        sendReward(LuckyReward, "nothing", p, e);
    }

    public void sendReward(final String LuckyReward, final String reward, final Player p, final BlockBreakEvent e) {
        RewardUtils rewardUtils = new RewardUtils(LuckyReward, reward);
        rewardUtils.getStringList(rewardUtils.getExecute()).forEach(execute -> {
            new TitleExecutor(execute, p);
            new ActionExecutor(execute, p);
            new BossBarExecutor(execute, p);
            new BroadcastActionExecutor(execute, p);
            new BroadcastExecutor(execute, p);
            new BroadcastTitleExecutor(execute, p);
            new BroadcastBossBarExecutor(execute, p);
            new ClearEffectExecutor(execute, p);
            new ConsoleExecutor(execute, p);
            new EffectExecutor(execute, p);
            new MessageExecutor(execute, p);
            new PlayerExecutor(execute, p);
            new SoundExecutor(execute, p);
            new SudoExecutor(execute, p);
            new SpawnMobExecutor(execute, e.getBlock().getLocation());
            new ItemExecutor(execute, e.getBlock().getLocation());
        });
    }

}
