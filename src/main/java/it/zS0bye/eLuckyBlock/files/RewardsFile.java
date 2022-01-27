package it.zS0bye.eLuckyBlock.files;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Rewards;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

@Getter
public class RewardsFile {

    private final ELuckyBlock plugin;

    private FileConfiguration config;
    private final String pattern;
    private final File file;

    public RewardsFile(ELuckyBlock plugin) {

        this.plugin = plugin;
        this.pattern = "rewards.yml";
        this.file = new File(this.plugin.getDataFolder(), this.pattern);

        this.saveConfig();
    }

    @SneakyThrows
    private void saveConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();

            InputStream is = plugin.getResource(this.pattern);
            OutputStream os = new FileOutputStream(file);

            ByteStreams.copy(is, os);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        InputStream is = plugin.getResource(this.pattern);
        if (is != null)
            this.config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(is, Charsets.UTF_8)));
        for(Rewards rewards : Rewards.values())
            rewards.reloadConfig();
        this.plugin.reloadRewards();
    }

}