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

package it.zS0bye.eLuckyBlock.objects;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class LaunchFirework implements Listener {

    private final ELuckyBlock plugin;
    private final Location location;
    private final String type;
    private final List<String> colors;

    public LaunchFirework(final ELuckyBlock plugin, final Location location, final String type, final List<String> colors) {
        this.plugin = plugin;
        this.location = location;
        this.type = type;
        this.colors = colors;
        send();
    }

    public void send() {
        Firework firework = (Firework) this.location.getWorld().spawnEntity(this.location, EntityType.FIREWORK);
        FireworkMeta fm = firework.getFireworkMeta();
        FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withTrail();
        builder.withFlicker();
        this.colors.forEach(colors -> {
            int red = Integer.parseInt(colors.split(",")[0]);
            int green = Integer.parseInt(colors.split(",")[1]);
            int blue = Integer.parseInt(colors.split(",")[2]);

            builder.withColor(Color.fromBGR(blue, green, red));
        });
        builder.with(FireworkEffect.Type.valueOf(this.type));
        fm.addEffects(builder.build());
        fm.setPower(0);
        firework.setFireworkMeta(fm);
        firework.setMetadata("eLuckyBlock", new FixedMetadataValue(this.plugin, true));
    }

}
