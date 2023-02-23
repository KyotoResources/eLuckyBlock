package it.zS0bye.eLuckyBlock.hooks;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.hooks.enums.Hooks;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

@Getter
public class HooksManager {

    private final ELuckyBlock plugin;

    public HooksManager(final ELuckyBlock plugin) {
        this.plugin = plugin;
        this.load();
    }

    public void load() {
        Hooks.PLACEHOLDERAPI.load();
        Hooks.WORLDGUARD.load();
        Hooks.WORLDEDIT.load();
        Hooks.TOKENENCHANT.load();
        Hooks.TOKENMANAGER.load();
        Hooks.PLOTSQUARED.load();
    }

    public void registerPlaceholders() {
        if(!Hooks.PLACEHOLDERAPI.isCheck()) return;
        new HPlaceholderAPI(this.plugin).register();
    }

    public String getPlaceholders(final Player player, final String message) {
        if(!Hooks.PLACEHOLDERAPI.isCheck()) return StringUtils.colorize(message);
        return new HPlaceholderAPI().getPlaceholders(player, message);
    }

    public void giveMoney(final OfflinePlayer player, final double amount) {
        if(!Hooks.VAULT.isCheck()) return;
        new HVaultAPI(player, amount).addMoney();
    }

    public void takeMoney(final OfflinePlayer player, final double amount) {
        if(!Hooks.VAULT.isCheck()) return;
        new HVaultAPI(player, amount).takeMoney();
    }

    public void giveTokens(final OfflinePlayer player, final double amount) {
        if(Hooks.TOKENENCHANT.isCheck()) new HTokenEnchantAPI(player, amount).addTokens();
        if(Hooks.TOKENMANAGER.isCheck()) new HTokenManagerAPI(player, amount).addTokens();
    }

    public void takeTokens(final OfflinePlayer player, final double amount) {
        if(Hooks.TOKENENCHANT.isCheck()) new HTokenEnchantAPI(player, amount).takeTokens();
        if(Hooks.TOKENMANAGER.isCheck()) new HTokenManagerAPI(player, amount).takeTokens();
    }

    public void loadAndCopySchem(final File file, final World world, final Location loc) {
        if(!Hooks.WORLDEDIT.isCheck()) return;
        new HWorldEditAPI(file, world, loc).loadAndCopy();
    }

    public void pasteSchem(final File file, final World world, final Location loc) {
        if(!Hooks.WORLDEDIT.isCheck()) return;
        new HWorldEditAPI(file, world, loc).paste();
    }

    public boolean checkPlot(final UUID uuid) {
        if(!Hooks.PLOTSQUARED.isCheck()) return false;
        return new HPlotSquaredAPI(uuid).checkPlot();
    }

    public ApplicableRegionSet getRegions(final Location location) {
        if(!Hooks.WORLDGUARD.isCheck()) return null;
        return new HWorldGuardAPI(location).applicableRegions();
    }


}
