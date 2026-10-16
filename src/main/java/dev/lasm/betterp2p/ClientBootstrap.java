package dev.lasm.betterp2p;
import dev.lasm.betterp2p.client.ClientCache;
import dev.lasm.betterp2p.client.RenderBlockOutline;
import dev.lasm.betterp2p.client.gui.GuiAdvancedMemoryCard;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

final class ClientBootstrap {
    private ClientBootstrap() {}
    static CommonProxy create(IEventBus bus) {
        bus.addListener(ClientBootstrap::registerScreens);
        NeoForge.EVENT_BUS.addListener(ClientBootstrap::render);
        NeoForge.EVENT_BUS.addListener(ClientBootstrap::logout);
        return new ClientProxy();
    }
    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(BetterP2P.ADVANCED_MEMORY_CARD_MENU.get(), (menu, inventory, title) -> new GuiAdvancedMemoryCard(menu));
    }
    private static void render(RenderLevelStageEvent.AfterTranslucentFeatures event) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        var buffers = minecraft.renderBuffers().bufferSource();
        RenderBlockOutline.showPartPlacementPreview(minecraft.player, event.getPoseStack(), buffers, event.getLevelRenderState().cameraRenderState.pos);
        buffers.endBatch();
    }
    private static void logout(ClientPlayerNetworkEvent.LoggingOut event) { ClientCache.clear(); }
}
