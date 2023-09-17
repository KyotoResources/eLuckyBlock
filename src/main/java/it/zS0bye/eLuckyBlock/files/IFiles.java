package it.zS0bye.eLuckyBlock.files;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFiles {

    String getPath();

    void reloadConfig();

    String getString(final String... var);

    String getStringNoColor(final String... var);

    String getCustomString(final String... var);

    String getCustomString(String replace, final String... var);

    List<String> getStringList(final String... var);

    boolean getBoolean(final String... var);

    boolean contains(final String... var);

    int getInt(final String... var);

    double getDouble(final String... var);

    void send(final CommandSender sender, final String... var);

    void send(final CommandSender sender, final Map<String, String> placeholders, final String... var);

    void sendList(final CommandSender sender, final String... var);

    String variables(final String... var);

    Set<String> getKeys();

    Set<String> getConfigurationSection(final String... var);

}
