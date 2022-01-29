package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.checker.LuckyChecker;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.hooks.enums.Hooks;
import it.zS0bye.eLuckyBlock.methods.OpenLuckyBlock;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InstantBlockListener implements Listener {

    private final ELuckyBlock plugin;
    private final String luckyblocks;

    public InstantBlockListener(final String luckyblocks) {
        this.plugin = ELuckyBlock.getInstance();
        this.luckyblocks = luckyblocks;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        String world = player.getWorld().getName();
        Location region = player.getLocation();

        if(block == null)
            return;

        if(!Lucky.INSTANT_BREAK.getBoolean(luckyblocks))
            return;

        if (new LuckyChecker(player, block, world, region, luckyblocks)
                .check())
            return;

        if(Hooks.PLOTSQUARED.isCheck() &&
                !HooksManager.checkPlot(player.getUniqueId()))
            return;

        if(e.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        if(player.getGameMode() == GameMode.CREATIVE)
            return;

        OpenLuckyBlock luckyblock = new OpenLuckyBlock();
        luckyblock.open(luckyblocks, player, block, e, true);
    }

}
