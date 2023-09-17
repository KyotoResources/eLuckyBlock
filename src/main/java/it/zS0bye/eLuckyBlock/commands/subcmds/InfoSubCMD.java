package it.zS0bye.eLuckyBlock.commands.subcmds;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class InfoSubCMD extends BaseCommand {

    private final String command;
    private final String permission;
    private final CommandSender sender;
    private ScoreTable score;
    private String[] args;
    private String playerName;

    public InfoSubCMD(final String command, final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.score = plugin.getScoreTable();
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.args = args;
        this.sender = sender;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public InfoSubCMD(final String command, final List<String> tab, final CommandSender sender) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        if(!sender.hasPermission(this.permission)) return;
        tab.add(getName());
    }

    public InfoSubCMD(final String command, final List<String> tab, final String[] args, final CommandSender sender) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.sender = sender;
        if(args[0].equalsIgnoreCase(getName()))
            Bukkit.getOnlinePlayers().forEach(players -> {
                if(!sender.hasPermission(this.permission + ".others")) return;
                tab.add(players.getName());
            });
    }

    @Override
    protected String getName() {
        return "info";
    }

    @Override
    protected void execute() {
        if(!this.sender.hasPermission(this.permission)) {
            Lang.INSUFFICIENT_PERMISSIONS.send(this.sender);
            return;
        }

        this.playerName = sender.getName();
        Lang message = Lang.INFO_USERS_CURRENT_BREAKS;

        if(args.length == 2) {
            if(!sender.hasPermission(this.permission + ".others")) {
                Lang.INSUFFICIENT_PERMISSIONS.send(sender);
                return;
            }
            this.playerName = args[1];
            message = Lang.INFO_ADMINS_PLAYER_BREAKS;
        }

        message.send(this.sender, new HashMap<>() {{
            this.put("%player%", playerName);
            this.put("%lbBreaks%", score.getScoreMap(playerName) + "");
        }});
    }

}
