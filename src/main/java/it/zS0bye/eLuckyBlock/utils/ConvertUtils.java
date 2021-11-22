package it.zS0bye.eLuckyBlock.utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class ConvertUtils {

    public static Material getMaterial(final String material) {
        return Material.getMaterial(material);
    }

    public static Effect getParticles(final String particles) {
        return Effect.valueOf(particles);
    }

    public static PotionEffectType getPotion(final String potion) {
        return PotionEffectType.getByName(potion);
    }
}
