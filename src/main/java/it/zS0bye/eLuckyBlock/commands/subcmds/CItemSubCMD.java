package it.zS0bye.eLuckyBlock.commands.subcmds;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.files.CItemsFile;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CItemSubCMD extends BaseCommand {

    private final String command;
    private final String permission;
    private String[] args;
    private final CommandSender sender;
    private ELuckyBlock plugin;

    public CItemSubCMD(final String command, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.args = args;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public CItemSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        if(!sender.hasPermission(this.permission)) return;
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "citem";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission(this.permission)) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        final Player player = (Player) sender;
        final CItemsFile file = new CItemsFile(this.plugin, args[1]);
        final ItemStack item = player.getInventory().getItemInMainHand();

        if(file.getFile().exists()) {
            String update = Lang.CITEM_ADMINS_UPDATE.getCustomString()
                    .replace("%item%", item.getType().toString());

            if(item.getItemMeta() != null
                    && item.getItemMeta().hasDisplayName())
                update = Lang.CITEM_ADMINS_UPDATE.getCustomString()
                        .replace("%item%", item.getItemMeta().getDisplayName());
            file.create(player.getInventory().getItemInMainHand());
            StringUtils.send(player, update);
            return;
        }

        String create = Lang.CITEM_ADMINS_CREATE.getCustomString()
                        .replace("%item%", item.getType().toString());

        if(item.getItemMeta() != null
        && item.getItemMeta().hasDisplayName())
            create = Lang.CITEM_ADMINS_CREATE.getCustomString()
                    .replace("%item%", item.getItemMeta().getDisplayName());

        file.create(player.getInventory().getItemInMainHand());
        StringUtils.send(player, create);
    }
}
