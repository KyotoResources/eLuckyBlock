package it.zS0bye.eLuckyBlock.commands;

import it.zS0bye.eLuckyBlock.commands.admins.ReloadCommand;
import it.zS0bye.eLuckyBlock.commands.users.AboutCommand;
import it.zS0bye.eLuckyBlock.commands.users.HelpCommand;
import it.zS0bye.eLuckyBlock.commands.users.InfoCommand;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final eLuckyBlock plugin;

    public MainCommand(final eLuckyBlock plugin) {
        this.plugin = plugin;
    }

    private boolean checkArgs(final String args, final String check) {
        return !args.equalsIgnoreCase(check);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandLabel, String[] args) {

        if(!cmd.getName().equalsIgnoreCase("eluckyblock")) {
            return true;
        }

        if(!sender.hasPermission("luckyblock.command")) {
            new AboutCommand(sender, this.plugin);
            return true;
        }

        if(args.length == 0) {
            new HelpCommand(sender, commandLabel, this.plugin);
            return true;
        }

        if(args.length == 1) {
            new HelpCommand(args, sender, commandLabel, this.plugin);
            new AboutCommand(args, sender, this.plugin);
            new ReloadCommand(args, sender, this.plugin);
            new InfoCommand(args, sender, "users", this.plugin);

            if(checkArgs(args[0], "help")
            && checkArgs(args[0], "about")
            && checkArgs(args[0], "reload")
            && checkArgs(args[0], "info"))
                new HelpCommand(sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 2) {
            new InfoCommand(args, sender, "admins", this.plugin);

            if(checkArgs(args[0], "info"))
                new HelpCommand(sender, commandLabel, this.plugin);

            return true;
        }

        new HelpCommand(sender, commandLabel, this.plugin);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if(!cmd.getName().equalsIgnoreCase("eluckyblock")) {
            return new ArrayList<>();
        }

        if(!sender.hasPermission("luckyblock.command")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            new HelpCommand(commands);
            new AboutCommand(commands);
            new ReloadCommand(commands, sender);
            new InfoCommand(commands, sender);
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        if(args.length == 2) {
            new InfoCommand(commands, args, sender);
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
