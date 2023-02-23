package it.zS0bye.eLuckyBlock.settings.enums;

public enum SettingType {
    CREATE("create"),
    EDIT("edit");

    private final String type;

    SettingType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
