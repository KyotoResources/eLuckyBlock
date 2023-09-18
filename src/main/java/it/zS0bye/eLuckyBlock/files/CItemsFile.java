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

package it.zS0bye.eLuckyBlock.files;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

@Getter
public class CItemsFile {

    private final ELuckyBlock plugin;
    private final File file;
    private final File directory;
    private final FileConfiguration config;

    public CItemsFile(final ELuckyBlock plugin, final String item) {
        this.plugin = plugin;
        this.directory = new File(this.plugin.getDataFolder(), "citems");
        this.file = new File(directory, item + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    @SneakyThrows
    public void create(final ItemStack item) {
        createDir();

        if(!file.exists()) {
            file.createNewFile();
            config.set("item", item);
            config.save(file);
            return;
        }

        config.set("item", item);
        config.save(file);
    }

    public ItemStack getItem() {
        return config.getItemStack("item");
    }

    private void createDir() {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if(!directory.exists())
            directory.mkdirs();
    }
}
