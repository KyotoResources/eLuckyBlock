package it.zS0bye.eLuckyBlock.commands.admins;

import it.zS0bye.eLuckyBlock.api.ILuckyBlockAPI;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.utils.ItemUtils;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveCommand extends BaseCommand {

    private String[] args;
    private final CommandSender sender;
    private ELuckyBlock plugin;

    public GiveCommand(final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
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

    public GiveCommand(final List<String> tab, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
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
                if(Lucky.UNIQUE_CHECK_ENABLED.getBoolean(luckyblocks))
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
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        Player other = Bukkit.getPlayerExact(args[1]);

        ILuckyBlockAPI api = ELuckyBlock.getApi();
        if(other == null) {
            Lang.PLAYER_NOT_FOUND.send(sender);
            return;
        }

        int amount = 1;

        if(!this.plugin.getLucky().getConfig().contains(args[2])) {
            Lang.GIVE_ERRORS_NOT_EXIST.send(sender);
            return;
        }

        if(!Lucky.UNIQUE_CHECK_ENABLED.getBoolean(args[2])) {
            Lang.GIVE_ERRORS_NOT_UNIQUE.send(sender);
            return;
        }

        if(args.length == 4) {
            if(!NumberUtils.isNumber(args[3])) {
                Lang.IS_NOT_NUMBER.send(sender);
                return;
            }
            amount = Integer.parseInt(args[3]);
            if(amount <= 0) {
                Lang.ONLY_POSITIVE_NUMBERS.send(sender);
                return;
            }
        }

        if(api.give(args[2], amount) == null) {
            this.plugin.getLogger().severe(ItemUtils.ExceptionMsg());
            return;
        }

        other.getInventory().addItem(api.give(args[2], amount));
        String senderMsg = Lang.GIVE_ADMINS_SENDER.getCustomString()
                .replace("%luckyblock%", args[2])
                .replace("%amount%", String.valueOf(amount))
                .replace("%receiver%", other.getName());

        String receiverMsg = Lang.GIVE_ADMINS_RECEIVER.getCustomString()
                .replace("%luckyblock%", args[2])
                .replace("%amount%", String.valueOf(amount))
                .replace("%sender%", sender.getName());


        StringUtils.send(senderMsg, sender);
        StringUtils.send(receiverMsg, other);
    }

}
