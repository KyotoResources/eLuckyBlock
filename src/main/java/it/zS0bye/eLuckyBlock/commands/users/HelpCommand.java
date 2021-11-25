package it.zS0bye.eLuckyBlock.commands.users;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.utils.LangUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends BaseCommand {

    private CommandSender sender;
    private String command;
    private eLuckyBlock plugin;

    public HelpCommand(final CommandSender sender, final String command, final eLuckyBlock plugin) {
        this.sender = sender;
        this.command = command;
        this.plugin = plugin;
        execute();
    }

    public HelpCommand(final String[] args, final CommandSender sender, final String command, final eLuckyBlock plugin) {
        this.sender = sender;
        this.command = command;
        this.plugin = plugin;
        if(args[0].equalsIgnoreCase(getName()))
            execute();
    }

    public HelpCommand(final List<String> tab) {
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "help";
    }

    @Override
    protected void execute() {

        LangUtils.HELP_USERS_TEXTS.getStringList().forEach(text -> sender.sendMessage(text
                .replace("%command%", command)
                .replace("%version%", version())
                .replace("%author%", author())));

    }
    private String version() {
        return "ₑLuckyBlock v" + this.plugin.getDescription().getVersion();
    }

    private String author() {
        return "Developed by zS0bye";
    }
}
