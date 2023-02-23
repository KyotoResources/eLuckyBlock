package it.zS0bye.eLuckyBlock.commands.subcmds;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoSubCMD extends BaseCommand {

    private final String command;
    private final String permission;
    private String[] args;
    private final CommandSender sender;
    private String type;
    private ELuckyBlock plugin;
    private ScoreTable score;

    public InfoSubCMD(final String command, final String[] args, final CommandSender sender, final String type, final ELuckyBlock plugin) {
        this.command = command;
        this.permission = this.command + ".command." + this.getName();
        this.args = args;
        this.sender = sender;
        this.type = type;
        this.plugin = plugin;
        this.score = plugin.getScoreTable();
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
        if(type.equals("users")) users();

        if(type.equals("admins")) admins();
    }

    private void users() {
        if(!sender.hasPermission(this.permission)) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(this.plugin.getLuckyScore().containsKey(sender.getName())) {
            int luckyBreaks = this.plugin.getLuckyScore().get(sender.getName());

            String text = Lang.INFO_USERS_CURRENT_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(luckyBreaks));

            StringUtils.send(sender, text);
            return;
        }

        this.score.getScore(sender.getName()).thenAccept(score -> {
            String text = Lang.INFO_USERS_CURRENT_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(score));

            StringUtils.send(sender, text);
        });


    }

    private void admins() {
        if(!sender.hasPermission(this.permission + ".others")) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(this.plugin.getLuckyScore().containsKey(args[1])) {

            int luckyBreaks = this.plugin.getLuckyScore().get(args[1]);

            String text = Lang.INFO_ADMINS_PLAYER_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(luckyBreaks))
                    .replace("%player%", args[1]);

            StringUtils.send(sender, text);
            return;
        }

        this.score.hasNotScore(args[1]).thenAccept(check -> {
            if(check) {
                Lang.PLAYER_NOT_FOUND.send(sender);
                return;
            }

            this.score.getScore(args[1]).thenAccept(score -> {
                String text = Lang.INFO_ADMINS_PLAYER_BREAKS.getCustomString()
                        .replace("%lbBreaks%", String.valueOf(score))
                        .replace("%player%", args[1]);

                StringUtils.send(sender, text);
            });
        });
    }

}
