package it.zS0bye.eLuckyBlock.commands;

import it.zS0bye.eLuckyBlock.commands.subcmds.*;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
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

    private final ELuckyBlock plugin;
    private final String command;

    public MainCommand(final ELuckyBlock plugin) {
        this.plugin = plugin;
        this.command = "eluckyblock";
    }

    private boolean checkArgs(final String args, final String check) {
        return !args.equalsIgnoreCase(check);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandLabel, String[] args) {

        if(!cmd.getName().equalsIgnoreCase(this.command)) return true;

        if(!sender.hasPermission(this.command + ".command")) {
            new AboutSubCMD(sender, this.plugin);
            return true;
        }

        if(args.length == 0) {
            new HelpSubCMD(sender, commandLabel, this.plugin);
            return true;
        }

        if(args.length == 1) {
            new HelpSubCMD(args, sender, commandLabel, this.plugin);
            new AboutSubCMD(args, sender, this.plugin);
            new ReloadSubCMD(this.command, args, sender, this.plugin);
            new InfoSubCMD(this.command, args, sender, "users", this.plugin);

            if(checkArgs(args[0], "help")
            && checkArgs(args[0], "about")
            && checkArgs(args[0], "reload")
            && checkArgs(args[0], "info"))
                new HelpSubCMD(sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 2) {
            new InfoSubCMD(this.command, args, sender, "admins", this.plugin);
            new CItemSubCMD(this.command, args, sender, this.plugin);
            new CreateSubCMD(this.command, args, sender, this.plugin);

            if(checkArgs(args[0], "info")
                    && checkArgs(args[0], "citem")
                    && checkArgs(args[0], "create"))
                new HelpSubCMD(sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 3) {
            new GiveSubCMD(this.command, args, sender, this.plugin);

            if(checkArgs(args[0], "give"))
                new HelpSubCMD(sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 4) {
            new GiveSubCMD(this.command, args, sender, this.plugin);

            if(checkArgs(args[0], "give"))
                new HelpSubCMD(sender, commandLabel, this.plugin);

            return true;
        }

        new HelpSubCMD(sender, commandLabel, this.plugin);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if(!cmd.getName().equalsIgnoreCase(this.command)) return new ArrayList<>();

        if(!sender.hasPermission(this.command + ".command")) return new ArrayList<>();

        if (args.length == 1) {
            new HelpSubCMD(commands);
            new AboutSubCMD(commands);
            new ReloadSubCMD(this.command, commands, sender);
            new InfoSubCMD(this.command, commands, sender);
            new GiveSubCMD(this.command, commands, sender);
            new CItemSubCMD(this.command, commands, sender);
            new CreateSubCMD(this.command, commands, sender);
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        if(args.length == 2) {
            new InfoSubCMD(this.command, commands, args, sender);
            new GiveSubCMD(this.command, commands, args, sender, this.plugin);
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }

        if(args.length == 3) {
            new GiveSubCMD(this.command, commands, args, sender, this.plugin);
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
