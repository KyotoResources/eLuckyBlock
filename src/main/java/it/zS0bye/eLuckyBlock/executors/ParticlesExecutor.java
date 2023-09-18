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

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticlesExecutor extends Executors {

    private final String execute;
    private final Player player;
    private final Location location;

    public ParticlesExecutor(final String execute, final Player player, final Location location) {
        this.execute = execute;
        this.player = player;
        this.location = location;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected String getType() {
        return "[PARTICLES] ";
    }

    @Override
    protected void apply() {

        String particles = execute
                .replace(getType(), "");

        if(particles.startsWith("block-")) {
            String block = particles.split("block-")[1];
            this.player.playEffect(this.location, Effect.STEP_SOUND, ConvertUtils.getMaterial(block.toUpperCase()));
            return;
        }

        this.player.playEffect(this.location,
                ConvertUtils.getParticles(particles),
                ConvertUtils.getParticles(particles).getData());

    }
}
