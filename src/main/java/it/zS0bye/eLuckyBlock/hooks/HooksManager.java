package it.zS0bye.eLuckyBlock.hooks;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.enums.Hooks;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.io.File;
import java.util.UUID;

@Getter
public class HooksManager {

    private final ELuckyBlock plugin;

    public HooksManager(final ELuckyBlock plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        PlaceholderAPI();
        Hooks.WORLDGUARD.load();
        Hooks.WORLDEDIT.load();
        Hooks.TOKENENCHANT.load();
        Hooks.TOKENMANAGER.load();
        Hooks.PLOTSQUARED.load();
    }

    private void PlaceholderAPI() {
        Hooks.PLACEHOLDERAPI.load();
        if(Hooks.PLACEHOLDERAPI.isCheck())
            new HPlaceholderAPI(this.plugin).register();
    }

    public static void giveMoney(final OfflinePlayer player, final double amount) {
        if(Hooks.VAULT.isCheck())
            new HVaultAPI(player, amount).addMoney();
    }

    public static void takeMoney(final OfflinePlayer player, final double amount) {
        if(Hooks.VAULT.isCheck())
            new HVaultAPI(player, amount).takeMoney();
    }

    public static void giveTokens(final OfflinePlayer player, final double amount) {
        if(Hooks.TOKENENCHANT.isCheck())
            new HTokenEnchantAPI(player, amount).addTokens();
        if(Hooks.TOKENMANAGER.isCheck())
            new HTokenManagerAPI(player, amount).addTokens();
    }

    public static void takeTokens(final OfflinePlayer player, final double amount) {
        if(Hooks.TOKENENCHANT.isCheck())
            new HTokenEnchantAPI(player, amount).takeTokens();
        if(Hooks.TOKENMANAGER.isCheck())
            new HTokenManagerAPI(player, amount).takeTokens();
    }

    public static void loadAndCopySchem(final File file, final World world, final Location loc) {
        if(Hooks.WORLDEDIT.isCheck())
            new HWorldEditAPI(file, world, loc).loadAndCopy();
    }

    public static void pasteSchem(final File file, final World world, final Location loc) {
        if(Hooks.WORLDEDIT.isCheck())
            new HWorldEditAPI(file, world, loc).paste();
    }

    public static boolean checkPlot(final UUID uuid) {
        if(Hooks.PLOTSQUARED.isCheck())
            return new HPlotSquaredAPI(uuid).checkPlot();
        return false;
    }


}
