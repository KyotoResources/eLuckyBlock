package it.zS0bye.eLuckyBlock.commands.admins;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.utils.LangUtils;
import it.zS0bye.eLuckyBlock.utils.LuckyUtils;
import it.zS0bye.eLuckyBlock.utils.RewardUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    private CommandSender sender;
    private eLuckyBlock plugin;

    public ReloadCommand(final String[] args, final CommandSender sender, final eLuckyBlock plugin) {
        this.sender = sender;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public ReloadCommand(final List<String> tab, final CommandSender sender) {
        if(sender.hasPermission("luckyblock.command.reload"))
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission("luckyblock.command.reload")) {
            LangUtils.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }
        this.plugin.reloadConfig();
        this.plugin.getLang().saveDefaultConfig();
        this.plugin.getLucky().saveDefaultConfig();
        this.plugin.getAnimations().saveDefaultConfig();
        this.plugin.getRewards().saveDefaultConfig();
        this.plugin.getFireworks().saveDefaultConfig();
        for (String luckyblock : this.plugin.getLucky().getConfig().getKeys(false)) {
            LuckyUtils utils = new LuckyUtils(luckyblock);
            String LuckyReward = utils.getString(utils.getRewards());
            new RewardUtils(luckyblock, LuckyReward);
        }
        LangUtils.RELOAD_ADMINS_CONFIGURATIONS.send(sender);
    }

}
