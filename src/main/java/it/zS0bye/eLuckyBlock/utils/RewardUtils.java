package it.zS0bye.eLuckyBlock.utils;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import it.zS0bye.eLuckyBlock.executors.*;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RewardUtils extends FileUtils {

    private final FileConfiguration rewards;
    private final String chance;
    private final String execute;

    public RewardUtils(final String luckyblock, final String reward) {
        this.rewards = eLuckyBlock.getInstance().getRewards().getConfig();
        this.chance = luckyblock + "." + reward + ".chance";
        this.execute = luckyblock + "." + reward + ".execute";
    }

    protected String getPrefix() {
        return ConfigUtils.SETTINGS_PREFIX.getString();
    }

    public String getString(final String path) {
        return ColorUtils.getColor(this.rewards.getString(path));
    }

    public boolean equals(final String path, final String equals) {
        return this.rewards.getString(path).equalsIgnoreCase(equals);
    }

    public boolean getBoolean(String path) {
        return this.rewards.getBoolean(path);
    }

    public List<String> getStringList(final String path) {
        List<String> list = new ArrayList<>();
        for(String setList : this.rewards.getStringList(path)) {
            list.add(ColorUtils.getColor(setList));
        }
        return list;
    }

    public boolean contains(String path) {
        return this.rewards.contains(path);
    }

    public int getInt(final String path) {
        return this.rewards.getInt(path);
    }

    public void sendReward(final Player player, final Location loc) {
        getStringList(getExecute()).forEach(execute -> {
            new TitleExecutor(execute, player);
            new ActionExecutor(execute, player);
            new BossBarExecutor(execute, player);
            new BroadcastActionExecutor(execute, player);
            new BroadcastExecutor(execute, player);
            new BroadcastTitleExecutor(execute, player);
            new BroadcastBossBarExecutor(execute, player);
            new ClearEffectExecutor(execute, player);
            new ConsoleExecutor(execute, player);
            new EffectExecutor(execute, player);
            new MessageExecutor(execute, player);
            new PlayerExecutor(execute, player);
            new SoundExecutor(execute, player);
            new SudoExecutor(execute, player);
            new SpawnMobExecutor(execute, loc);
            new ItemExecutor(execute, loc);
            new GiveXPExecutor(execute, player);
            new TakeXPExecutor(execute, player);
            new FireworksExecutor(execute, player, loc);
            new ParticlesExecutor(execute, player, loc);

            if(ConfigUtils.HOOKS_VAULT.getBoolean()) {
                new GiveMoneyExecutor(execute, player);
                new TakeMoneyExecutor(execute, player);
            }

            if(ConfigUtils.HOOKS_WORLDEDIT.getBoolean())
                new SchematicExecutor(execute, player, loc);
        });
    }
}
