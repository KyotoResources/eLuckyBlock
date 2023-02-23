package it.zS0bye.eLuckyBlock.commands.subcmds;

import it.zS0bye.eLuckyBlock.api.ILuckyBlockAPI;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.LuckyBlocksFile;
import it.zS0bye.eLuckyBlock.files.enums.LuckyFile;
import it.zS0bye.eLuckyBlock.utils.ItemUtils;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveSubCMD extends BaseCommand {

    private final String command;
    private final String permission;
    private String[] args;
    private final CommandSender sender;
    private ELuckyBlock plugin;

    public GiveSubCMD(final String command, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.args = args;
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public GiveSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        if(!sender.hasPermission(this.permission)) return;
        tab.add(getName());
    }

    public GiveSubCMD(final String command, final List<String> tab, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        if(args.length == 2)
            Bukkit.getOnlinePlayers().forEach(players -> {
                if(!sender.hasPermission(this.permission)) return;
                tab.add(players.getName());
            });
        if(args.length == 3)
            LuckyBlocksFile.getFiles(this.plugin).forEach(luckyblocks -> {
                if(!LuckyFile.UNIQUE_CHECK_ENABLED.getBoolean(luckyblocks)) return;
                tab.add(luckyblocks);
            });
    }

    @Override
    protected String getName() {
        return "give";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission(this.permission)) {
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

        if(!LuckyBlocksFile.getFiles(this.plugin).contains(args[2])) {
            Lang.GIVE_ERRORS_NOT_EXIST.send(sender);
            return;
        }

        if(!LuckyFile.UNIQUE_CHECK_ENABLED.getBoolean(args[2])) {
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


        StringUtils.send(sender, senderMsg);
        StringUtils.send(other, receiverMsg);
    }

}
