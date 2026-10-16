package dev.lasm.betterp2p.item;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.parts.IPartHost;
import appeng.parts.p2p.P2PTunnelPart;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.network.ModNetwork;
import dev.lasm.betterp2p.network.data.MemoryInfo;
import dev.lasm.betterp2p.network.data.P2PLocation;
import dev.lasm.betterp2p.util.p2p.P2PUtil;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public final class ItemAdvancedMemoryCard extends Item {
    public ItemAdvancedMemoryCard(Properties properties) {
        super(properties.stacksTo(1).component(BetterP2P.MEMORY_INFO, new MemoryInfo()));
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> list, TooltipFlag flag) {
        var info = stack.getOrDefault(BetterP2P.MEMORY_INFO, new MemoryInfo());
        list.accept(Component.translatable("gui.advanced_memory_card.mode." + info.mode().name().toLowerCase(Locale.ROOT)));
    }
    @Override public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            // Client state belongs to the physical client; never resolve client classes on a dedicated server.
            if (level.isClientSide()) BetterP2P.proxy.clearClientCache();
            return InteractionResult.SUCCESS;
        }
        return super.use(level, player, hand);
    }
    @Override public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.PASS;
        var player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;
        var entity = level.getBlockEntity(context.getClickedPos());
        if (entity instanceof IInWorldGridNodeHost && entity instanceof IPartHost host) {
            var selected = host.selectPartWorld(context.getClickLocation());
            var part = selected.part != null ? selected.part : host.getPart(null);
            if (part == null || part.getGridNode() == null) return InteractionResult.FAIL;
            var grid = part.getGridNode().getGrid();
            var stack = context.getItemInHand();
            var info = stack.getOrDefault(BetterP2P.MEMORY_INFO, new MemoryInfo());
            Optional<P2PLocation> entry = Optional.empty();
            int type = -1;
            if (part instanceof P2PTunnelPart<?> tunnel) {
                type = P2PUtil.getTypeIndex(tunnel); entry = Optional.of(P2PLocation.toLoc(tunnel));
            }
            var updated = new MemoryInfo(entry, info.frequency(), info.mode(), info.guiScale(), type);
            stack.set(BetterP2P.MEMORY_INFO, updated);
            ModNetwork.initConnection(player, grid, updated);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
