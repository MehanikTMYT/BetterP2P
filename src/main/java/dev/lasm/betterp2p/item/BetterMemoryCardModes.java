package dev.lasm.betterp2p.item;

public enum BetterMemoryCardModes {
    OUTPUT("output", 3), INPUT("input", 3), COPY("copy", 4), UNBIND("unbind", 2);
    public static final int MAX_TOOLTIP_LENGTH = 40;
    public final String unlocalizedName;
    public final String[] unlocalizedDesc;
    BetterMemoryCardModes(String name, int lines) {
        unlocalizedName = "gui.advanced_memory_card.mode." + name;
        unlocalizedDesc = new String[lines];
        for (int i = 0; i < lines; i++) unlocalizedDesc[i] = unlocalizedName + ".desc." + (i + 1);
    }
    public String getUnlocalizedName() { return unlocalizedName; }
    public String[] getUnlocalizedDesc() { return unlocalizedDesc; }
    public BetterMemoryCardModes next() { return next(false); }
    public BetterMemoryCardModes next(boolean reverse) {
        return values()[Math.floorMod(ordinal() + (reverse ? -1 : 1), values().length)];
    }
}
