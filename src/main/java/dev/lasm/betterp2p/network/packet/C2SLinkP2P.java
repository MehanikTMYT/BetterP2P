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
public record C2SLinkP2P(P2PLocation input, P2PLocation output) implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SLinkP2P> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"link_p2p"));
 public static final StreamCodec<ByteBuf,C2SLinkP2P> STREAM_CODEC=StreamCodec.composite(P2PLocation.STREAM_CODEC,C2SLinkP2P::input,P2PLocation.STREAM_CODEC,C2SLinkP2P::output,C2SLinkP2P::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}

 public P2PLocation getInput(){return input;}
 public P2PLocation getOutput(){return output;}
 public static void handle(C2SLinkP2P m,IPayloadContext ctx){var state=ModNetwork.playerState.get(ctx.player().getUUID());if(state!=null && state.gridCache.linkP2P(m.input(),m.output())!=null)ModNetwork.requestP2PUpdate(ctx.player());}
}
