package it.zS0bye.eLuckyBlock.rewards;

import java.util.List;

public class ExcReward {

    private final List<String> commands;

    public ExcReward(List<String> commands) {
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

}