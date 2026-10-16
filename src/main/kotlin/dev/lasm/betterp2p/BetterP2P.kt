package dev.lasm.betterp2p

import com.mojang.blaze3d.vertex.PoseStack
import dev.lasm.betterp2p.client.AdvancedMemoryCardMenu
import dev.lasm.betterp2p.client.RenderBlockOutline
import dev.lasm.betterp2p.client.gui.GuiAdvancedMemoryCard
import dev.lasm.betterp2p.item.ItemAdvancedMemoryCard
import dev.lasm.betterp2p.network.ModNetwork
import dev.lasm.betterp2p.network.data.MemoryInfo
import java.util.function.Supplier
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.Item
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(BetterP2P.MOD_ID)
object BetterP2P {
    val proxy: CommonProxy =
        runForDist({ Supplier { ClientProxy() } }, { Supplier { CommonProxy() } }).get()

    const val MOD_ID = "betterp2p"

    val logger: Logger = LogManager.getLogger(MOD_ID)

    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(MOD_ID)
    val ADVANCED_MEMORY_CARD_ITEM: DeferredHolder<Item, ItemAdvancedMemoryCard> =
        ITEMS.registerItem("advanced_memory_card", ::ItemAdvancedMemoryCard)

    val MENUS: DeferredRegister<MenuType<*>> = DeferredRegister.create(Registries.MENU, MOD_ID)
    val ADVANCED_MEMORY_CARD_MENU: DeferredHolder<MenuType<*>, MenuType<AdvancedMemoryCardMenu>> =
        MENUS.register(
            "advanced_memory_card",
            Supplier { MenuType(::AdvancedMemoryCardMenu, FeatureFlagSet.of()) }
        )

    val DATA_COMPONENTS: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MOD_ID)
    val MEMORY_INFO: DeferredHolder<DataComponentType<*>, DataComponentType<MemoryInfo>> =
        DATA_COMPONENTS.registerComponentType("memory_info") { builder ->
            builder.persistent(MemoryInfo.CODEC).networkSynchronized(MemoryInfo.STREAM_CODEC)
        }

    init {
        ITEMS.register(MOD_BUS)
        MENUS.register(MOD_BUS)
        DATA_COMPONENTS.register(MOD_BUS)
        NeoForge.EVENT_BUS.addListener(::onPlayerQuit)
        NeoForge.EVENT_BUS.addListener(::onRenderLevelStage)
        MOD_BUS.addListener(ModNetwork::registerNetwork)
        MOD_BUS.addListener(::onRegisterMenuScreens)
        MOD_BUS.addListener(::onBuildCreativeModeTabContents)
        MOD_BUS.addListener(::onCommonSetup)
    }

    fun onCommonSetup(event: FMLCommonSetupEvent) {
        // Fleet port (26.1): item components are bound only after the datapacks load, so building the tunnel ItemStacks
        // here throws "Components not bound yet". Proxy initialises the tunnel table lazily on first use instead.
    }

    fun onPlayerQuit(event: PlayerEvent.PlayerLoggedOutEvent) {
        ModNetwork.removeConnection(event.entity)
    }

    fun onBuildCreativeModeTabContents(event: BuildCreativeModeTabContentsEvent) {
        if (
            event.tabKey ==
                ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    Identifier.fromNamespaceAndPath("ae2", "main")
                )
        )
            event.accept(ADVANCED_MEMORY_CARD_ITEM.get())
    }

    // Fleet port (26.1): stages are separate event classes; the camera comes from the extracted level render state.
    fun onRenderLevelStage(context: RenderLevelStageEvent.AfterTranslucentFeatures) {
        val player = Minecraft.getInstance().player ?: return
        val buffers = Minecraft.getInstance().renderBuffers().bufferSource()
        RenderBlockOutline.showPartPlacementPreview(
            player,
            context.poseStack,
            buffers,
            context.levelRenderState.cameraRenderState.pos
        )
        buffers.endBatch()
    }

    fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
        event.register(ADVANCED_MEMORY_CARD_MENU.get()) { menu, inv, name ->
            GuiAdvancedMemoryCard(menu)
        }
    }
}
