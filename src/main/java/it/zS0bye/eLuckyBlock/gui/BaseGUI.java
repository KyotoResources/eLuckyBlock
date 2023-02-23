package it.zS0bye.eLuckyBlock.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.builder.item.SkullBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import it.zS0bye.eLuckyBlock.files.enums.GUI;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class BaseGUI {

    abstract protected void setPlaceholders();
    abstract protected void updateItems();
    abstract protected void setItems();
    abstract protected void setEvents();

    private final Map<String, GuiItem> items;
    private final Player player;

    @Getter
    private final String guiName;

    private String material;
    private String displayName;
    private List<String> lore;

    public BaseGUI(final String guiName, final Map<String, GuiItem> items, final Player player) {
        this.guiName = guiName;
        this.items = items;
        this.player = player;
    }

    protected GuiItem getItem(final String name) {
        if(this.items.containsKey(name))
            return this.items.get(name);
        return ItemBuilder.from(Material.AIR).asGuiItem();
    }

    protected String getPath(final String name) {
        return this.guiName + GUI.ITEMS.getPath() + "." + name;
    }

    protected int getRows() {
        return GUI.CUSTOM.getInt(this.guiName + GUI.ROWS.getPath());
    }

    protected String getTitle() {
        return GUI.CUSTOM.getString(this.guiName + GUI.TITLE.getPath());
    }

    protected Set<String> getItemsName() {
        return GUI.CUSTOM.getConfigurationSection(this.guiName + GUI.ITEMS.getPath());
    }

    protected boolean hasFillerEnabled() {
        return GUI.CUSTOM.getBoolean(this.guiName + GUI.FILL_INVENTORY_ENABLED.getPath());
    }

    protected String getFillerItem() {
        return GUI.CUSTOM.getString(this.guiName + GUI.FILL_INVENTORY_ITEM.getPath());
    }

    protected int getSlot(final String name) {
        return GUI.CUSTOM.getInt(this.getPath(name) + GUI.ITEM_SLOT.getPath());
    }

    protected List<Integer> getSlots(final String name) {
        final String path = this.getPath(name) + GUI.ITEM_SLOTS.getPath();
        if(!GUI.CUSTOM.contains(path)) return new ArrayList<>();
        return GUI.CUSTOM.getIntegerList(path);
    }

    protected void updateItem(final Gui gui, final String name, final int slot) {
        this.updateItems();
        gui.updateItem(slot, this.getItem(name));
    }

    protected void updateItem(final Gui gui, final String name) {
        this.updateItem(gui, name, this.getSlot(name));
    }

    protected final void initItem(final String name, final GuiItem item) {
        this.items.put(name, item);
    }

    @SafeVarargs
    protected final void initItem(final String name, final Map<String, String>... placeholders) {
        this.material = GUI.CUSTOM.getString(this.getPath(name) + GUI.ITEM_MATERIAL.getPath());
        this.displayName = GUI.CUSTOM.getString(this.getPath(name) + GUI.ITEM_DISPLAY_NAME.getPath());
        this.lore = GUI.CUSTOM.getStringList(this.getPath(name) + GUI.ITEM_LORE.getPath());
        this.items.put(name, this.guiItem(this.material, this.getPath(name), placeholders));
    }

    @SafeVarargs
    protected final void initItem(final String name, final ItemStack material, final Map<String, String>... placeholders) {
        this.displayName = GUI.CUSTOM.getString(this.getPath(name) + GUI.ITEM_DISPLAY_NAME.getPath());
        this.lore = GUI.CUSTOM.getStringList(this.getPath(name) + GUI.ITEM_LORE.getPath());
        this.items.put(name, this.guiItem(material, this.getPath(name), placeholders));
    }

    @SafeVarargs
    protected final void initItem(final String name, final ItemStack item, final String displayName, final List<String> lore, final Map<String, List<String>> placeholderLore, final Map<String, String>... placeholders) {
        this.items.put(name, this.guiItem(this.getPath(name), item, displayName, lore, placeholderLore, placeholders));
    }

    protected void setItem(final PaginatedGui gui, final String name) {
        if(this.getSlots(name).isEmpty()) {
            gui.setItem(this.getSlot(name), this.getItem(name));
            return;
        }
        
        this.getSlots(name).forEach(slot -> gui.setItem(slot, this.getItem(name)));
    }

    protected void setItem(final Gui gui, final String name) {
        if(this.getSlots(name).isEmpty()) {
            gui.setItem(this.getSlot(name), this.getItem(name));
            return;
        }

        this.getSlots(name).forEach(slot -> gui.setItem(slot, this.getItem(name)));
    }

    protected void setItem(final Gui gui, final String name, final int slot) {
        gui.setItem(slot, this.getItem(name));
    }

    @SafeVarargs
    private final GuiItem guiItem(final String material, final String path,  final Map<String, String>... placeholders) {
        if(material.startsWith("base64-")) return skullTexture(path, placeholders).asGuiItem();
        if(material.startsWith("player-")) return skullOwner(path, placeholders).asGuiItem();
        return item(path, material, placeholders).asGuiItem();
    }

    @SafeVarargs
    private final GuiItem guiItem(final ItemStack material, final String path,  final Map<String, String>... placeholders) {
        return item(path, material, placeholders).asGuiItem();
    }

    @SafeVarargs
    private final GuiItem guiItem(final String path, final ItemStack item, final String displayName, final List<String> lore, final Map<String, List<String>> placeholderLore, final Map<String, String>... placeholders) {
        return this.item(path, item, displayName, lore, placeholderLore, placeholders).asGuiItem();
    }

    private int getAmount(final String path) {
        final int amount = GUI.CUSTOM.getInt(path + GUI.ITEM_AMOUNT.getPath());
        return amount == 0 ? 1 : amount;
    }

    @SafeVarargs
    private final TextComponent getDisplayName(String displayName, final Map<String, String>... placeholders) {
        for (Map<String, String> placeholder : placeholders) {
            for (String key : placeholder.keySet()) displayName = displayName.replace(key, placeholder.get(key));
        }
        return Component.text(displayName);
    }

    @SafeVarargs
    private final List<Component> getLore(final List<String> lore, final Map<String, String>... placeholders) {
        final List<Component> setLore = new ArrayList<>();
        if(lore.isEmpty()) return setLore;
        for(String getLore : lore) {
            for(Map<String, String> placeholder : placeholders) {
                for (String key : placeholder.keySet())
                    getLore = getLore.replace(key, placeholder.get(key));
            }
            setLore.add(Component.text(getLore));
        }
        return setLore;
    }

    @SafeVarargs
    private final List<Component> getListLore(final List<String> lore, final Map<String, List<String>> placeholderLore, final Map<String, String>... placeholders) {
        final List<String> setLore = new ArrayList<>();
        final List<Component> setComponent = new ArrayList<>();
        if (lore.isEmpty()) return setComponent;

        for (String getLore : lore) {

            for (Map<String, String> placeholder : placeholders) {
                for (String key : placeholder.keySet()) {
                    getLore = getLore.replace(key, placeholder.get(key));
                }
                setLore.add(getLore);
            }

            for (String key : placeholderLore.keySet()) {
                if (!getLore.contains(key)) continue;
                setLore.addAll(placeholderLore.get(key));
                setLore.removeIf(s -> s.contains(key));
            }
        }

        for(final String getLore : setLore) setComponent.add(Component.text(getLore));
        return setComponent;
    }

    private ItemStack getXItemLatest(final String material, final int amount) {
        final Optional<XMaterial> oMaterial = XMaterial.matchXMaterial(material);
        if(!oMaterial.isPresent()) return new ItemStack(Material.AIR);
        final ItemStack item = oMaterial.get().parseItem();
        if(item == null) return new ItemStack(Material.AIR);
        item.setAmount(amount);
        return item;
    }

    @SuppressWarnings("deprecation")
    private ItemStack getXItemLegacy(final String material, final byte data, final int amount) {
        final Optional<XMaterial> oMaterial = XMaterial.matchXMaterial(material);
        if(!oMaterial.isPresent()) return new ItemStack(Material.AIR);
        final ItemStack item = oMaterial.get().parseItem();
        if(item == null) return new ItemStack(Material.AIR);
        item.setAmount(amount);
        item.setDurability(data);
        return item;
    }

    @SuppressWarnings("deprecation")
    private ItemStack getXItemLegacy(final int id, final byte data, final int amount) {
        final Optional<XMaterial> oMaterial = XMaterial.matchXMaterial(id, data);
        if(!oMaterial.isPresent()) return new ItemStack(Material.AIR);
        final ItemStack item = oMaterial.get().parseItem();
        if(item == null) return new ItemStack(Material.AIR);
        item.setAmount(amount);
        return item;
    }

    protected ItemStack getItemStack(final String material, final int amount) {
        if(!material.contains(":")) {
            if(!NumberUtils.isDigits(material)) return this.getXItemLatest(material, amount);
            return this.getXItemLegacy(Integer.parseInt(material), (byte) 0, amount);
        }
        final String idString = material.split(":")[0];
        final byte data = Byte.parseByte(material.split(":")[1]);
        if(!NumberUtils.isDigits(idString)) return this.getXItemLegacy(idString, data, amount);
        final int id = Integer.parseInt(idString);
        return this.getXItemLegacy(id, data, amount);
    }

    private String getTexture(final String path) {
        final String material = GUI.CUSTOM.getString(path + GUI.ITEM_MATERIAL.getPath());
        if(!material.startsWith("base64-")) return "";
        return material.split("base64-")[1];
    }

    private OfflinePlayer getOwner(final String path) {
        final String material = GUI.CUSTOM.getString(path + GUI.ITEM_MATERIAL.getPath());
        if(!material.startsWith("player-")) return null;
        return Bukkit.getOfflinePlayer(material.split("player-")[1]
                .replace("%player%", this.player.getName()));
    }

    @SafeVarargs
    private final ItemBuilder item(final String path, final String material, final Map<String, String>... placeholders) {
        return ItemBuilder.from(this.getItemStack(material, this.getAmount(path)))
                .name(this.getDisplayName(this.displayName, placeholders))
                .lore(this.getLore(this.lore, placeholders))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .setNbt("eLuckyBlock-GUI", path);
    }

    @SafeVarargs
    private final ItemBuilder item(final String path, final ItemStack material, final Map<String, String>... placeholders) {
        return ItemBuilder.from(material)
                .name(this.getDisplayName(this.displayName, placeholders))
                .lore(this.getLore(this.lore, placeholders))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .setNbt("eLuckyBlock-GUI", path);
    }

    @SafeVarargs
    private final ItemBuilder item(final String path, final ItemStack item, final String displayName, final List<String> lore, final Map<String, List<String>> placeholderLore, final Map<String, String>... placeholders) {
        return ItemBuilder.from(item)
                .amount(1)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .name(this.getDisplayName(displayName, placeholders))
                .lore(this.getListLore(lore, placeholderLore, placeholders))
                .setNbt("eLuckyBlock-GUI", path);
    }

    @SafeVarargs
    private final SkullBuilder skullTexture(final String path, final Map<String, String>... placeholders) {
        return ItemBuilder.skull()
                .texture(this.getTexture(path))
                .amount(this.getAmount(path))
                .name(this.getDisplayName(this.displayName, placeholders))
                .lore(this.getLore(this.lore, placeholders))
                .setNbt("eLuckyBlock-GUI", path);
    }

    @SafeVarargs
    private final SkullBuilder skullOwner(final String path, final Map<String, String>... placeholders) {
        return ItemBuilder.skull()
                .owner(this.getOwner(path))
                .amount(this.getAmount(path))
                .name(this.getDisplayName(this.displayName, placeholders))
                .lore(this.getLore(this.lore, placeholders))
                .setNbt("eLuckyBlock-GUI", path);
    }

}
