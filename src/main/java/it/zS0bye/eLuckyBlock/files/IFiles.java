package it.zS0bye.eLuckyBlock.files;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public interface IFiles {

    void reloadConfig();

    String getString(final String... var);

    String getCustomString(final String... var);

    List<String> getStringList(final String... var);

    boolean getBoolean(final String... var);

    boolean contains(final String... var);

    int getInt(final String... var);

    void send(final CommandSender sender, final String... var);

    StringBuilder variables(final String... var);

    Set<String> getKeys();

    Set<String> getConfigurationSection(final String... var);

}
