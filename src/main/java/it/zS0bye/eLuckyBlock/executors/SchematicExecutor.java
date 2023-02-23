package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.SchematicsFile;
import it.zS0bye.eLuckyBlock.hooks.HooksManager;
import it.zS0bye.eLuckyBlock.utils.VersionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SchematicExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final Location location;
    private final HooksManager hooks;

    public SchematicExecutor(final ELuckyBlock plugin, final String execute, final Player player, final Location location) {
        this.plugin = plugin;
        this.execute = execute;
        this.player = player;
        this.location = location;
        this.hooks = plugin.getHooks();
        if (!this.execute.startsWith(this.getType())) return;
        if (!VersionUtils.legacy()) {
            this.apply();
            return;
        }
        this.plugin.getLogger().severe("The \"[SCHEMATIC]\" executor only works with WorldEdit 7+!");
    }

    @Override
    protected String getType() {
        return "[SCHEMATIC] ";
    }

    @Override
    protected void apply() {

        final String schematic = this.hooks.getPlaceholders(this.player, execute
                .replace(getType(), ""));

        final SchematicsFile schemFile = new SchematicsFile(this.plugin, schematic);

        if(schemFile.getFile() == null) return;

        this.hooks.loadAndCopySchem(schemFile.getFile(), this.player.getWorld(), this.location);
        this.hooks.pasteSchem(schemFile.getFile(), this.player.getWorld(), this.location);
    }
}
