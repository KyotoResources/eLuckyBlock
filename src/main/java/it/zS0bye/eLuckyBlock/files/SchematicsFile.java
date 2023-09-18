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

import java.io.*;

@Getter
public class SchematicsFile {

    private final ELuckyBlock plugin;

    private String schematic;
    private String pattern;
    private final File file;
    private final File directory;

    public SchematicsFile(ELuckyBlock plugin, final String schematic) {
        this.plugin = plugin;
        this.schematic = schematic;
        this.directory = new File(this.plugin.getDataFolder(), "schematics");
        this.file = new File(directory, schematic + ".schem");

        this.createDir();
    }

    public SchematicsFile(ELuckyBlock plugin) {
        this.plugin = plugin;
        this.pattern = "schematics/example.schem";
        this.directory = new File(this.plugin.getDataFolder(), "schematics");
        this.file = new File(directory, "example.schem");

        this.loadSchematics();
    }

    @SneakyThrows
    public void loadSchematics() {
        createDir();

        if(directory.listFiles().length != 0)
           return;

        if(!file.exists()) {
            file.createNewFile();
            plugin.saveResource(pattern, true);
        }
    }

    private void createDir() {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if(!directory.exists())
            directory.mkdirs();
    }

}
