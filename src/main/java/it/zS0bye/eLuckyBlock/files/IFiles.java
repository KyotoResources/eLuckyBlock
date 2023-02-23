package it.zS0bye.eLuckyBlock.files;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFiles {

    String getPath();

    void reloadConfig();

    String getString(final String... var);

    String getCustomString(final String minVar, final Map<String, String>... placeholders);

    String getCustomString(final Map<String, String>... placeholders);

    List<String> getStringList(final String... var);

    boolean getBoolean(final String... var);

    boolean contains(final String... var);

    int getInt(final String... var);

    double getDouble(final String... var);

    void send(final CommandSender sender, final String minVar, final Map<String, String>... placeholders);

    void send(final CommandSender sender, final Map<String, String>... placeholders);

    String variables(final String... var);

    Set<String> getKeys();

    Set<String> getConfigurationSection(final String... var);

}
