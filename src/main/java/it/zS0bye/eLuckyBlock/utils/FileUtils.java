package it.zS0bye.eLuckyBlock.utils;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class FileUtils {

    private final static int CENTER_PX = 154;

    protected abstract String getPrefix();

    public abstract String getString(final String path);

    public abstract List<String> getStringList(final String path);

    public abstract boolean contains(final String path);

    public abstract boolean equals(final String path, final String equals);

    public abstract boolean getBoolean(final String path);

    public abstract int getInt(final String path);

    public String getCustomString(final String path) {
        if (getString(path).startsWith("%prefix%")) {
            String replace = getString(path).replace("%prefix%", getPrefix());
            if (replace.startsWith(getPrefix() + "%center%")) {
                String replace2 = replace.replace("%center%", "");
                return sendCentered(replace2);
            }
            return replace;
        }
        if(getString(path).startsWith("%center%")) {
            String replace = getString(path.replace("%center%", ""));
            return sendCentered(replace);
        }
        return getString(path);
    }

    public void send(final CommandSender sender, final String path) {
        if (getCustomString(path).isEmpty()) {
            return;
        }
        sender.sendMessage(getCustomString(path));
    }

    public static void send(final String msg, final CommandSender sender) {
        if (msg.isEmpty()) {
            return;
        }
        sender.sendMessage(msg);
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
