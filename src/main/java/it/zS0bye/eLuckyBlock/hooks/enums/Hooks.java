package it.zS0bye.eLuckyBlock.hooks.enums;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import lombok.Getter;
import org.bukkit.Bukkit;

public enum Hooks {
    PLACEHOLDERAPI("PlaceholderAPI"),
    WORLDGUARD("WorldGuard"),
    WORLDEDIT("WorldEdit"),
    VAULT("Vault"),
    TOKENENCHANT("TokenEnchant"),
    TOKENMANAGER("TokenManager"),
    ULTRAPRISONCORE("UltraPrisonCore"),
    PLOTSQUARED("PlotSquared");

    private final ELuckyBlock plugin;
    private final String path;

    @Getter
    private boolean check;

    Hooks(final String path) {
        this.plugin = ELuckyBlock.getInstance();
        this.path = path;
    }

    public void load() {
        if(!Config.valueOf("HOOKS_" + path.toUpperCase()).getBoolean()) {
            check = false;
            return;
        }

        if (Bukkit.getPluginManager().getPlugin(path) != null) {
            check = true;
            return;
        }

        this.plugin.getLogger().severe("Could not find " + path + " This plugin is required.");
        Bukkit.getPluginManager().disablePlugin(this.plugin);
        check = false;
    }
}
