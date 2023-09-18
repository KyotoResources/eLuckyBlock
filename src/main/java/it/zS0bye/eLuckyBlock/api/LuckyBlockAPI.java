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

package it.zS0bye.eLuckyBlock.api;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.executors.*;
import it.zS0bye.eLuckyBlock.files.enums.Lucky;
import it.zS0bye.eLuckyBlock.utils.ItemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LuckyBlockAPI implements ILuckyBlockAPI {

    private final ELuckyBlock plugin;

    public LuckyBlockAPI() {
        this.plugin = ELuckyBlock.getInstance();
    }

    @Override
    public void forcedOpening(String luckyblock, Player player) {
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            if(plugin.getRandomReward().containsKey(Lucky.REWARDS.getString(luckyblock)))
                plugin.getRandomReward().get(Lucky.REWARDS.getString(luckyblock)).getRandomValue().getCommands().forEach(command -> sendReward(player, player.getLocation(), command));
        }, 2L);
    }

    @Override
    public void forcedOpening(String luckyblock, Player player, Location loc) {
        if(plugin.getRandomReward().containsKey(Lucky.REWARDS.getString(luckyblock)))
            plugin.getRandomReward().get(Lucky.REWARDS.getString(luckyblock)).getRandomValue().getCommands().forEach(command -> sendReward(player, loc, command));
    }

    @Override
    public ItemStack give(final String luckyblock, final int amount) {
        int data;
        String material = Lucky.MATERIAL.getString(luckyblock);
        String displayName = Lucky.UNIQUE_CHECK_NAME.getString(luckyblock);
        List<String> lore = Lucky.UNIQUE_CHECK_LORE.getStringList(luckyblock);

        if(material.startsWith("player-"))
            return ItemUtils.createSkull(material.split("player-")[1], amount, displayName, lore);

        if(material.startsWith("base64-"))
            return ItemUtils.createSkull(amount, displayName, lore, material.split("base64-")[1]);

        if(NumberUtils.isNumber(material)) {
            int id = Integer.parseInt(material);
            if(Lucky.DATA.contains(luckyblock)) {
                data = Lucky.DATA.getInt(luckyblock);
                return ItemUtils.createItem(id, (byte) data, amount, displayName, lore);
            }
            return ItemUtils.createItem(id, amount, displayName, lore);
        }

        if(Lucky.DATA.contains(luckyblock)) {
            data = Lucky.DATA.getInt(luckyblock);
            return ItemUtils.createItem(material, (short) data, amount, displayName, lore);
        }

        return ItemUtils.createItem(material, amount, displayName, lore);
    }

    private void sendReward(final Player player, final Location loc, final String execute) {
        new TitleExecutor(execute, player);
        new ActionExecutor(execute, player);
        new BossBarExecutor(execute, player);
        new BroadcastActionExecutor(execute, player);
        new BroadcastExecutor(execute, player);
        new BroadcastTitleExecutor(execute, player);
        new BroadcastBossBarExecutor(execute, player);
        new ClearEffectExecutor(execute, player);
        new ConsoleExecutor(execute, player);
        new EffectExecutor(execute, player);
        new MessageExecutor(execute, player);
        new PlayerExecutor(execute, player);
        new SoundExecutor(execute, player);
        new SudoExecutor(execute, player);
        new SpawnMobExecutor(execute, loc);
        new ItemExecutor(execute, loc);
        new GiveXPExecutor(execute, player);
        new TakeXPExecutor(execute, player);
        new FireworksExecutor(execute, player, loc);
        new ParticlesExecutor(execute, player, loc);
        new CItemExecutor(execute, loc);
        new GiveTokensExecutor(execute, player);
        new TakeTokensExecutor(execute, player);
        new GiveMoneyExecutor(execute, player);
        new TakeMoneyExecutor(execute, player);
        new SchematicExecutor(execute, player, loc);
    }

}
