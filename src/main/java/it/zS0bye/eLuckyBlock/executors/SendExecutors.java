package it.zS0bye.eLuckyBlock.executors;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SendExecutors {

    public static void send(final ELuckyBlock plugin, String executor, final Player player, final Location location, final Map<String, String> placeholders) {
            for (String key : placeholders.keySet()) executor = executor.replace(key, placeholders.get(key));
            new TitleExecutor(plugin, executor, player);
            new ActionExecutor(plugin, executor, player);
            new BossBarExecutor(plugin, executor, player);
            new ConsoleExecutor(plugin, executor, player);
            new MessageExecutor(plugin, executor, player);
            new PlayerExecutor(plugin, executor, player);
            new SoundExecutor(plugin, executor, player);
            new SudoExecutor(plugin, executor, player);
            new GiveMoneyExecutor(plugin, executor, player);
            new TakeMoneyExecutor(plugin, executor, player);

            new FireworksExecutor(plugin, executor, player, location);
            new CItemExecutor(plugin, executor, location);
            new SchematicExecutor(plugin, executor, player, location);

            new ParticlesExecutor(executor, player, location);
            new SpawnMobExecutor(executor, location);
            new ItemExecutor(executor, location);

            new ClearEffectExecutor(executor, player);
            new EffectExecutor(executor, player);
            new GiveXPExecutor(executor, player);
            new TakeXPExecutor(executor, player);
    }

    public static void send(final ELuckyBlock plugin, final String executors, final Player player, final Location location) {
        send(plugin, executors, player, location, new HashMap<>());
    }

}
