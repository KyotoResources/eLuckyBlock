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

package it.zS0bye.eLuckyBlock.commands.admins;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.files.enums.Rewards;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends BaseCommand {

    private CommandSender sender;
    private ELuckyBlock plugin;

    public ReloadCommand(final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.sender = sender;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public ReloadCommand(final List<String> tab, final CommandSender sender) {
        if(sender.hasPermission("eluckyblock.command.reload"))
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "reload";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission("eluckyblock.command.reload")) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        this.plugin.reloadConfig();
        for(Config config : Config.values())
            config.reloadConfig();

        this.plugin.getLang().reload();
        this.plugin.getLucky().reload();
        this.plugin.getAnimations().reload();
        this.plugin.getRewards().reload();
        this.plugin.getFireworks().reload();
        new HooksManager(this.plugin);
        Lang.RELOAD_ADMINS_CONFIGURATIONS.send(sender);
    }

}
