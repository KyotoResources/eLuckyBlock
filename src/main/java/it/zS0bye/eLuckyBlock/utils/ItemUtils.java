package it.zS0bye.eLuckyBlock.utils;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import it.zS0bye.eLuckyBlock.checker.VersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

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

    public static String ExceptionMsg() {
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

    public static ItemStack createSkull(String player, int amount, String name, List<String> lore) {

        if(VersionChecker.getV1_8()
                || VersionChecker.getV1_9()
                || VersionChecker.getV1_10()
                || VersionChecker.getV1_11()
                || VersionChecker.getV1_12()) {
            return legacySkull(player, amount, name, lore);
        }else {
            return latestSkull(Bukkit.getOfflinePlayer(player), amount, name, lore);
        }

    }

    public static ItemStack createSkull(int amount, String name, List<String> lore, String value) {

        if(VersionChecker.getV1_8()
                || VersionChecker.getV1_9()
                || VersionChecker.getV1_10()
                || VersionChecker.getV1_11()
                || VersionChecker.getV1_12()) {
            return legacySkull(amount, name, lore, value);
        }else {
            return latestSkull(amount, name, lore, value);
        }

    }

    private static ItemStack legacySkull(String player, int amount, String name, List<String> lore) {
        ItemStack skull = new ItemStack(Material.valueOf("SKULL"), amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwner(player);
            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> listlore = new ArrayList<>();
            for (String setlore : lore) {
                listlore.add(ChatColor.translateAlternateColorCodes('&', setlore));
            }
            skullMeta.setLore(listlore);
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }

    private static ItemStack legacySkull(int amount, String name, List<String> lore, String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));
        ItemStack skull = new ItemStack(Material.valueOf("SKULL"), amount, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> listlore = new ArrayList<>();
            for (String setlore : lore) {
                listlore.add(ChatColor.translateAlternateColorCodes('&', setlore));
            }
            skullMeta.setLore(listlore);
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }

    private static ItemStack latestSkull(OfflinePlayer player, int amount, String name, List<String> lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> listlore = new ArrayList<>();
            for (String setlore : lore) {
                listlore.add(ChatColor.translateAlternateColorCodes('&', setlore));
            }
            skullMeta.setLore(listlore);
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }

    private static ItemStack latestSkull(int amount, String name, List<String> lore, String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> listlore = new ArrayList<>();
            for (String setlore : lore) {
                listlore.add(ChatColor.translateAlternateColorCodes('&', setlore));
            }
            skullMeta.setLore(listlore);
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }

    public static boolean hasCustomSkull(final Skull skull) {
        Field profileField;
        GameProfile gameProfile = null;
        try {
            profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            gameProfile = (GameProfile) profileField.get(skull);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        if (gameProfile != null) {
            return gameProfile.getProperties().containsKey("textures");
        }
        return false;
    }

    public static boolean hasCustomSkull(final ItemStack skull) {
        if (skull.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            Field profileField = null;
            GameProfile gameProfile = null;
            if (skullMeta != null) {
                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    gameProfile = (GameProfile) profileField.get(skullMeta);
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }

                if (gameProfile != null) {
                    return gameProfile.getProperties().containsKey("textures");
                }
            }
        }
        return false;
    }

    public static boolean isSkullCorrect(final Skull skull, final String value) {
        Field profileField;
        GameProfile gameProfile = null;
        try {
            profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            gameProfile = (GameProfile) profileField.get(skull);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        String text = "";
        if (gameProfile != null) {
            Collection<Property> textures = gameProfile.getProperties().get("textures");

            for (Property texture : textures) {
                text = texture.getValue();
            }
        }

        return value.equalsIgnoreCase(text);
    }

    public static boolean isSkullCorrect(final ItemStack skull, final String value) {
        if (skull.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            Field profileField;
            GameProfile gameProfile = null;
            if (skullMeta != null) {
                try {
                    profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    gameProfile = (GameProfile) profileField.get(skullMeta);
                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }

                String text = "";
                if (gameProfile != null) {
                    Collection<Property> textures = gameProfile.getProperties().get("textures");

                    for (Property texture : textures) {
                        text = texture.getValue();
                    }
                }

                return value.equalsIgnoreCase(text);
            }
        }
        return false;
    }

}
