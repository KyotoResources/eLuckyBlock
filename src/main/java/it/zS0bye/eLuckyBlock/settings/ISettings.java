package it.zS0bye.eLuckyBlock.settings;

import org.bukkit.inventory.ItemStack;

public interface ISettings {

    void cancel();

    String getId();

    void setInstantBreak(final boolean value);

    boolean hasInstantBreak();

    void setUniqueCheck(final boolean value);

    boolean hasUniqueCheck();

    void setPermissionEnabled(final boolean value);

    boolean hasPermissionEnabled();

    void setToggleLB(final boolean value);

    boolean hasToggleLB();

    void setPermission(final String value);

    String getPermission();

    void setMaterial(final ItemStack value);

    ItemStack getMaterial();

}
