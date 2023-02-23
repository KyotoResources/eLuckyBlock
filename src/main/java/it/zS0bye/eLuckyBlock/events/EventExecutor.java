package it.zS0bye.eLuckyBlock.events;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface EventExecutor<T extends Event> {

    void execute(@NotNull final T event);

}

