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
public record C2SChangeP2PType(int newType, P2PLocation p2p) implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SChangeP2PType> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"change_p2p_type"));
 public static final StreamCodec<ByteBuf,C2SChangeP2PType> STREAM_CODEC=StreamCodec.composite(ByteBufCodecs.INT,C2SChangeP2PType::newType,P2PLocation.STREAM_CODEC,C2SChangeP2PType::p2p,C2SChangeP2PType::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
public C2SChangeP2PType(P2PLocation p2p){this(MemoryInfo.TUNNEL_ANY,p2p);}
 public int getNewType(){return newType;}
 public P2PLocation getP2p(){return p2p;}
 public static void handle(C2SChangeP2PType m,IPayloadContext ctx){var state=ModNetwork.playerState.get(ctx.player().getUUID());if(state==null)return;var type=BetterP2P.proxy.getP2PFromIndex(m.newType());if(type==null)return;if(state.gridCache.changeAllP2Ps(m.p2p(),type))ModNetwork.requestP2PList(ctx.player(),type.index);}
}
