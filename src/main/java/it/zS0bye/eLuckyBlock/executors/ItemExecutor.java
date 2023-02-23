package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ItemExecutor extends Executors {

    private final String execute;
    private final Location location;

    public ItemExecutor(final String execute, final Location location) {
        this.execute = execute;
        this.location = location;
        if (!this.execute.startsWith(this.getType())) return;
        this.apply();
    }

    protected String getType() {
        return "[ITEM] ";
    }

    protected void apply() {

        final String material = execute
                .replace(this.getType(), "")
                .split(";")[0]
                .toUpperCase();

        final int durability = Integer.parseInt(execute
                .replace(this.getType(), "")
                .split(";")[1]);

        final ItemStack item = new ItemStack(ConvertUtils.getMaterial(material), (short) durability);

        if(this.location.getWorld() == null) return;

        this.location.getWorld().dropItem(this.location, item);
    }
}