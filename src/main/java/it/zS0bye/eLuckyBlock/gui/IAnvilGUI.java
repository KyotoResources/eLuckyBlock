package it.zS0bye.eLuckyBlock.gui;

import com.cryptomorin.xseries.XMaterial;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.events.CloseAnvilEvent;
import it.zS0bye.eLuckyBlock.events.EventExecutor;
import it.zS0bye.eLuckyBlock.events.CompleteAnvilEvent;
import it.zS0bye.eLuckyBlock.files.enums.GUI;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class IAnvilGUI {

    private final String id;
    private final String guiName;

    private final static Map<String, EventExecutor<CompleteAnvilEvent>> inputEvent = new HashMap<>();
    private final static Map<String, EventExecutor<CloseAnvilEvent>> closeEvent = new HashMap<>();

    public IAnvilGUI(final ELuckyBlock plugin, final String id, final Player player, final String type, final String regex) {
        this.id = id;
        this.guiName = "Input_GUI";
        final String title = GUI.CUSTOM.getString(this.guiName + GUI.TITLE.getPath())
                .replace("%edit%", type);

        new AnvilGUI.Builder()
                .title(title)
                .itemLeft(this.item())
                .onClose(target -> {
                    final CloseAnvilEvent apply = new CloseAnvilEvent();
                    if (!closeEvent.containsKey(this.id)) return;
                    final EventExecutor<CloseAnvilEvent> mode = closeEvent.get(this.id);
                    if (mode == null) return;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> mode.execute(apply), 2L);
                })
                .onComplete((target, text) -> {
                    final CompleteAnvilEvent complete = new CompleteAnvilEvent(text, regex);
                    if (!inputEvent.containsKey(this.id)) return AnvilGUI.Response.close();
                    final EventExecutor<CompleteAnvilEvent> mode = inputEvent.get(this.id);
                    if (mode == null) return AnvilGUI.Response.close();
                    mode.execute(complete);
                    return AnvilGUI.Response.close();
                })
                .plugin(plugin)
                .open(player);
    }

    private ItemStack item() {
        final String path = this.guiName + GUI.ITEMS.getPath() + ".paper";
        final ItemStack item = XMaterial.PAPER.parseItem();
        if(item == null) return new ItemStack(Material.AIR);
        final ItemMeta meta = item.getItemMeta();
        if(meta == null) return new ItemStack(Material.AIR);
        meta.setDisplayName(GUI.CUSTOM.getString(path + GUI.ITEM_DISPLAY_NAME.getPath()));
        meta.setLore(GUI.CUSTOM.getStringList(path + GUI.ITEM_LORE.getPath()));
        item.setItemMeta(meta);
        return item;
    }

    public void setInputEvent(final EventExecutor<CompleteAnvilEvent> event) {
        inputEvent.put(this.id, event);
    }

    public IAnvilGUI setCloseEvent(final EventExecutor<CloseAnvilEvent> event) {
        closeEvent.put(this.id, event);
        return this;
    }

}
