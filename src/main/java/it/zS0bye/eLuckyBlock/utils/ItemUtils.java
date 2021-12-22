package it.zS0bye.eLuckyBlock.utils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemUtils {

    private static ItemStack createItem(final ItemStack is, int amount, String name, List<String> lore) {
        is.setAmount(amount);
        ItemMeta im = is.getItemMeta();
        List<String> listlore = new ArrayList<>();

        if(im != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            for(String setlore : lore) {
                listlore.add(ChatColor.translateAlternateColorCodes('&', setlore));
            }
            im.setLore(listlore);
        }

        is.setItemMeta(im);
        return is;
    }

    private static String ExceptionMsg() {
        return "An error occurred with the item you set for the luckyblock. If by any chance you are using 1.13+ and have entered any legacy ids, please update them. If not, check the spigot wiki for compatible items based on the server version.";
    }

    public static ItemStack createItem(String material, int amount, String name, List<String> lore) {

        Optional<XMaterial> getMaterial = XMaterial.matchXMaterial(material);
        if(!getMaterial.isPresent()) {
            return null;
        }

        ItemStack is = getMaterial.orElseThrow(() -> new RuntimeException(ExceptionMsg())).parseItem();

        if(is == null) {
            return new ItemStack(Material.STONE);
        }
        return createItem(is, amount, name, lore);
    }

    public static ItemStack createItem(String material, short data, int amount, String name, List<String> lore) {

        Optional<XMaterial> getMaterial = XMaterial.matchXMaterial(material);
        if(!getMaterial.isPresent()) {
            return null;
        }

        ItemStack is = getMaterial.orElseThrow(() -> new RuntimeException(ExceptionMsg())).parseItem();
        if(is == null) {
            return new ItemStack(Material.STONE);
        }
        is.setDurability(data);
        return createItem(is, amount, name, lore);
    }

    public static ItemStack createItem(int material, int amount, String name, List<String> lore) {

        Optional<XMaterial> getMaterial = XMaterial.matchXMaterial(material, (byte) 0);

        ItemStack is = getMaterial.orElseThrow(() -> new RuntimeException(ExceptionMsg())).parseItem();

        if(is == null) {
            return new ItemStack(Material.STONE);
        }
        return createItem(is, amount, name, lore);
    }

    public static ItemStack createItem(int material, byte data, int amount, String name, List<String> lore) {

        Optional<XMaterial> getMaterial = XMaterial.matchXMaterial(material, data);

        ItemStack is = getMaterial.orElseThrow(() -> new RuntimeException(ExceptionMsg())).parseItem();

        if(is == null) {
            return new ItemStack(Material.STONE);
        }
        return createItem(is, amount, name, lore);
    }

//    public static ItemStack createSkull(int amount, String name, List<String> lore, String value) {
//        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
//        gameProfile.getProperties().put("textures", new Property("textures", value));
//        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, amount, (short) 3);
//        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
//        if (skullMeta != null) {
//            try {
//                Field profileField = skullMeta.getClass().getDeclaredField("profile");
//                profileField.setAccessible(true);
//                profileField.set(skullMeta, gameProfile);
//            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
//                exception.printStackTrace();
//            }
//            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
//            List<String> listlore = new ArrayList<>();
//            for (String setlore : lore) {
//                listlore.add(ChatColor.translateAlternateColorCodes('&', setlore));
//            }
//            skullMeta.setLore(listlore);
//            skull.setItemMeta(skullMeta);
//        }
//        return skull;
//    }

}
