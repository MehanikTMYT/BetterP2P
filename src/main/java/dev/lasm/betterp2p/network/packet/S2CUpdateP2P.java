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
public record S2CUpdateP2P(List<P2PInfo> infos, boolean clear) implements IS2CMessage {
 public static final CustomPacketPayload.Type<S2CUpdateP2P> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"update_p2p"));
 public static final StreamCodec<ByteBuf,S2CUpdateP2P> STREAM_CODEC=StreamCodec.composite(ByteBufCodecs.collection(ArrayList::new,BetterP2PCodecs.P2P_INFO_STREAM,Integer.MAX_VALUE),S2CUpdateP2P::infos,ByteBufCodecs.BOOL,S2CUpdateP2P::clear,S2CUpdateP2P::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
public S2CUpdateP2P(){this(List.of(),false);} public S2CUpdateP2P(List<P2PInfo> infos){this(infos,false);}
 public List<P2PInfo> getInfos(){return infos;}
 public boolean getClear(){return clear;}
 public static void handle(S2CUpdateP2P m,IPayloadContext ctx){ClientHandlers.updateP2P(m,ctx);}
}
