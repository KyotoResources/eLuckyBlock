package it.zS0bye.eLuckyBlock.settings;

import com.cryptomorin.xseries.XMaterial;
import it.zS0bye.eLuckyBlock.settings.enums.SettingType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Settings implements ISettings {

    private final static Map<String, Boolean> instant_break = new HashMap<>();
    private final static Map<String, Boolean> unique_check = new HashMap<>();
    private final static Map<String, Boolean> permission_enabled = new HashMap<>();
    private final static Map<String, Boolean> toggle_lb = new HashMap<>();
    private final static Map<String, String> permission = new HashMap<>();
    private final static Map<String, ItemStack> material = new HashMap<>();

    private final String luckyblock;
    private final SettingType type;
    private final Player player;

    public Settings(final String luckyblock, final Player player, final SettingType type) {
        this.luckyblock = luckyblock;
        this.player = player;
        this.type = type;
    }

    @Override
    public void cancel() {
        instant_break.remove(this.getId());
        unique_check.remove(this.getId());
        permission_enabled.remove(this.getId());
        toggle_lb.remove(this.getId());
        permission.remove(this.getId());
        material.remove(this.getId());
    }

    @Override
    public String getId() {
        return this.luckyblock + ";" + player.getName() + ";" + this.type;
    }

    @Override
    public void setInstantBreak(final boolean value) {
        instant_break.put(this.getId(), value);
    }

    @Override
    public boolean hasInstantBreak() {
        if(!instant_break.containsKey(this.getId())) return false;
        return instant_break.get(this.getId());
    }

    @Override
    public void setUniqueCheck(final boolean value) {
        unique_check.put(this.getId(), value);
    }

    @Override
    public boolean hasUniqueCheck() {
        if(!unique_check.containsKey(this.getId())) return false;
        return unique_check.get(this.getId());
    }

    @Override
    public void setPermissionEnabled(final boolean value) {
        permission_enabled.put(this.getId(), value);
    }

    @Override
    public boolean hasPermissionEnabled() {
        if(!permission_enabled.containsKey(this.getId())) return false;
        return permission_enabled.get(this.getId());
    }

    @Override
    public void setToggleLB(final boolean value) {
        toggle_lb.put(this.getId(), value);
    }

    @Override
    public boolean hasToggleLB() {
        if(!toggle_lb.containsKey(this.getId())) return false;
        return toggle_lb.get(this.getId());
    }

    @Override
    public void setPermission(final String value) {
        permission.put(this.getId(), value);
    }

    @Override
    public String getPermission() {
        if(!permission.containsKey(this.getId())) return "luckyblock." + this.luckyblock;
        return permission.get(this.getId());
    }

    @Override
    public void setMaterial(final ItemStack value) {
        material.put(this.getId(), value);
    }

    @Override
    public ItemStack getMaterial() {
        if(!material.containsKey(this.getId())) return XMaterial.SPONGE.parseItem();
        return material.get(this.getId());
    }

}
