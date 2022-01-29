package it.zS0bye.eLuckyBlock.methods;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.api.ILuckyBlockAPI;
import it.zS0bye.eLuckyBlock.database.SQLLuckyBlocks;
import it.zS0bye.eLuckyBlock.database.SQLLuckyBreaks;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.reflections.ActionField;
import it.zS0bye.eLuckyBlock.reflections.TitleField;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OpenLuckyBlock {

    private final ELuckyBlock plugin;
    private final Map<String, Integer> luckyBreaks;
    private final SQLLuckyBreaks sqlLuckyBreaks;
    private final SQLLuckyBlocks sqlLuckyBlocks;
    private final Set<Player> checkUnique = new HashSet<>();

    public OpenLuckyBlock() {
        this.plugin = ELuckyBlock.getInstance();
        this.luckyBreaks = plugin.getLuckyBreaks();
        this.sqlLuckyBreaks = plugin.getSqlLuckyBreaks();
        this.sqlLuckyBlocks = plugin.getSqlLuckyBlocks();
    }

    private void open(final String luckyblock, final Player player, final Location loc, final Cancellable e) {

        if (Lucky.PERMISSION_REQUIRED_ENABLED.getBoolean(luckyblock)) {

            Permission perm = new Permission(Lucky.PERMISSION_REQUIRED.getString(luckyblock), PermissionDefault.OP);
            perm.addParent("luckyblock.*", true);

            if (!player.hasPermission(perm)) {
                e.setCancelled(true);
                if (Lucky.PERMISSION_REQUIRED_MODULES_CHAT.getBoolean(luckyblock)) {
                    Lucky.PERMISSION_REQUIRED_CHAT.send(player, luckyblock);
                }
                if (Lucky.PERMISSION_REQUIRED_MODULES_TITLE.getBoolean(luckyblock)) {
                    new TitleField(player,
                            Lucky.PERMISSION_REQUIRED_TITLE.getString(luckyblock),
                            Lucky.PERMISSION_REQUIRED_TITLE_SUBTITLE.getString(luckyblock),
                            Lucky.PERMISSION_REQUIRED_TITLE_FADEIN.getInt(luckyblock),
                            Lucky.PERMISSION_REQUIRED_TITLE_STAY.getInt(luckyblock),
                            Lucky.PERMISSION_REQUIRED_TITLE_FADEOUT.getInt(luckyblock));
                }
                if (Lucky.PERMISSION_REQUIRED_MODULES_ACTION.getBoolean(luckyblock)) {
                    new ActionField(player,
                            Lucky.PERMISSION_REQUIRED_ACTION.getString(luckyblock)
                                    .replace("%prefix%", Config.SETTINGS_PREFIX.getString()));
                }
                return;
            }
        }

        if(this.luckyBreaks.containsKey(player.getName())) {
            this.luckyBreaks.put(player.getName(), this.luckyBreaks.get(player.getName()) + 1);
            this.sqlLuckyBreaks.addLuckyBreaks(player.getName(), this.luckyBreaks.get(player.getName()));
        }else {
            this.sqlLuckyBreaks.getLuckyBreaks(player.getName()).thenAccept(getLuckyBreaks -> {
                this.luckyBreaks.put(player.getName(), getLuckyBreaks + 1);
                this.sqlLuckyBreaks.addLuckyBreaks(player.getName(), getLuckyBreaks + 1);
            });
        }

        ILuckyBlockAPI api = ELuckyBlock.getApi();
        api.forcedOpening(luckyblock, player, loc);
    }

    public void open(String luckyblock, Player player, Block block, Cancellable e, boolean instant) {

        Location loc = block.getLocation();

        if(!Lucky.UNIQUE_CHECK_ENABLED.getBoolean(luckyblock)) {
            if(instant)
                block.setType(Material.AIR);
            open(luckyblock, player, block.getLocation(), e);
            return;
        }

        String convertLoc = sqlLuckyBlocks.convertLoc(loc);
        sqlLuckyBlocks.getLuckyBlock(convertLoc).thenAccept(luckyLoc -> {
            if(luckyblock.equals(luckyLoc)) {
                checkUnique.add(player);
            }
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                if(checkUnique.contains(player)) {
                    if(instant)
                        block.setType(Material.AIR);
                    open(luckyblock, player, block.getLocation(), e);
                    checkUnique.remove(player);
                }
            }
        }.runTaskLater(this.plugin, 1L);
    }
}
