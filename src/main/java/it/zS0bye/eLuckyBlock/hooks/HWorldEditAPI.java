package it.zS0bye.eLuckyBlock.hooks;

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
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HWorldEditAPI {

    private final File file;
    private final World world;
    private final Location loc;
    private Clipboard clipboard;

    public HWorldEditAPI(final File file, final World world, final Location loc) {
        this.file = file;
        this.world = world;
        this.loc = loc;
    }

    public void loadAndCopy() {
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        if(format == null)
            return;

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void paste() {
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(this.world);
        double x = this.loc.getX();
        double y = this.loc.getY();
        double z = this.loc.getZ();

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
