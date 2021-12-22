package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.files.SchematicsFile;
import it.zS0bye.eLuckyBlock.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import java.io.FileInputStream;
import java.io.IOException;

public class SchematicExecutor extends Executors {

    private final eLuckyBlock plugin;
    private final String execute;
    private final Player player;
    private final Location location;


    public SchematicExecutor(final String execute, final Player player, final Location location) {
        this.plugin = eLuckyBlock.getInstance();
        this.execute = execute;
        this.player = player;
        this.location = location;
        if (this.execute.startsWith(getType()))
            apply();
    }

    @Override
    protected String getType() {
        return "[SCHEMATIC] ";
    }

    @Override
    protected void apply() {

        String schematic = ColorUtils.getPapi(this.player, execute
                .replace(getType(), ""));

        SchematicsFile schemFile = new SchematicsFile(this.plugin, schematic);
        if(schemFile.getFile() == null) {
            return;
        }

        ClipboardFormat format = ClipboardFormats.findByFile(schemFile.getFile());
        Clipboard clipboard = null;
        World world = BukkitAdapter.adapt(this.player.getWorld());
        double x = this.location.getX();
        double y = this.location.getY();
        double z = this.location.getZ();

        try (ClipboardReader reader = format.getReader(new FileInputStream(schemFile.getFile()))) {
            clipboard = reader.read();
        }catch (IOException ex) {
            ex.printStackTrace();
        }

        if(clipboard == null) {
            return;
        }

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(x, y, z))
                    .ignoreAirBlocks(true)
                    .build();
            Operations.complete(operation);
        }catch (WorldEditException ex) {
            ex.printStackTrace();
        }

    }
}
