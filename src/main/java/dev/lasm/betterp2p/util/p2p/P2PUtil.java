package dev.lasm.betterp2p.util.p2p;
import appeng.parts.AEBasePart;
import appeng.parts.p2p.P2PTunnelPart;
import dev.lasm.betterp2p.BetterP2P;
import net.minecraft.network.chat.Component;
import java.lang.reflect.Field;

public final class P2PUtil {
    private P2PUtil() {}
    // Resolve the fixed AE2 field names once, rather than on every GUI action.
    private static final class Fields {
        static final Field CUSTOM_NAME = field(AEBasePart.class, "customName");
        static final Field OUTPUT = field(P2PTunnelPart.class, "output");
        private static Field field(Class<?> owner, String name) {
            try { Field field = owner.getDeclaredField(name); field.setAccessible(true); return field; }
            catch (ReflectiveOperationException e) { throw new ExceptionInInitializerError(e); }
        }
    }
    public static boolean hasChannel(P2PTunnelPart<?> tunnel) { return tunnel.isPowered() && tunnel.isActive(); }
    public static int getTypeIndex(P2PTunnelPart<?> tunnel) {
        TunnelInfo info = BetterP2P.proxy.getP2PFromClass(tunnel.getClass());
        return info == null ? -1 : info.index;
    }
    public static void setCustomName(AEBasePart part, Component name) {
        try { Fields.CUSTOM_NAME.set(part, name); }
        catch (IllegalAccessException e) { throw new IllegalStateException("Cannot update AE2 tunnel name", e); }
    }
    public static void pleaseSetTheFuckingOutputState(P2PTunnelPart<?> part, boolean output) {
        try { Fields.OUTPUT.setBoolean(part, output); part.getHost().markForSave(); }
        catch (IllegalAccessException e) { throw new IllegalStateException("Cannot update AE2 tunnel output", e); }
    }
}
