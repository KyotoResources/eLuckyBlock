package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LuckyUtils extends FileUtils {

    private final FileConfiguration lucky;
    private final String material;
    private final String data;
    private final String rewards;
    private final String deny_pickup;
    private final String deny_absorb;
    private final String perms_required;
    private final String perms_required_enabled;
    private final String perms_required_chat_enabled;
    private final String perms_required_chat;
    private final String perms_required_title_enabled;
    private final String perms_required_title;
    private final String perms_required_title_subtitle;
    private final String perms_required_title_fadein;
    private final String perms_required_title_stay;
    private final String perms_required_title_fadeout;
    private final String perms_required_action_enabled;
    private final String perms_required_action;
    private final String particles;
    private final String blocked_worlds_type;
    private final String blocked_worlds;
    private final String blocked_regions_type;
    private final String blocked_regions;

    public LuckyUtils(final String luckyblock) {
        this.lucky = eLuckyBlock.getInstance().getLucky().getConfig();
        this.material = luckyblock + ".material";
        this.data = luckyblock + ".data";
        this.rewards = luckyblock + ".rewards";
        this.deny_pickup = luckyblock + ".prevent.deny_pickup";
        this.deny_absorb = luckyblock + ".prevent.deny_absorb";
        this.perms_required = luckyblock + ".permission_required.permission";
        this.perms_required_enabled = luckyblock + ".permission_required.enabled";
        this.perms_required_chat_enabled= luckyblock + ".permission_required.modules.chat";
        this.perms_required_chat = luckyblock + ".permission_required.chat";
        this.perms_required_title_enabled = luckyblock + ".permission_required.modules.title";
        this.perms_required_title = luckyblock + ".permission_required.title.title";
        this.perms_required_title_subtitle = luckyblock + ".permission_required.title.subtitle";
        this.perms_required_title_fadein = luckyblock + ".permission_required.title.fade-in";
        this.perms_required_title_stay = luckyblock + ".permission_required.title.stay";
        this.perms_required_title_fadeout = luckyblock  + ".permission_required.title.fade-out";
        this.perms_required_action_enabled = luckyblock + ".permission_required.modules.action";
        this.perms_required_action = luckyblock + ".permission_required.action";
        this.particles = luckyblock + ".particles";
        this.blocked_worlds_type = luckyblock + ".prevent.blocked_worlds.type";
        this.blocked_worlds = luckyblock + ".prevent.blocked_worlds.worlds";
        this.blocked_regions_type = luckyblock + ".prevent.blocked_regions.type";
        this.blocked_regions = luckyblock + ".prevent.blocked_regions.regions";
    }

    protected String getPrefix() {
        return ConfigUtils.SETTINGS_PREFIX.getString();
    }

    public String getString(final String path) {
        return ColorUtils.getColor(this.lucky.getString(path));
    }

    public boolean contains(final String path) {
        return this.lucky.contains(path);
    }

    public boolean equals(final String path, final String equals) {
        return this.lucky.getString(path).equalsIgnoreCase(equals);
    }

    public List<String> getStringList(final String path) {
        List<String> lore = new ArrayList<>();
        for(String setLore : this.lucky.getStringList(path))
            lore.add(ColorUtils.getColor(setLore));
        return lore;
    }

    public boolean getBoolean(final String path) {
        return this.lucky.getBoolean(path);
    }

    public int getInt(final String path) {
        return this.lucky.getInt(path);
    }
}
