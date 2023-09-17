package it.zS0bye.eLuckyBlock.commands.subcmds;

import it.zS0bye.eLuckyBlock.LuckyBlock;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.*;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadSubCMD extends BaseCommand {

    private final String command;
    private final String permission;
    private CommandSender sender;
    private ELuckyBlock plugin;

    public ReloadSubCMD(final String command, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public ReloadSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        if(!sender.hasPermission(this.permission)) return;
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission(this.permission)) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        this.plugin.reloadConfig();
        for(final Config config : Config.values()) config.reloadConfig();

        if(this.plugin.getLanguagesFile().reload()) {
            for(final Lang lang : Lang.values()) lang.reloadConfig();
        }

        if(this.plugin.getAnimationsFile().reload()) {
            for(final Animations animation : Animations.values()) animation.reloadConfig();
        }

        if(this.plugin.getFireworksFile().reload()) {
            for(final Fireworks firework : Fireworks.values()) firework.reloadConfig();
        }

        this.plugin.loadLuckyBlocks(true);
        LuckyBlock.addRewards(this.plugin);
        Lang.RELOAD_CONFIGURATIONS.send(sender);
    }

}
