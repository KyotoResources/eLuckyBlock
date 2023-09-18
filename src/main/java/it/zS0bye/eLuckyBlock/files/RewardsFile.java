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

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Rewards;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

@Getter
public class RewardsFile {

    private final ELuckyBlock plugin;

    private FileConfiguration config;
    private final String pattern;
    private final File file;

    public RewardsFile(ELuckyBlock plugin) {

        this.plugin = plugin;
        this.pattern = "rewards.yml";
        this.file = new File(this.plugin.getDataFolder(), this.pattern);

        this.saveConfig();
    }

    @SneakyThrows
    private void saveConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();

            InputStream is = plugin.getResource(this.pattern);
            OutputStream os = new FileOutputStream(file);

            ByteStreams.copy(is, os);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        InputStream is = plugin.getResource(this.pattern);
        if (is != null)
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(is, Charsets.UTF_8)));
        for(Rewards rewards : Rewards.values())
            rewards.reloadConfig();
        this.plugin.getRandomReward().clear();
        this.plugin.addRewards();
    }

}