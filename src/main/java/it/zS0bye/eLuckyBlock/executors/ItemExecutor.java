package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.utils.ConvertUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ItemExecutor extends Executors {

    private final Location location;
    private String execute;

    public ItemExecutor(final String execute, final Location location) {
        this.location = location;
        if (!execute.startsWith(this.getType())) return;
        this.execute = execute.replace(this.getType(), "");
        this.apply();
    }

    protected String getType() {
        return "[ITEM] ";
    }

    protected void apply() {
        final String material = execute.split(";")[0].toUpperCase();
        final int durability = Integer.parseInt(execute.split(";")[1]);
        final ItemStack item = new ItemStack(ConvertUtils.getMaterial(material), (short) durability);
        if(this.location.getWorld() == null) return;
        this.location.getWorld().dropItem(this.location, item);
    }
}