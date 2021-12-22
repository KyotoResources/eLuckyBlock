package it.zS0bye.eLuckyBlock.commands.admins;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.utils.FileUtils;
import it.zS0bye.eLuckyBlock.utils.LangUtils;
import it.zS0bye.eLuckyBlock.utils.LuckyUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveCommand extends BaseCommand {

    private String[] args;
    private final CommandSender sender;
    private eLuckyBlock plugin;
    private LuckyUtils utils;

    public GiveCommand(final String[] args, final CommandSender sender, final eLuckyBlock plugin) {
        this.args = args;
        this.sender = sender;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public GiveCommand(final List<String> tab, final CommandSender sender) {
        this.sender = sender;
        if(sender.hasPermission("eluckyblock.command.give"))
        tab.add(getName());
    }

    public GiveCommand(final List<String> tab, final String[] args, final CommandSender sender, final eLuckyBlock plugin) {
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(getName())) {
            return;
        }
        if(args.length == 2)
            Bukkit.getOnlinePlayers().forEach(players -> {
                if(sender.hasPermission("eluckyblock.command.give"))
                tab.add(players.getName());
            });
        if(args.length == 3)
            this.plugin.getLucky().getConfig().getKeys(false).forEach(luckyblocks -> {
                utils = new LuckyUtils(luckyblocks);
                if(utils.getBoolean(utils.getUnique_check_enabled()))
                tab.add(luckyblocks);
            });
    }

    @Override
    protected String getName() {
        return "give";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission("eluckyblock.command.give")) {
            LangUtils.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        Player other = Bukkit.getPlayerExact(args[1]);
        if(other == null) {
            LangUtils.PLAYER_NOT_FOUND.send(sender);
            return;
        }

        utils = new LuckyUtils(args[2]);
        int amount = 1;

        if(!utils.contains(args[2])) {
            LangUtils.GIVE_ERRORS_NOT_EXIST.send(sender);
            return;
        }

        if(!utils.getBoolean(utils.getUnique_check_enabled())) {
            LangUtils.GIVE_ERRORS_NOT_UNIQUE.send(sender);
            return;
        }

        if(args.length == 4) {
            if(!NumberUtils.isNumber(args[3])) {
                LangUtils.IS_NOT_NUMBER.send(sender);
                return;
            }
            amount = Integer.parseInt(args[3]);
            if(amount <= 0) {
                LangUtils.ONLY_POSITIVE_NUMBERS.send(sender);
                return;
            }
        }

        other.getInventory().addItem(utils.give(amount));

        String senderMsg = LangUtils.GIVE_ADMINS_SENDER.getCustomString()
                .replace("%luckyblock%", args[2])
                .replace("%amount%", String.valueOf(amount))
                .replace("%receiver%", other.getName());

        String receiverMsg = LangUtils.GIVE_ADMINS_RECEIVER.getCustomString()
                .replace("%luckyblock%", args[2])
                .replace("%amount%", String.valueOf(amount))
                .replace("%sender%", sender.getName());


        FileUtils.send(senderMsg, sender);
        FileUtils.send(receiverMsg, other);
    }

}
