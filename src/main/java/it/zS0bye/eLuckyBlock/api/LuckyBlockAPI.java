package it.zS0bye.eLuckyBlock.api;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.LuckyFile;
import it.zS0bye.eLuckyBlock.utils.ItemUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LuckyBlockAPI implements ILuckyBlockAPI {

    private final ELuckyBlock plugin;

    public LuckyBlockAPI() {
        this.plugin = ELuckyBlock.getInstance();
    }

    @Override
    public void forcedOpening(String luckyblock, Player player) {
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
//            if(plugin.getRandomReward().containsKey(Lucky.REWARDS.getString(luckyblock)))
//                plugin.getRandomReward().get(Lucky.REWARDS.getString(luckyblock)).getRandomValue().getCommands().forEach(command -> sendReward(player, player.getLocation(), command));
        }, 2L);
    }

    @Override
    public void forcedOpening(String luckyblock, Player player, Location loc) {
//        if(plugin.getRandomReward().containsKey(Lucky.REWARDS.getString(luckyblock)))
//            plugin.getRandomReward().get(Lucky.REWARDS.getString(luckyblock)).getRandomValue().getCommands().forEach(command -> sendReward(player, loc, command));
    }

    @Override
    public ItemStack give(final String luckyblock, final int amount) {
        int data;
        String material = LuckyFile.MATERIAL.getString(luckyblock);
        String displayName = LuckyFile.UNIQUE_CHECK_NAME.getString(luckyblock);
        List<String> lore = LuckyFile.UNIQUE_CHECK_LORE.getStringList(luckyblock);

        if(material.startsWith("player-"))
            return ItemUtils.createSkull(material.split("player-")[1], amount, displayName, lore);

        if(material.startsWith("base64-"))
            return ItemUtils.createSkull(amount, displayName, lore, material.split("base64-")[1]);

        if(NumberUtils.isNumber(material)) {
            int id = Integer.parseInt(material);
            if(LuckyFile.DATA.contains(luckyblock)) {
                data = LuckyFile.DATA.getInt(luckyblock);
                return ItemUtils.createItem(id, (byte) data, amount, displayName, lore);
            }
            return ItemUtils.createItem(id, amount, displayName, lore);
        }

        if(LuckyFile.DATA.contains(luckyblock)) {
            data = LuckyFile.DATA.getInt(luckyblock);
            return ItemUtils.createItem(material, (short) data, amount, displayName, lore);
        }

        return ItemUtils.createItem(material, amount, displayName, lore);
    }

}
