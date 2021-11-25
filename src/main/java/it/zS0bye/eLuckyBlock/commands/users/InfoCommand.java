package it.zS0bye.eLuckyBlock.commands.users;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.database.SQLLuckyBlock;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.utils.FileUtils;
import it.zS0bye.eLuckyBlock.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoCommand extends BaseCommand {

    private String[] args;
    private final CommandSender sender;
    private String type;
    private eLuckyBlock plugin;
    private SQLLuckyBlock sql;

    public InfoCommand(final String[] args, final CommandSender sender, final String type, final eLuckyBlock plugin) {
        this.args = args;
        this.sender = sender;
        this.type = type;
        this.plugin = plugin;
        this.sql = plugin.getSqlLuckyBlock();
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public InfoCommand(final List<String> tab, final CommandSender sender) {
        this.sender = sender;
        tab.add(usersTab());
    }

    public InfoCommand(final List<String> tab, final String[] args, final CommandSender sender) {
        this.sender = sender;
        if(args[0].equalsIgnoreCase(getName()))
            Bukkit.getOnlinePlayers().forEach(players -> {
                tab.add(adminsTab(players.getName()));
            });
    }

    @Override
    protected String getName() {
        return "info";
    }

    @Override
    protected void execute() {
        if(type.equals("users"))
            users();

        if(type.equals("admins"))
            admins();
    }

    private void users() {
        if(!sender.hasPermission("luckyblock.command.info")) {
            LangUtils.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(this.plugin.getLuckyBreaks().containsKey(sender.getName())) {
            int luckyBreaks = this.plugin.getLuckyBreaks().get(sender.getName());

            String text = LangUtils.INFO_USERS_CURRENT_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(luckyBreaks));

            FileUtils.send(text, sender);
            return;
        }

        this.sql.getLuckyBreaks(sender.getName()).thenAccept(getLuckyBreaks -> {
            String text = LangUtils.INFO_USERS_CURRENT_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(getLuckyBreaks));

            FileUtils.send(text, sender);
        });


    }

    private String usersTab() {
        if(sender.hasPermission("luckyblock.command.info"))
            return getName();
        return "";
    }

    private void admins() {
        if(!sender.hasPermission("luckyblock.command.info.others")) {
            LangUtils.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(this.plugin.getLuckyBreaks().containsKey(args[1])) {

            int luckyBreaks = this.plugin.getLuckyBreaks().get(args[1]);

            String text = LangUtils.INFO_ADMINS_PLAYER_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(luckyBreaks))
                    .replace("%player%", args[1]);

            FileUtils.send(text, sender);
            return;
        }

        this.sql.hasNotLuckyBreaks(args[1]).thenAccept(check -> {
            if(check) {
                LangUtils.PLAYER_NOT_FOUND.send(sender);
                return;
            }

            this.sql.getLuckyBreaks(args[1]).thenAccept(getLuckyBreaks -> {
                String text = LangUtils.INFO_ADMINS_PLAYER_BREAKS.getCustomString()
                        .replace("%lbBreaks%", String.valueOf(getLuckyBreaks))
                        .replace("%player%", args[1]);

                FileUtils.send(text, sender);
            });
        });
    }

    private String adminsTab(final String players) {
        if(sender.hasPermission("luckyblock.command.info.others"))
            return players;
        return "";
    }
}
