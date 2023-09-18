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

package it.zS0bye.eLuckyBlock.commands.users;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends BaseCommand {

    private CommandSender sender;
    private String command;
    private ELuckyBlock plugin;

    public HelpCommand(final CommandSender sender, final String command, final ELuckyBlock plugin) {
        this.sender = sender;
        this.command = command;
        this.plugin = plugin;
        execute();
    }

    public HelpCommand(final String[] args, final CommandSender sender, final String command, final ELuckyBlock plugin) {
        this.sender = sender;
        this.command = command;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public HelpCommand(final List<String> tab) {
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "help";
    }

    @Override
    protected void execute() {

        Lang.HELP_USERS_TEXTS.getStringList().forEach(text -> sender.sendMessage(text
                .replace("%command%", command)
                .replace("%version%", version())
                .replace("%author%", author())));

    }
    private String version() {
        return "ₑLuckyBlock v" + this.plugin.getDescription().getVersion();
    }

    private String author() {
        return "Developed by zS0bye";
    }
}
