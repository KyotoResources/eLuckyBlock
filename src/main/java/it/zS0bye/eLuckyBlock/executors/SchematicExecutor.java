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

package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.SchematicsFile;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SchematicExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final Location location;


    public SchematicExecutor(final String execute, final Player player, final Location location) {
        this.plugin = ELuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.location = location;
        if (this.execute.startsWith(getType()))
            if (!VersionChecker.getV1_8()
                    && !VersionChecker.getV1_9()
                    && !VersionChecker.getV1_10()
                    && !VersionChecker.getV1_11()
                    && !VersionChecker.getV1_12()) {
                apply();
            }else {
                this.plugin.getLogger().severe("The \"[SCHEMATIC]\" executor only works with WorldEdit 7+!");
            }
    }

    @Override
    protected String getType() {
        return "[SCHEMATIC] ";
    }

    @Override
    protected void apply() {

        String schematic = StringUtils.getPapi(this.player, execute
                .replace(getType(), ""));

        SchematicsFile schemFile = new SchematicsFile(this.plugin, schematic);

        if(schemFile.getFile() == null) {
            return;
        }

        HooksManager.loadAndCopySchem(schemFile.getFile(), this.player.getWorld(), this.location);
        HooksManager.pasteSchem(schemFile.getFile(), this.player.getWorld(), this.location);
    }
}
