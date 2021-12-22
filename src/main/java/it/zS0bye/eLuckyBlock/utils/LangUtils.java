package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public enum LangUtils implements IFileUtils {
    PLAYER_NOT_FOUND("player_not_found", "%prefix%&cPlayer not found."),
    IS_NOT_NUMBER("is_not_number", "%prefix%&cPlease enter a valid number."),
    ONLY_POSITIVE_NUMBERS("only_positive_numbers", "%prefix%&cThe number entered must be greater than 0, and cannot be negative!"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions", "%prefix%&cYou don''t have enough permissions."),
    UPDATE_NOTIFICATION("update_notification", "%prefix%A new update is available! Download version &d%new%&7 from &dhttps://www.spigotmc.org/resources/eluckyblock.97759/&7, you currently have version &d%old%&7."),
    HELP_USERS_TEXTS("Help_Command.users.texts", ",&5 ┃ &d%version%,,&5 ┃ &e[] &7⁼ &7Optional,&5 ┃ &c‹› &7⁼ &7Required,,&5 ┃ &f/%command% help &7‑ &dOpen this page.,&5 ┃ &f/%command% about &7‑ &dShow plug-in info.,&5 ┃ &f/%command% reload &7‑ &dReload configurations.,&5 ┃ &f/%command% info &e[player] &7‑ &dShow the breaks LuckyBlocks.,&5 ┃ &f/%command% give &c<player> <lucky> &e[amount] &7‑ &dGive a player some LuckyBlocks.,,&5 ┃ &d%author%, "),
    RELOAD_ADMINS_CONFIGURATIONS("Reload_Command.admins.configurations", "%prefix%The configurations have been reloaded!"),
    INFO_USERS_CURRENT_BREAKS("Info_Command.users.current_breaks", "%prefix%You have &d%lbBreaks% &7breaks LuckyBlocks."),
    INFO_ADMINS_PLAYER_BREAKS("Info_Command.admins.player_breaks", "%prefix%&f%player%&7 has &d%lbBreaks% &7breaks LuckyBlocks."),
    GIVE_ADMINS_SENDER("Give_Command.admins.sender", "%prefix%You gave &d%luckyblock% x%amount% &7to &d%receiver%&7."),
    GIVE_ADMINS_RECEIVER("Give_Command.admins.receiver", "%prefix%You received &d%luckyblock% x%amount% &7by &d%sender%&7."),
    GIVE_ERRORS_NOT_UNIQUE("Give_Command.errors.not_unique", "%prefix%&cYou cannot give a luckyblock that is not unique!"),
    GIVE_ERRORS_NOT_EXIST("Give_Command.errors.not_exist", "%prefix%&cThis LuckyBlock does not exist!");


    private final String path;
    private final String def;
    private final eLuckyBlock plugin;
    private final FileConfiguration lang;
    private final static int CENTER_PX = 154;

    LangUtils(String path, String def) {
        this.path = path;
        this.def = def;
        this.plugin = eLuckyBlock.getInstance();
        this.lang = this.plugin.getLang().getConfig();
    }

    @Override
    public String getPrefix() {
        return ConfigUtils.SETTINGS_PREFIX.getString();
    }

    @Override
    public String getString() {
        if(contains())
            return ColorUtils.getColor(this.lang.getString(path));
        return ColorUtils.getColor(def);
    }

    @Override
    public List<String> getStringList() {
        List<String> list = new ArrayList<>();
        if(!contains()) {
            if (def.contains(",")) {
                for (String setList : def.split(",")) {
                    list.add(ColorUtils.getColor(setList));
                }
            }
            return list;
        }

        for (String setList : this.lang.getStringList(path)) {
            list.add(ColorUtils.getColor(setList));
        }

        return list;
    }

    @Override
    public boolean getBoolean() {
        if(contains())
            return this.lang.getBoolean(path);
        return Boolean.parseBoolean(def);
    }

    @Override
    public boolean contains() {
        return this.lang.contains(path);
    }

    @Override
    public int getInt() {
        if(contains())
            return this.lang.getInt(path);
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

    private String sendCentered(String message){
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
