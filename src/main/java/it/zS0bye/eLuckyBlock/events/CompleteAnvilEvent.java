package it.zS0bye.eLuckyBlock.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class CompleteAnvilEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancellable;

    @Setter
    @Getter
    private String message;
    @Getter
    @Setter
    private String regex;

    @Getter
    private boolean pattern;

    public CompleteAnvilEvent(final String message, final String regex) {
        this.cancellable = false;
        this.message = message;
        this.regex = regex;

        if(this.regex == null || this.regex.isEmpty()) return;
        this.pattern = Pattern.compile(this.regex).matcher(this.message).matches();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancellable;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancellable = cancel;
    }
}
