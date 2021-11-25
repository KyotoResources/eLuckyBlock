package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.files.LanguagesFile;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public enum LangUtils implements IFileUtils {
    PLAYER_NOT_FOUND("player_not_found"),
    INSUFFICIENT_PERMISSIONS("insufficient_permissions"),
    UPDATE_NOTIFICATION("update_notification"),
    HELP_USERS_TEXTS("Help_Command.users.texts"),
    RELOAD_ADMINS_CONFIGURATIONS("Reload_Command.admins.configurations"),
    INFO_USERS_CURRENT_BREAKS("Info_Command.users.current_breaks"),
    INFO_ADMINS_PLAYER_BREAKS("Info_Command.admins.player_breaks");


    private final String path;
    private final eLuckyBlock plugin;
    private final LanguagesFile lang;
    private final static int CENTER_PX = 154;

    LangUtils(String path) {
        this.path = path;
        this.plugin = eLuckyBlock.getInstance();
        this.lang = this.plugin.getLang();
    }

    @Override
    public String getPrefix() {
        return ConfigUtils.SETTINGS_PREFIX.getString();
    }

    @Override
    public String getString() {
        return ColorUtils.getColor(this.lang.getConfig().getString(path));
    }

    @Override
    public List<String> getStringList() {
        List<String> list = new ArrayList<>();
        for(String setList : this.lang.getConfig().getStringList(path)) {
            list.add(ColorUtils.getColor(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean() {
        return this.lang.getConfig().getBoolean(path);
    }

    @Override
    public int getInt() {
        return this.lang.getConfig().getInt(path);
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
