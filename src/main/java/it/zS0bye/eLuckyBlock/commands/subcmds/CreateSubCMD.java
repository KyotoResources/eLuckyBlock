package it.zS0bye.eLuckyBlock.commands.subcmds;

import dev.triumphteam.gui.guis.GuiItem;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.gui.CreateGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSubCMD extends BaseCommand {

    private final String command;
    private final String permission;
    private String[] args;
    private final CommandSender sender;
    private ELuckyBlock plugin;

    private final Map<String, GuiItem> items = new HashMap<>();

    public CreateSubCMD(final String command, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.args = args;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public CreateSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        if(!sender.hasPermission(this.permission)) return;
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "create";
    }

    @Override
    protected void execute() {

        if(!sender.hasPermission(this.permission)) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(sender instanceof ConsoleCommandSender) {
            Lang.NOT_A_PLAYER.send(sender);
            return;
        }

        final Player player = (Player) sender;
        new CreateGUI(this.plugin, args[1], this.items, player);
    }
}
