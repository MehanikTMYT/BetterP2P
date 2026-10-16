package dev.lasm.betterp2p.util.p2p;
import appeng.parts.p2p.P2PTunnelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import java.util.function.Supplier;

public final class ClientTunnelInfo extends TunnelInfo {
    public final Supplier<Identifier> icon;
    public ClientTunnelInfo(int index, ItemStack stack, Class<? extends P2PTunnelPart<?>> clazz, Supplier<Identifier> icon) {
        super(index, stack, clazz); this.icon = icon;
    }
    public Supplier<Identifier> getIcon() { return icon; }
    @Override public String toString() { return "ClientTunnelInfo(icon=" + icon.get() + ") " + super.toString(); }
}
