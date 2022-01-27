package it.zS0bye.eLuckyBlock.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ILuckyBlockAPI {

    void forcedOpening(String luckyblock, Player player);

    void forcedOpening(String luckyblock, Player player, Location loc);

    ItemStack give(final String luckyblock, final int amount);

}
