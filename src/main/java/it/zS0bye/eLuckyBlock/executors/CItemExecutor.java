package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.CItemsFile;
import org.bukkit.Location;

import java.util.logging.Level;

public class CItemExecutor extends Executors {

    private final ELuckyBlock plugin;
    private final String execute;
    private final Location location;

    public CItemExecutor(final ELuckyBlock plugin, final String execute, final Location location) {
        this.plugin = plugin;
        this.execute = execute;
        this.location = location;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    @Override
    protected String getType() {
        return "[CUSTOM_ITEM] ";
    }

    @Override
    protected void apply() {

        final String citem = execute
                .replace(this.getType(), "");

        final CItemsFile file = new CItemsFile(this.plugin, citem);

        if(!file.getFile().exists()) {
            this.plugin.getLogger().log(Level.SEVERE, "The custom item you entered is invalid!");
            return;
        }

        if(this.location.getWorld() == null) return;

        this.location.getWorld().dropItem(this.location, file.getItem());
    }
}
