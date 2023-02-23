package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.files.enums.LuckyFile;
import it.zS0bye.eLuckyBlock.mysql.SQLConversion;
import it.zS0bye.eLuckyBlock.mysql.tables.LuckyTable;
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
    private final LuckyTable luckyTable;

    public UniqueBlockListener(final String luckyblocks) {
        this.luckyblocks = luckyblocks;
        this.luckyTable = ELuckyBlock.getInstance().getLuckyTable();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        Block block = e.getBlockPlaced();
        String world = player.getWorld().getName();
        Location region = player.getLocation();
        Location location = block.getLocation();
        String convertLoc = SQLConversion.convertLoc(location);

        if(new LuckyChecker(block, world, region, luckyblocks)
                .check())
            return;

        if(item.getItemMeta() == null)
            return;

        String displayName = item.getItemMeta().getDisplayName();
        List<String> lore = item.getItemMeta().getLore();

        if (item.getItemMeta().hasDisplayName()
        && displayName.equals(LuckyFile.UNIQUE_CHECK_NAME.getString(luckyblocks))) {

            if(!LuckyFile.UNIQUE_CHECK_LORE.contains(luckyblocks)
            || LuckyFile.UNIQUE_CHECK_LORE.getStringList(luckyblocks).size() == 0
            && !item.getItemMeta().hasLore()) {
                this.luckyTable.setLocation(convertLoc, luckyblocks);
                return;
            }

            if(item.getItemMeta().hasLore() &&
                    lore.equals(LuckyFile.UNIQUE_CHECK_LORE.getStringList(luckyblocks))) {
               this.luckyTable.setLocation(convertLoc, luckyblocks);
            }

        }
    }
}
