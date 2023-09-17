package it.zS0bye.eLuckyBlock;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import it.zS0bye.eLuckyBlock.executors.SendExecutors;
import it.zS0bye.eLuckyBlock.files.enums.LuckyFile;
import it.zS0bye.eLuckyBlock.mysql.SQLConversion;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import it.zS0bye.eLuckyBlock.rewards.ExcReward;
import it.zS0bye.eLuckyBlock.rewards.RandomReward;
import lombok.Getter;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class LuckyBlock {

    private final ELuckyBlock plugin;
    private final Player player;
    private final String name;
    private final ScoreTable score;

    private final String material;
    private final boolean instant_break;
    private final boolean unique_check;
    private final String unique_name;
    private final List<String> unique_lore;
    private final boolean prevents;
    private final boolean deny_pickup;
    private final boolean deny_absorb;
    private final String worlds_type;
    private final List<String> worlds_list;
    private final List<String> allowed_gamemodes;

    private final static Map<String, RandomReward<ExcReward>> randomMap = new HashMap<>();

    public LuckyBlock(final ELuckyBlock plugin, final Player player, final String name) {
        this.plugin = plugin;
        this.player = player;
        this.name = name;
        this.score = this.plugin.getScoreTable();
        this.material = LuckyFile.MATERIAL.getString(name);
        this.instant_break = LuckyFile.INSTANT_BREAK.getBoolean(name);
        this.unique_check = LuckyFile.UNIQUE_CHECK_ENABLED.getBoolean(name);
        this.unique_name = LuckyFile.UNIQUE_CHECK_NAME.getString(name);
        this.unique_lore = LuckyFile.UNIQUE_CHECK_LORE.getStringList(name);
        this.prevents = LuckyFile.PREVENTIONS_ENABLED.getBoolean(name);
        this.deny_pickup = LuckyFile.PREVENTIONS_DENY_PICKUP.getBoolean(name);
        this.deny_absorb = LuckyFile.PREVENTIONS_DENY_ABSORB.getBoolean(name);
        this.worlds_type = LuckyFile.PREVENTIONS_WORLDS_TYPE.getString(name);
        this.worlds_list = LuckyFile.PREVENTIONS_WORLDS_LIST.getStringList(name);
        this.allowed_gamemodes = LuckyFile.PREVENTIONS_ALLOWED_GAMEMODES.getStringList(name);
    }

    public boolean isLuckyBlock(final Block block) {
        if(this.isUniqueCheck(block)) return false;
        return XBlock.isSimilar(block, this.getMaterial());
    }

    public boolean isLuckyBlock(final ItemStack item) {
        final NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasNBTData() && nbtItem.getString("eLuckyBlock").equals(this.name);
    }

    private boolean isUniqueCheck(final Block block) {
        if(!this.unique_check) return false;
        final Set<Block> unique_block = new HashSet<>();
        this.plugin.getLuckyTable().getLuckyBlock(SQLConversion.convertLoc(block.getLocation())).thenAccept(luckyBlock -> {
            if(!Objects.equals(luckyBlock, this.name)) return;
            unique_block.add(block);
        });
        return unique_block.contains(block);
    }

    @SuppressWarnings("deprecation")
    private XMaterial getMaterial() {

        if(this.material.contains(":")) {
            final String val1 = this.material.split(":")[0];
            final String val2 = this.material.split(":")[1];
            Optional<XMaterial> material = XMaterial.matchXMaterial(val1);
            if(!NumberUtils.isDigits(val2)) return material.orElse(XMaterial.STONE);
            final byte data = NumberUtils.toByte(val2);
            if(NumberUtils.isDigits(val1)) {
                material = XMaterial.matchXMaterial(NumberUtils.toInt(val1), data);
                return material.orElse(XMaterial.STONE);
            }
            if(!material.isPresent()) return XMaterial.STONE;
            final ItemStack item = material.get().parseItem();
            if(item == null) return XMaterial.STONE;
            item.setDurability(data);
            return XMaterial.matchXMaterial(item);
        }

        if(NumberUtils.isDigits(this.material)) {
            final Optional<XMaterial> material = XMaterial.matchXMaterial(NumberUtils.toInt(this.material), (byte) 0);
            return material.orElse(XMaterial.STONE);
        }

        if(!this.material.startsWith("player-") && !this.material.startsWith("base64-")) {
            final Optional<XMaterial> material = XMaterial.matchXMaterial(this.material);
            return material.orElse(XMaterial.STONE);
        }

        final NBTItem nbt = new NBTItem(Objects.requireNonNull(XMaterial.PLAYER_HEAD.parseItem()));
        final NBTCompound skull = nbt.addCompound("SkullOwner");

        if(this.material.startsWith("player-")) {
            final String player = this.material.split("player-")[1];
            skull.setString("Name", player);
            return XMaterial.matchXMaterial(nbt.getItem());
        }

        if(this.material.startsWith("base64-")) {
            final String base64 = this.material.split("base64-")[1];
            final NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
            texture.setString("Value",  base64);
            return XMaterial.matchXMaterial(nbt.getItem());
        }

        return XMaterial.STONE;
    }

    private boolean stayWorlds() {
        if(!this.prevents || this.worlds_list.isEmpty()) return false;
        final String world = this.player.getWorld().getName();
        if(this.worlds_type.equalsIgnoreCase("blacklist")) return this.worlds_list.contains(world);
        if(this.worlds_type.equalsIgnoreCase("whitelist")) return !this.worlds_list.contains(world);
        return false;
    }

    public boolean canAbsorb() {
        return this.prevents && !this.deny_absorb;
    }

    public boolean canGameMode() {
        if(this.player.hasPermission("eluckyblock.bypass.gamemode")
                || !this.prevents
                || this.allowed_gamemodes.isEmpty()
                || this.allowed_gamemodes.contains("*")) return false;
        return !this.allowed_gamemodes.contains(this.player.getGameMode().name());
    }

    public ItemStack give(final int amount) {
        final ItemStack item = Objects.requireNonNull(this.getMaterial().parseItem());
        item.setAmount(amount);

        final NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("eLuckyBlock", this.name);
        return nbtItem.getItem();
    }

    public void open(final Block block, final boolean instant) {
        if(!this.isLuckyBlock(block)) return;
        if(this.canGameMode() || this.stayWorlds()) return;
        if(instant) {
            if(!this.instant_break) return;
            block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
            block.setType(Material.AIR);
        }
        if(this.prevents && this.deny_pickup) block.setType(Material.AIR);
        this.forceOpen(block.getLocation());
    }

    public void forceOpen(final Location location) {
        if (!randomMap.containsKey(this.name)) return;
        randomMap.get(this.name).getRandomValue().getCommands().forEach(executor -> SendExecutors.send(this.plugin, executor, player, location));
        this.score.addScore(this.player.getName());
    }

    public static void addRewards(final ELuckyBlock plugin) {
        plugin.getLuckyblocks().forEach(luckyblock -> {
            if (randomMap.containsKey(luckyblock)) return;
            final RandomReward<ExcReward> random = new RandomReward<>();
            for (final String reward : LuckyFile.REWARDS.getConfigurationSection(luckyblock)) {
                final String path = LuckyFile.REWARDS.getPath() + "." + reward;
                random.add(LuckyFile.REWARDS_CHANCE.getDouble(luckyblock, path), new ExcReward(LuckyFile.REWARDS_EXECUTORS.getStringList(luckyblock, path)));
            }
            randomMap.put(luckyblock, random);
        });
    }

    public static List<LuckyBlock> getLuckyBlocks(final ELuckyBlock plugin, final Player player) {
        final List<LuckyBlock> luckyblocks = new ArrayList<>();
        plugin.getLuckyblocks().forEach(luckyblock -> luckyblocks.add(new LuckyBlock(plugin, player, luckyblock)));
        return luckyblocks;
    }

}
