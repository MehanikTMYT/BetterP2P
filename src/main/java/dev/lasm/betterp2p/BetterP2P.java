package dev.lasm.betterp2p;
import dev.lasm.betterp2p.client.AdvancedMemoryCardMenu;
import dev.lasm.betterp2p.item.ItemAdvancedMemoryCard;
import dev.lasm.betterp2p.network.ModNetwork;
import dev.lasm.betterp2p.network.data.MemoryInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterP2P.MOD_ID)
public final class BetterP2P {
    public static final String MOD_ID = "betterp2p";
    public static final Logger logger = LogManager.getLogger(MOD_ID);
    public static CommonProxy proxy;
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredHolder<Item, ItemAdvancedMemoryCard> ADVANCED_MEMORY_CARD_ITEM =
            ITEMS.registerItem("advanced_memory_card", ItemAdvancedMemoryCard::new);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<AdvancedMemoryCardMenu>> ADVANCED_MEMORY_CARD_MENU =
            MENUS.register("advanced_memory_card", () -> new MenuType<>(AdvancedMemoryCardMenu::new, FeatureFlagSet.of()));
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MemoryInfo>> MEMORY_INFO =
            DATA_COMPONENTS.registerComponentType("memory_info", builder -> builder.persistent(MemoryInfo.CODEC).networkSynchronized(MemoryInfo.STREAM_CODEC));
    public BetterP2P(IEventBus modBus) {
        proxy = FMLEnvironment.getDist().isClient() ? ClientBootstrap.create(modBus) : new CommonProxy();
        ITEMS.register(modBus); MENUS.register(modBus); DATA_COMPONENTS.register(modBus);
        NeoForge.EVENT_BUS.addListener(BetterP2P::onPlayerQuit);
        NeoForge.EVENT_BUS.addListener(BetterP2P::onServerStopped);
        modBus.addListener(ModNetwork::registerNetwork);
        modBus.addListener(BetterP2P::onBuildCreativeModeTabContents);
    }
    private static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) { ModNetwork.removeConnection(event.getEntity()); }
    private static void onServerStopped(ServerStoppedEvent event) { ModNetwork.playerState.clear(); }
    private static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath("ae2", "main"))))
            event.accept(ADVANCED_MEMORY_CARD_ITEM.get());
    }
}
