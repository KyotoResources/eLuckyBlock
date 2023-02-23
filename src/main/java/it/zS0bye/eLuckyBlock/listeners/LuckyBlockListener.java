package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.files.enums.LuckyFile;
import it.zS0bye.eLuckyBlock.objects.OpenLuckyBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckyBlockListener implements Listener {

    private final ELuckyBlock plugin;

    public LuckyBlockListener(final ELuckyBlock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final String world = player.getWorld().getName();
        final Location region = player.getLocation();

        if (event.isCancelled()) return;

        if (new LuckyChecker(player, block, world, region, luckyblocks)
                .check())
            return;

//        if(Hooks.PLOTSQUARED.isCheck() &&
//                !HooksManager.checkPlot(player.getUniqueId()))
//            return;

        open(e, player, block);
    }

    private void open(final BlockBreakEvent e, final Player player, final Block block) {

        OpenLuckyBlock luckyblock = new OpenLuckyBlock();
        luckyblock.open(luckyblocks, player, block, e, false);
    }

}
