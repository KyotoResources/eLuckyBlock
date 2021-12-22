package it.zS0bye.eLuckyBlock.files;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import lombok.Getter;

import java.io.*;

@Getter
public class SchematicsFile {

    private final eLuckyBlock plugin;

    private String schematic;
    private String pattern;
    private final File file;
    private final File directory;

    public SchematicsFile(eLuckyBlock plugin, final String schematic) {
        this.plugin = plugin;
        this.schematic = schematic;
        this.directory = new File(this.plugin.getDataFolder(), "schematics");
        this.file = new File(directory, schematic + ".schem");

        this.createDir();
    }

    public SchematicsFile(eLuckyBlock plugin) {
        this.plugin = plugin;
        this.pattern = "schematics/example.schem";
        this.directory = new File(this.plugin.getDataFolder(), "schematics");
        this.file = new File(directory, "example.schem");

        this.loadSchematics();
    }

    public void loadSchematics() {
        createDir();

        if(directory.listFiles().length != 0)
           return;

        if(!file.exists()) {
            try {
                file.createNewFile();
                plugin.saveResource(pattern, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDir() {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        if(!directory.exists())
            directory.mkdirs();
    }

}
