package it.zS0bye.eLuckyBlock.imgs.enums;

public enum ImageChar {
    BLOCK('\u2588');
    private final char c;

    ImageChar(char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }
}
