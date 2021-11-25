package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FireworkUtils extends FileUtils {

    private final FileConfiguration fireworks;
    private final String type;
    private final String times;
    private final String delay;
    private final String colors;

    public FireworkUtils(final String firework) {
        this.fireworks = eLuckyBlock.getInstance().getFireworks().getConfig();
        this.type = firework + ".type";
        this.times = firework + ".times";
        this.delay = firework + ".delay";
        this.colors = firework + ".colors";
    }

    @Override
    protected String getPrefix() {
        return ConfigUtils.SETTINGS_PREFIX.getString();
    }

    @Override
    public String getString(String path) {
        return ColorUtils.getColor(this.fireworks.getString(path));
    }

    @Override
    public List<String> getStringList(String path) {
        List<String> list = new ArrayList<>();
        for(String setList : this.fireworks.getStringList(path)) {
            list.add(ColorUtils.getColor(setList));
        }
        return list;
    }

    @Override
    public boolean contains(String path) {
        return this.fireworks.contains(path);
    }

    @Override
    public boolean equals(String path, String equals) {
        return this.fireworks.getString(path).equalsIgnoreCase(equals);
    }

    @Override
    public boolean getBoolean(String path) {
        return this.fireworks.getBoolean(path);
    }

    @Override
    public int getInt(String path) {
        return this.fireworks.getInt(path);
    }
}
