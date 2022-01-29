package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.database.SQLLuckyBlocks;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UniqueBlockListener implements Listener {

    private final String luckyblocks;
    private final SQLLuckyBlocks sqlLuckyBlocks;

    public UniqueBlockListener(final String luckyblocks) {
        this.luckyblocks = luckyblocks;
        this.sqlLuckyBlocks = ELuckyBlock.getInstance().getSqlLuckyBlocks();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        Block block = e.getBlockPlaced();
        String world = player.getWorld().getName();
        Location region = player.getLocation();
        Location location = block.getLocation();
        String convertLoc = this.sqlLuckyBlocks.convertLoc(location);

        if(new LuckyChecker(block, world, region, luckyblocks)
                .check())
            return;

        if(item.getItemMeta() == null)
            return;

        String displayName = item.getItemMeta().getDisplayName();
        List<String> lore = item.getItemMeta().getLore();

        if (item.getItemMeta().hasDisplayName()
        && displayName.equals(Lucky.UNIQUE_CHECK_NAME.getString(luckyblocks))) {

            if(!Lucky.UNIQUE_CHECK_LORE.contains(luckyblocks)
            || Lucky.UNIQUE_CHECK_LORE.getStringList(luckyblocks).size() == 0
            && !item.getItemMeta().hasLore()) {
                this.sqlLuckyBlocks.setLocation(convertLoc, luckyblocks);
                return;
            }

            if(item.getItemMeta().hasLore() &&
                    lore.equals(Lucky.UNIQUE_CHECK_LORE.getStringList(luckyblocks))) {
               this.sqlLuckyBlocks.setLocation(convertLoc, luckyblocks);
            }

        }
    }
}
