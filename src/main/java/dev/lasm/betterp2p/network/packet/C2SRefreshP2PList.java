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
public record C2SRefreshP2PList(int tunnelType) implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SRefreshP2PList> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"refresh_p2p_list"));
 public static final StreamCodec<ByteBuf,C2SRefreshP2PList> STREAM_CODEC=StreamCodec.composite(ByteBufCodecs.INT,C2SRefreshP2PList::tunnelType,C2SRefreshP2PList::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
public C2SRefreshP2PList(){this(MemoryInfo.TUNNEL_ANY);} public int getType(){return tunnelType;}
 public int getTunnelType(){return tunnelType;}
 public static void handle(C2SRefreshP2PList m,IPayloadContext ctx){ModNetwork.requestP2PList(ctx.player(),m.tunnelType());}
}
