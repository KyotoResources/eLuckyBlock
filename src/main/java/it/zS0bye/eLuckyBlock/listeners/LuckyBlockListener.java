package it.zS0bye.eLuckyBlock.listeners;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.LuckyBlock;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LuckyBlockListener implements Listener {

    private final ELuckyBlock plugin;
    private final ScoreTable score;

    public LuckyBlockListener(final ELuckyBlock plugin) {
        this.plugin = plugin;
        this.score = this.plugin.getScoreTable();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        this.score.initScore(player.getName());
        this.score.importScore(player.getName());
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        this.score.exportScore(player.getName());
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        if (event.isCancelled()) return;
        LuckyBlock.getLuckyBlocks(this.plugin, player).forEach(luckyblock -> luckyblock.open(block, false));
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        if(block == null) return;
        if(event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if(player.getGameMode() == GameMode.CREATIVE) return;
        LuckyBlock.getLuckyBlocks(this.plugin, player).forEach(luckyblock -> luckyblock.open(block, true));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Firework)) return;
        final Firework firework = (Firework) event.getDamager();
        if (!firework.hasMetadata("eLuckyBlock")) return;
        event.setCancelled(true);
    }

}
