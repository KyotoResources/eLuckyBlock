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

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.files.CItemsFile;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CItemCommand extends BaseCommand {

    private String[] args;
    private final CommandSender sender;
    private ELuckyBlock plugin;

    public CItemCommand(final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.args = args;
        this.sender = sender;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public CItemCommand(final List<String> tab, final CommandSender sender) {
        this.sender = sender;
        if(sender.hasPermission("eluckyblock.command.citem"))
            tab.add(getName());
    }

    @Override
    protected String getName() {
        return "citem";
    }

    @Override
    protected void execute() {
        if(!sender.hasPermission("eluckyblock.command.citem")) {
            Lang.INSUFFICIENT_PERMISSIONS.send(sender);
            return;
        }

        Player player = (Player) sender;
        CItemsFile file = new CItemsFile(this.plugin, args[1]);
        ItemStack item = player.getInventory().getItemInMainHand();

        if(file.getFile().exists()) {
            String update = Lang.CITEM_ADMINS_UPDATE.getCustomString()
                    .replace("%item%", item.getType().toString());

            if(item.getItemMeta() != null
                    && item.getItemMeta().hasDisplayName())
                update = Lang.CITEM_ADMINS_UPDATE.getCustomString()
                        .replace("%item%", item.getItemMeta().getDisplayName());
            file.create(player.getInventory().getItemInMainHand());
            StringUtils.send(update, player);
            return;
        }

        String create = Lang.CITEM_ADMINS_CREATE.getCustomString()
                        .replace("%item%", item.getType().toString());

        if(item.getItemMeta() != null
        && item.getItemMeta().hasDisplayName())
            create = Lang.CITEM_ADMINS_CREATE.getCustomString()
                    .replace("%item%", item.getItemMeta().getDisplayName());

        file.create(player.getInventory().getItemInMainHand());
        StringUtils.send(create, player);
    }
}
