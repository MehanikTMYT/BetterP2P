package dev.lasm.betterp2p.util.p2p;
import appeng.parts.p2p.P2PTunnelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class TunnelInfo {
    public final int index;
    public final ItemStack stack;
    public final Class<? extends P2PTunnelPart<?>> clazz;
    public final Component dispName;
    public TunnelInfo(int index, ItemStack stack, Class<? extends P2PTunnelPart<?>> clazz) {
        this.index = index; this.stack = stack; this.clazz = clazz;
        this.dispName = stack.getDisplayName();
    }
    public int getIndex() { return index; }
    public ItemStack getStack() { return stack; }
    public Class<? extends P2PTunnelPart<?>> getClazz() { return clazz; }
    public Component getDispName() { return dispName; }
    @Override public String toString() {
        return "TunnelInfo(index=" + index + ", stack=" + stack + ", clazz=" + clazz + ", dispName=" + dispName.getString() + ")";
    }
}
