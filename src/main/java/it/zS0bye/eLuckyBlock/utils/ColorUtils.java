package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private final static int CENTER_PX = 154;

    public static String getColor(String message) {
        if(message == null) {
            eLuckyBlock.getInstance().getLogger()
                    .log(Level.SEVERE, "There was an error searching for a missing message!");
            return "";
        }
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPapi(Player p, String message) {
        if(ConfigUtils.HOOKS_PLACEHOLDERAPI.getBoolean())
            return PlaceholderAPI.setPlaceholders(p, getColor(message));
        return getColor(message);
    }

    public static String sendCentered(String message){
        message = getColor(message);

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
