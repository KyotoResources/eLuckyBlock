package it.zS0bye.eLuckyBlock.commands.admins;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.files.enums.Rewards;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    private CommandSender sender;
    private ELuckyBlock plugin;

    public ReloadCommand(final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.sender = sender;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public ReloadCommand(final List<String> tab, final CommandSender sender) {
        if(sender.hasPermission("eluckyblock.command.reload"))
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission("eluckyblock.command.reload")) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        this.plugin.reloadConfig();
        for(Config config : Config.values())
            config.reloadConfig();

        this.plugin.getLang().reload();
        this.plugin.getLucky().reload();
        this.plugin.getAnimations().reload();
        this.plugin.getRewards().reload();
        this.plugin.getFireworks().reload();
        new HooksManager(this.plugin);
        Lang.RELOAD_ADMINS_CONFIGURATIONS.send(sender);
    }

}
