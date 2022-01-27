package it.zS0bye.eLuckyBlock.files;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

@Getter
public class CItemsFile {

    private final ELuckyBlock plugin;
    private final File file;
    private final File directory;
    private final FileConfiguration config;

    public CItemsFile(final ELuckyBlock plugin, final String item) {
        this.plugin = plugin;
        this.directory = new File(this.plugin.getDataFolder(), "citems");
        this.file = new File(directory, item + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    @SneakyThrows
    public void create(final ItemStack item) {
        createDir();

        if(!file.exists()) {
            file.createNewFile();
            config.set("item", item);
            config.save(file);
            return;
        }

        config.set("item", item);
        config.save(file);
    }

    public ItemStack getItem() {
        return config.getItemStack("item");
    }

    private void createDir() {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if(!directory.exists())
            directory.mkdirs();
    }
}
