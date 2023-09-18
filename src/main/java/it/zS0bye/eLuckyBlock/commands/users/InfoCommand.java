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
import it.zS0bye.eLuckyBlock.mysql.tables.ScoreTable;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InfoCommand extends BaseCommand {

    private String[] args;
    private final CommandSender sender;
    private String type;
    private ELuckyBlock plugin;
    private ScoreTable score;

    public InfoCommand(final String[] args, final CommandSender sender, final String type, final ELuckyBlock plugin) {
        this.args = args;
        this.sender = sender;
        this.type = type;
        this.plugin = plugin;
        this.score = plugin.getScoreTable();
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public InfoCommand(final List<String> tab, final CommandSender sender) {
        this.sender = sender;
        if(sender.hasPermission("eluckyblock.command.info"))
        tab.add(getName());
    }

    public InfoCommand(final List<String> tab, final String[] args, final CommandSender sender) {
        this.sender = sender;
        if(args[0].equalsIgnoreCase(getName()))
            Bukkit.getOnlinePlayers().forEach(players -> {
                if(sender.hasPermission("eluckyblock.command.info.others"))
                tab.add(players.getName());
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
        if(!sender.hasPermission("eluckyblock.command.info")) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(this.plugin.getLuckyScore().containsKey(sender.getName())) {
            int luckyBreaks = this.plugin.getLuckyScore().get(sender.getName());

            String text = Lang.INFO_USERS_CURRENT_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(luckyBreaks));

            StringUtils.send(text, sender);
            return;
        }

        this.score.getScore(sender.getName()).thenAccept(score -> {
            String text = Lang.INFO_USERS_CURRENT_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(score));

            StringUtils.send(text, sender);
        });


    }

    private void admins() {
        if(!sender.hasPermission("eluckyblock.command.info.others")) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        if(this.plugin.getLuckyScore().containsKey(args[1])) {

            int luckyBreaks = this.plugin.getLuckyScore().get(args[1]);

            String text = Lang.INFO_ADMINS_PLAYER_BREAKS.getCustomString()
                    .replace("%lbBreaks%", String.valueOf(luckyBreaks))
                    .replace("%player%", args[1]);

            StringUtils.send(text, sender);
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

                StringUtils.send(text, sender);
            });
        });
    }

}
