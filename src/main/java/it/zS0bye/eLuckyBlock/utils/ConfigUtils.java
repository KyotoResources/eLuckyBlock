package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public enum ConfigUtils implements IFileUtils {
    SETTINGS_PREFIX("Settings.prefix", " &d❏ ₑLuckyBlock ┃ &7"),
    CHECK_UPDATE_ENABLED("Settings.check_update.enabled", "true"),
    CHECK_UPDATE_TYPE("Settings.check_update.type", "NORMAL"),
    HOOKS_PLACEHOLDERAPI("Settings.hooks.PlaceholderAPI", "false"),
    HOOKS_WORLDGUARD("Settings.hooks.WorldGuard", "false"),
    HOOKS_WORLDEDIT("Settings.hooks.WorldEdit", "false"),
    HOOKS_VAULT("Settings.hooks.Vault", "false"),
    DB_TYPE("Storage.type", "SQLite"),
    DB_NAME("Storage.mysql.database", "eLuckyBlock"),
    DB_HOST("Storage.mysql.hostname", "localhost"),
    DB_PORT("Storage.mysql.port", "3306"),
    DB_USER("Storage.mysql.user", "root"),
    DB_PASSWORD("Storage.mysql.password", "MyPassword"),
    DB_CUSTOMURI("Storage.mysql.advanced.customURI", "jdbc:mysql://%host%:%port%/%database%?useSSL=false");

    private final String path;
    private final String def;
    private final eLuckyBlock plugin;
    private final static int CENTER_PX = 154;

    ConfigUtils(String path, String def) {
        this.path = path;
        this.def = def;
        this.plugin = eLuckyBlock.getInstance();
    }

    @Override
    public String getPrefix() {
        return SETTINGS_PREFIX.getString();
    }

    @Override
    public String getString() {
        if(contains())
            return ColorUtils.getColor(this.plugin.getConfig().getString(path));
        return ColorUtils.getColor(def);
    }

    @Override
    public List<String> getStringList() {
        List<String> list = new ArrayList<>();
        if(contains()) {
            for (String setList : this.plugin.getConfig().getStringList(path)) {
                list.add(ColorUtils.getColor(setList));
            }
        }
        if(def.contains(",")) {
            for (String setList : def.split(",")) {
                list.add(ColorUtils.getColor(setList));
            }
        }
        return list;
    }

    @Override
    public boolean getBoolean() {
        if(contains())
            return this.plugin.getConfig().getBoolean(path);
        return Boolean.parseBoolean(def);
    }

    @Override
    public boolean contains() {
        return this.plugin.getConfig().contains(path);
    }

    @Override
    public int getInt() {
        if(contains())
            return this.plugin.getConfig().getInt(path);
        return Integer.parseInt(def);
    }

    public String getCustomString() {
        if (getString().startsWith("%prefix%")) {
            String replace = getString().replace("%prefix%", getPrefix());
            if (replace.startsWith(getPrefix() + "%center%")) {
                String replace2 = replace.replace("%center%", "");
                return sendCentered(replace2);
            }
            return replace;
        }
        if(getString().startsWith("%center%")) {
            String replace = getString().replace("%center%", "");
            return sendCentered(replace);
        }
        return getString();
    }

    public void send(final CommandSender sender) {
        if (getCustomString().isEmpty()) {
            return;
        }
        sender.sendMessage(getCustomString());
    }

    public String sendCentered(String message){
        message = ColorUtils.getColor(message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                FontUtils dFI = FontUtils.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = FontUtils.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString() + message;
    }

}
