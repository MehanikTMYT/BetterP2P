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
public record C2SUnlinkP2P(P2PLocation p2p, int tunnelType) implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SUnlinkP2P> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"unlink_p2p"));
 public static final StreamCodec<ByteBuf,C2SUnlinkP2P> STREAM_CODEC=StreamCodec.composite(P2PLocation.STREAM_CODEC,C2SUnlinkP2P::p2p,ByteBufCodecs.INT,C2SUnlinkP2P::tunnelType,C2SUnlinkP2P::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
public C2SUnlinkP2P(P2PLocation p2p){this(p2p,MemoryInfo.TUNNEL_ANY);} public int getType(){return tunnelType;}
 public P2PLocation getP2p(){return p2p;}
 public int getTunnelType(){return tunnelType;}
 public static void handle(C2SUnlinkP2P m,IPayloadContext ctx){var state=ModNetwork.playerState.get(ctx.player().getUUID());if(state==null)return;state.gridCache.unlinkP2P(m.p2p());ModNetwork.requestP2PUpdate(ctx.player());}
}
