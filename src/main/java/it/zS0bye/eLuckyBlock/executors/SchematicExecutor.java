package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.SchematicsFile;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.utils.VersionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SchematicExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final Player player;
    private final Location location;
    private final HooksManager hooks;
    private String execute;

    public SchematicExecutor(final ELuckyBlock plugin, final String execute, final Player player, final Location location) {
        this.plugin = plugin;
        this.player = player;
        this.location = location;
        this.hooks = plugin.getHooks();
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(getType(), "");
        this.apply();
    }

    @Override
    protected String getType() {
        return "[SCHEMATIC] ";
    }

    @Override
    protected void apply() {

        if (VersionUtils.legacy()) {
            this.plugin.getLogger().severe("The \"[SCHEMATIC]\" executor only works with WorldEdit 7+!");
            return;
        }

        final String schematic = this.hooks.getPlaceholders(this.player, execute
                .replace(getType(), ""));

        final SchematicsFile schemFile = new SchematicsFile(this.plugin, schematic);

        if(schemFile.getFile() == null) return;

        this.hooks.loadAndCopySchem(schemFile.getFile(), this.player.getWorld(), this.location);
        this.hooks.pasteSchem(schemFile.getFile(), this.player.getWorld(), this.location);
    }
}
