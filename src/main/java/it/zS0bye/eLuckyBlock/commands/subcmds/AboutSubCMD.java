package it.zS0bye.eLuckyBlock.commands.subcmds;

import it.zS0bye.eLuckyBlock.commands.BaseCommand;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.utils.StringUtils;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class AboutSubCMD extends BaseCommand {

    private CommandSender sender;
    private ELuckyBlock plugin;

    public AboutSubCMD(final String[] args, final CommandSender sender, final ELuckyBlock plugin) {
        this.sender = sender;
        this.plugin = plugin;
        if(!args[0].equalsIgnoreCase(this.getName())) return;
        this.execute();
    }

    public AboutSubCMD(final CommandSender sender, final ELuckyBlock plugin) {
        this.sender = sender;
        this.plugin = plugin;
        this.execute();
    }

    public AboutSubCMD(final List<String> tab) {
        tab.add(getName());
    }

    @Override
    protected String getName() {
        return "about";
    }

    @Override
    protected void execute() {

        StringUtils.image(this.sender, this.getLegacyText());
        this.sender.spigot().sendMessage(this.getClickableLink());
        this.sender.sendMessage("");

    }

    private String[] getLegacyText() {
        final List<String> text = Arrays.asList(
                "",
                "&5&lₑLUCKYBLOCK &8‑ v" + this.plugin.getDescription().getVersion(),
                "",
                "&d┃ &7Rate our service by giving us",
                "&d┃ &7a positive review &e✭✭✭✭✭&7!",
                "",
                "&5● &7Developed by &dzS0bye",
                "");
        return text.toArray(new String[0]);
    }

    @SuppressWarnings("deprecation")
    private TextComponent getClickableLink() {
        final TextComponent text = new TextComponent(StringUtils.center("&7‹ &d● &7› &f&l&nCLICK TO OPEN THE RESOURCE&r &7‹ &d● &7›"));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§5§nClick Me!").create()));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/eluckyblock.97759/"));
        return text;
    }

}
