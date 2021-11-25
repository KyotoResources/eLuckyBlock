package it.zS0bye.eLuckyBlock.methods;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class launchFirework implements Listener {

    private final eLuckyBlock plugin;
    private final Location location;
    private final String type;
    private final List<String> colors;

    public launchFirework(final eLuckyBlock plugin, final Location location, final String type, final List<String> colors) {
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
        fm.setPower((int).9);
        firework.setFireworkMeta(fm);
        firework.setMetadata("eLuckyBlock", new FixedMetadataValue(this.plugin, true));
    }

}
