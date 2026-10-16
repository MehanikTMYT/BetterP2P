package dev.lasm.betterp2p;
import appeng.api.parts.IPartItem;
import appeng.core.definitions.AEParts;
import appeng.core.definitions.ItemDefinition;
import appeng.parts.p2p.*;
import dev.lasm.betterp2p.util.p2p.TunnelInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import java.util.*;

/** Side-neutral tunnel table. Initialize only after item components have been bound. */
public class CommonProxy {
    protected final Map<Class<?>, TunnelInfo> tunnelTypes = new LinkedHashMap<>();
    protected final Map<Integer, TunnelInfo> tunnelIndices = new LinkedHashMap<>();
    private boolean initialized;
    public synchronized void initTunnels() {
        if (initialized) return;
        tunnelTypes.clear(); tunnelIndices.clear();
        registerTunnel(AEParts.ME_P2P_TUNNEL, 0, MEP2PTunnelPart.class);
        registerTunnel(AEParts.FE_P2P_TUNNEL, 1, FEP2PTunnelPart.class);
        registerTunnel(AEParts.REDSTONE_P2P_TUNNEL, 2, RedstoneP2PTunnelPart.class);
        registerTunnel(AEParts.FLUID_P2P_TUNNEL, 3, FluidP2PTunnelPart.class);
        registerTunnel(AEParts.ITEM_P2P_TUNNEL, 4, ItemP2PTunnelPart.class);
        registerTunnel(AEParts.LIGHT_P2P_TUNNEL, 5, LightP2PTunnelPart.class);
        registerModTunnel("appmek", "chemical_p2p_tunnel", 6, "me.ramidzkh.mekae2.ae2.ChemicalP2PTunnelPart");
        registerModTunnel("mae2", "pattern_p2p_tunnel", 7, "stone.mae2.parts.p2p.PatternP2PTunnelPart");
        if (ModList.get().isLoaded("gtceu")) registerModTunnel("mae2", "eu_p2p_tunnel", 8, "stone.mae2.parts.p2p.EUP2PTunnelPart");
        initialized = true;
    }
    protected TunnelInfo createInfo(int index, ItemStack stack, Class<? extends P2PTunnelPart<?>> clazz) {
        return new TunnelInfo(index, stack, clazz);
    }
    private void registerTunnel(ItemDefinition<?> item, int index, Class<? extends P2PTunnelPart<?>> clazz) {
        register(createInfo(index, item.stack(1), clazz));
    }
    private void register(TunnelInfo info) { tunnelTypes.put(info.clazz, info); tunnelIndices.put(info.index, info); }
    @SuppressWarnings("unchecked")
    private void registerModTunnel(String namespace, String path, int index, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            var item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(namespace, path));
            if (!P2PTunnelPart.class.isAssignableFrom(clazz) || !(item instanceof IPartItem<?>)) {
                BetterP2P.logger.error("Unsupported P2P integration {}: class/item is not a tunnel", className);
                return;
            }
            register(createInfo(index, new ItemStack(item), (Class<? extends P2PTunnelPart<?>>) clazz));
        } catch (ClassNotFoundException e) {
            BetterP2P.logger.debug("Optional P2P integration {} is absent", className);
        }
    }
    public synchronized TunnelInfo getP2PFromIndex(int index) { initTunnels(); return tunnelIndices.get(index); }
    public synchronized TunnelInfo getP2PFromClass(Class<?> clazz) { initTunnels(); return tunnelTypes.get(clazz); }
    public synchronized List<TunnelInfo> getP2PTypeList() { initTunnels(); return List.copyOf(tunnelIndices.values()); }
    public void clearClientCache() {}
}
