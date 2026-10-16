package dev.lasm.betterp2p.network.packet;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.network.ModNetwork;
import dev.lasm.betterp2p.network.data.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.*;
public record C2SRenameP2P(P2PLocation p2p, String name) implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SRenameP2P> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"rename_p2p"));
 public static final StreamCodec<ByteBuf,C2SRenameP2P> STREAM_CODEC=StreamCodec.composite(P2PLocation.STREAM_CODEC,C2SRenameP2P::p2p,ByteBufCodecs.STRING_UTF8,C2SRenameP2P::name,C2SRenameP2P::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}

 public P2PLocation getP2p(){return p2p;}
 public String getName(){return name;}
 public static void handle(C2SRenameP2P message, IPayloadContext context) {
    var player = context.player();
    var state = ModNetwork.playerState.get(player.getUUID());
    if (state == null) return;
    // Resolve only a tunnel in the grid associated with this open card session.
    var tunnel = state.gridCache.getTunnel(message.p2p());
    if (tunnel == null) return;
    var input = tunnel.isOutput() ? tunnel.getInput() : tunnel;
    if (input == null) return;
    dev.lasm.betterp2p.util.p2p.P2PUtil.setCustomName(tunnel,
            net.minecraft.network.chat.Component.literal(message.name()));
    for (var output : input.getOutputs()) {
        state.gridCache.markDirty(P2PLocation.toLoc(output), output);
    }
    state.gridCache.markDirty(P2PLocation.toLoc(input), input);
    ModNetwork.requestP2PUpdate(player);
 }
}
