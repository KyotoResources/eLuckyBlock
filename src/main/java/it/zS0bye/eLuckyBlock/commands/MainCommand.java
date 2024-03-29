/*
 * The LuckyBlock Plugin you needed! - https://github.com/KyotoResources/eLuckyBlock
 * Copyright (c) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zS0bye.eLuckyBlock.commands;

import it.zS0bye.eLuckyBlock.commands.admins.CItemCommand;
import it.zS0bye.eLuckyBlock.commands.admins.GiveCommand;
import it.zS0bye.eLuckyBlock.commands.admins.ReloadCommand;
import it.zS0bye.eLuckyBlock.commands.users.AboutCommand;
import it.zS0bye.eLuckyBlock.commands.users.HelpCommand;
import it.zS0bye.eLuckyBlock.commands.users.InfoCommand;
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

    public MainCommand(final ELuckyBlock plugin) {
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

        if(!sender.hasPermission("eluckyblock.command")) {
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
            new CItemCommand(args, sender, this.plugin);

            if(checkArgs(args[0], "info")
                    && checkArgs(args[0], "citem"))
                new HelpCommand(sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 3) {
            new GiveCommand(args, sender, this.plugin);

            if(checkArgs(args[0], "give"))
                new HelpCommand(sender, commandLabel, this.plugin);

            return true;
        }

        if(args.length == 4) {
            new GiveCommand(args, sender, this.plugin);

            if(checkArgs(args[0], "give"))
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

        if(!sender.hasPermission("eluckyblock.command")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            new HelpCommand(commands);
            new AboutCommand(commands);
            new ReloadCommand(commands, sender);
            new InfoCommand(commands, sender);
            new GiveCommand(commands, sender);
            new CItemCommand(commands, sender);
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        if(args.length == 2) {
            new InfoCommand(commands, args, sender);
            new GiveCommand(commands, args, sender, this.plugin);
            StringUtil.copyPartialMatches(args[1], commands, completions);
        }

        if(args.length == 3) {
            new GiveCommand(commands, args, sender, this.plugin);
            StringUtil.copyPartialMatches(args[2], commands, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
