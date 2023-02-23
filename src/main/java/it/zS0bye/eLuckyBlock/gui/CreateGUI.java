package it.zS0bye.eLuckyBlock.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.GUI;
import it.zS0bye.eLuckyBlock.files.enums.Lang;
import it.zS0bye.eLuckyBlock.settings.ISettings;
import it.zS0bye.eLuckyBlock.settings.Settings;
import it.zS0bye.eLuckyBlock.settings.enums.SettingType;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CreateGUI extends BaseGUI {

    private final @NotNull Gui gui;

    private final ELuckyBlock plugin;
    private final String luckyblock;
    private final Map<String, GuiItem> items = new HashMap<>();
    private final Player player;
    private final ISettings settings;

    private final String material = "material";
    private final String instant_break = "instant_break";
    private final String unique_check = "unique_check";
    private final String permission_check = "permission_check";
    private final String rewards = "rewards";
    private final String fireworks = "fireworks";
    private final String animations = "animations";
    private final String schematics = "schematics";
    private final String prevents = "prevents";
    private final String toggle_lb = "toggle_lb";
    private final String enable_lb = "enable_lb";
    private final String disable_lb = "disable_lb";
    private final String create = "create";
    private final String cancel = "cancel";
    private final String close = "close";

    private final Map<String, String> ibPlaceholders = new HashMap<>();
    private final Map<String, String> ucPlaceholders = new HashMap<>();
    private final Map<String, String> pcPlaceholders = new HashMap<>();

    private final Map<String, String> settingsPlaceholders = new HashMap<>();
    private final Map<String, String> inputPlaceholders = new HashMap<>();

    public CreateGUI(final ELuckyBlock plugin, final String luckyblock, final Map<String, GuiItem> items, final Player player) {
        super("Create_GUI", items, player);
        this.plugin = plugin;
        this.luckyblock = luckyblock;
        this.player = player;
        this.settings = new Settings(this.luckyblock, this.player, SettingType.CREATE);
        this.gui = Gui.gui()
                .rows(this.getRows())
                .title(Component.text(this.getTitle()
                        .replace("%luckyblock%", this.luckyblock)))
                .disableAllInteractions()
                .create();
        this.setItems();
        this.setEvents();

        this.gui.open(this.player);
    }

    @Override
    protected void setPlaceholders() {

        this.ibPlaceholders.put("%status%", this.getGuiStatus(this.settings.hasInstantBreak()));
        this.ucPlaceholders.put("%status%", this.getGuiStatus(this.settings.hasUniqueCheck()));

        this.pcPlaceholders.put("%status%", this.getGuiStatus(this.settings.hasPermissionEnabled()));
        this.pcPlaceholders.put("%permission%", this.settings.getPermission());

    }

    @Override
    protected void updateItems() {
        this.setPlaceholders();

        this.getItemsName().forEach(key -> {
            if(key.equals(this.material)) {
                this.initItem(key, this.settings.getMaterial().clone());
                return;
            }
            if(key.equals(this.instant_break)) {
                this.initItem(key, this.ibPlaceholders);
                return;
            }
            if(key.equals(this.unique_check)) {
                this.initItem(key, this.ucPlaceholders);
                return;
            }
            if(key.equals(this.permission_check)) {
                this.initItem(key, this.pcPlaceholders);
                return;
            }
            this.initItem(key);
        });

        this.initItem(this.toggle_lb, this.getTLBItem());
    }

    @SuppressWarnings("all")
    @Override
    protected void setItems() {

        this.updateItems();

        this.getItemsName().forEach(key -> {
            if(key.equals(this.disable_lb) || key.equals(this.enable_lb)) return;
            this.setItem(this.gui, key);
        });

        this.setItem(this.gui, this.toggle_lb, this.getTLBSlot());

        if(!this.hasFillerEnabled()) return;
        this.gui.getFiller().fill(this.getItem(this.getFillerItem()));
    }

    @Override
    protected void setEvents() {

        this.getItem(this.close).setAction(event -> {
            if(!event.isLeftClick()) return;
            this.gui.close(this.player);
        });

        this.getItem(this.cancel).setAction(event -> {
            if(!event.isLeftClick()) return;
            this.settings.cancel();
            this.gui.close(this.player);

            Lang.SETTINGS_GUI_CANCEL_SAVES.send(this.player);
        });

        this.gui.addSlotAction(this.getSlot(this.material), event -> {
            if(!event.isLeftClick()) return;

            final ItemStack item = event.getCursor();
            if(item == null || item.getType() == Material.AIR) return;

            final ItemStack material = this.settings.getMaterial().clone();
            if(item.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) material.setItemMeta(item.getItemMeta());
            if(item.equals(material)) return;

            if(!item.getType().isBlock()) {
                Lang.SETTINGS_GUI_ERRORS_INVALID_MATERIAL.send(this.player);
                return;
            }

            this.settings.setMaterial(item.clone());
            this.updateItem(this.getGui(), this.material);

            Lang.SETTINGS_GUI_SET_MATERIAL.send(this.player);
        });

        this.gui.addSlotAction(this.getSlot(this.instant_break), event -> {
            if(!event.isLeftClick()) return;
            final String type = "Instant Break";

            this.settings.setInstantBreak(!this.settings.hasInstantBreak());
            this.updateItem(this.getGui(), this.instant_break);

            this.settingsPlaceholders.put("%type%", type);
            this.settingsPlaceholders.put("%status%", this.getSetStatus(!this.settings.hasInstantBreak()));
            Lang.SETTINGS_GUI_TOGGLE.send(this.player, this.settingsPlaceholders);
        });

        this.gui.addSlotAction(this.getSlot(this.unique_check), event -> {
            if(!event.isLeftClick()) return;
            final String type = "Unique Check";

            this.settings.setUniqueCheck(!this.settings.hasUniqueCheck());
            this.updateItem(this.getGui(), this.unique_check);

            this.settingsPlaceholders.put("%type%", type);
            this.settingsPlaceholders.put("%status%", this.getSetStatus(!this.settings.hasUniqueCheck()));
            Lang.SETTINGS_GUI_TOGGLE.send(this.player, this.settingsPlaceholders);
        });

        this.gui.addSlotAction(this.getTLBSlot(), event -> {
            if(!event.isLeftClick()) return;
            final String type = "Toggle LuckyBlock";

            this.settings.setToggleLB(!this.settings.hasToggleLB());
            this.updateItem(this.getGui(), this.toggle_lb, this.getTLBSlot());

            this.settingsPlaceholders.put("%type%", type);
            this.settingsPlaceholders.put("%status%", this.getSetStatus(!this.settings.hasToggleLB()));
            Lang.SETTINGS_GUI_TOGGLE.send(this.player, this.settingsPlaceholders);
        });

        this.gui.addSlotAction(this.getSlot(this.permission_check), event -> {
            final String type = "Permission Check";

            if(!event.isLeftClick()) {
                if(!event.isRightClick()) return;
                this.settings.setPermissionEnabled(!this.settings.hasPermissionEnabled());
                this.updateItem(this.getGui(), this.permission_check);

                this.settingsPlaceholders.put("%type%", type);
                this.settingsPlaceholders.put("%status%", this.getSetStatus(!this.settings.hasPermissionEnabled()));
                Lang.SETTINGS_GUI_TOGGLE.send(this.player, this.settingsPlaceholders);
                return;
            }

            new IAnvilGUI(this.plugin, this.settings.getId(), this.player, type, "\\w+(?:\\.+\\w+)+")
                    .setCloseEvent(close -> this.gui.open(this.player))
                    .setInputEvent(input -> {
                        this.inputPlaceholders.put("%pattern%", Lang.INPUT_GUI_PATTERNS_PERMISSION.getString());
                        if(!input.isPattern()) {
                            Lang.INPUT_GUI_ERRORS_REGEX_NOT_VALID.send(this.player, this.inputPlaceholders);
                            return;
                        }
                        this.settings.setPermission(input.getMessage().toLowerCase());
                        this.updateItem(this.getGui(), this.permission_check);

                        this.inputPlaceholders.put("%category%", type);
                        this.inputPlaceholders.put("%edit%", input.getMessage());
                        Lang.INPUT_GUI_SUCCESSFULL.send(this.player, this.inputPlaceholders);
                    });
        });

    }

    private String getGuiStatus(final boolean status) {
        return status ? GUI.CUSTOM.getString(this.getGuiName() + GUI.PLACEHOLDER_STATUS_DISABLE.getPath())
                : GUI.CUSTOM.getString(this.getGuiName() + GUI.PLACEHOLDER_STATUS_ENABLE.getPath());
    }

    private String getSetStatus(final boolean status) {
        return status ? Lang.SETTINGS_GUI_STATUS_DISABLED.getString() : Lang.SETTINGS_GUI_STATUS_ENABLED.getString();
    }

    private GuiItem getTLBItem() {
        return !this.settings.hasToggleLB() ? this.getItem(this.enable_lb) : this.getItem(this.disable_lb);
    }

    private int getTLBSlot() {
        return this.settings.hasToggleLB() ? this.getSlot(this.enable_lb) : this.getSlot(this.disable_lb);
    }

}
