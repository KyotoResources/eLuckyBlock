package it.zS0bye.eLuckyBlock.files;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class LuckyBlocksFile {

    private final ELuckyBlock plugin;
    private final File directory;

    private FileConfiguration config;
    private String luckyblock;
    private String material;
    private String pattern;
    private File file;

    private boolean instant_break;
    private boolean unique_check;
    private boolean permission_required;
    private boolean prevents;

    public LuckyBlocksFile(final ELuckyBlock plugin, final String luckyblock, final String material, final boolean instant_break, final boolean unique_check, final boolean permission_required, final boolean prevents) {
        this.plugin = plugin;
        this.luckyblock = luckyblock;
        this.material = material;
        this.instant_break = instant_break;
        this.unique_check = unique_check;
        this.permission_required = permission_required;
        this.prevents = prevents;
        this.pattern = "luckyblocks/" + this.luckyblock.toLowerCase() + ".yml";
        this.directory = new File(this.plugin.getDataFolder(), "luckyblocks");
        this.file = new File(this.directory, this.luckyblock.toLowerCase() + ".yml");
    }

    @SuppressWarnings("all")
    public LuckyBlocksFile(final ELuckyBlock plugin, final String luckyblock) {
        this.plugin = plugin;
        this.luckyblock = luckyblock;
        this.pattern = "luckyblocks/" + this.luckyblock.toLowerCase() + ".yml";
        this.directory = new File(this.plugin.getDataFolder(), "luckyblocks");
        this.file = new File(directory, this.luckyblock.toLowerCase() + ".yml");

        if(!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdir();
        if(!this.directory.exists()) this.directory.mkdirs();
        if(!this.plugin.getLuckyConfig().containsKey(this.luckyblock.toLowerCase())) {
            this.config = YamlConfiguration.loadConfiguration(this.file);
            return;
        }
        this.config = this.plugin.getLuckyConfig().get(this.luckyblock.toLowerCase());
    }

    public LuckyBlocksFile(final ELuckyBlock plugin) {
        this.plugin = plugin;
        this.directory = new File(this.plugin.getDataFolder(), "luckyblocks");
    }

    @SuppressWarnings("all")
    @SneakyThrows
    public void createFile() {

        if(!this.plugin.getDataFolder().exists()) this.plugin.getDataFolder().mkdir();
        if(!this.directory.exists()) this.directory.mkdirs();

        this.config = YamlConfiguration.loadConfiguration(this.file);

        this.config.addDefault("display_name", this.luckyblock);
        this.config.addDefault("material", this.material);
        this.config.addDefault("instant_break", this.instant_break);
        this.config.addDefault("unique_check.enabled", this.unique_check);
        this.config.addDefault("unique_check.display_name", "&dLuckyblock &l" + this.luckyblock);
        this.config.addDefault("unique_check.lore", Arrays.asList("&5&l┃ &7Place and open me!"));
        this.config.addDefault("permission_required.enabled", this.permission_required);
        this.config.addDefault("permission_required.permission", "luckyblock." + this.luckyblock.toLowerCase());
        this.config.addDefault("permission_required.executors", new ArrayList<>());
        this.config.addDefault("prevents.enabled", this.prevents);
        this.config.addDefault("prevents.deny_pickup", true);
        this.config.addDefault("prevents.deny_absorb", true);
        this.config.addDefault("prevents.blocked_worlds.type", "BLACKLIST");
        this.config.addDefault("prevents.blocked_worlds.worlds", Arrays.asList("blworld1"));
        this.config.addDefault("prevents.blocked_regions.type", "BLACKLIST");
        this.config.addDefault("prevents.blocked_regions.regions", Arrays.asList("blregion1"));
        this.config.addDefault("prevents.allowed_gamemodes", Arrays.asList("SURVIVAL"));

        this.config.options().copyDefaults(true);
        this.save();
    }

    @SuppressWarnings("all")
    public static List<String> getFiles(final ELuckyBlock plugin) {
        final List<String> files = new ArrayList<>();
        final File directory = new File(plugin.getDataFolder(), "luckyblocks");
        if(directory.listFiles() == null) return files;
        for(final File file : directory.listFiles()) files.add(file.getName()
                .replace(".yml", ""));
        return files;
    }

    @SuppressWarnings("all")
    public List<FileConfiguration> getConfigs() {
        final List<FileConfiguration> configs = new ArrayList<>();
        for(final File file : this.directory.listFiles()) configs.add(YamlConfiguration.loadConfiguration(file));
        return configs;
    }

    @SneakyThrows
    public void save() {
        this.config.save(this.file);
        this.plugin.getLuckyConfig().put(this.luckyblock.toLowerCase(), this.config);
    }

    public boolean isExist() {
        return this.file.exists();
    }

    @SuppressWarnings("all")
    public void delete() {
        this.file.delete();
        this.plugin.getLuckyConfig().remove(this.luckyblock.toLowerCase());
    }

    public void setName(final String name) {
        if(this.config == null) return;
        this.config.set("name", name);
        this.save();
    }

    @SneakyThrows
    public boolean rename(final String newName, final CommandSender sender) {
        if(this.luckyblock.equalsIgnoreCase(newName)) {
            this.setName(newName);
            return true;
        }
        if(this.plugin.getLuckyConfig().containsKey(newName.toLowerCase())) {
//            Lang.RENAME_ERRORS_ALREADY_EXISTS.send(sender);
            return false;
        }
        final File newFile = new File(this.directory, newName.toLowerCase() + ".yml");
        if(!this.file.renameTo(newFile)) return false;
        this.config = YamlConfiguration.loadConfiguration(newFile);
        this.plugin.getLuckyConfig().remove(this.luckyblock.toLowerCase());
        this.plugin.getLuckyConfig().put(newName.toLowerCase(), this.config);
        this.config.set("display_name", newName);
        this.config.save(newFile);
        return true;
    }

}
