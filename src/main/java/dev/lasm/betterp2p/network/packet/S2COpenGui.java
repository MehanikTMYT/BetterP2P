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
public record S2COpenGui(List<P2PInfo> infos, MemoryInfo memoryInfo) implements IS2CMessage {
 public static final CustomPacketPayload.Type<S2COpenGui> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"open_gui"));
 public static final StreamCodec<ByteBuf,S2COpenGui> STREAM_CODEC=StreamCodec.composite(ByteBufCodecs.collection(ArrayList::new,BetterP2PCodecs.P2P_INFO_STREAM,Integer.MAX_VALUE),S2COpenGui::infos,MemoryInfo.STREAM_CODEC,S2COpenGui::memoryInfo,S2COpenGui::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
public S2COpenGui(){this(List.of(),new MemoryInfo());} public S2COpenGui(List<P2PInfo> infos){this(infos,new MemoryInfo());}
 public List<P2PInfo> getInfos(){return infos;}
 public MemoryInfo getMemoryInfo(){return memoryInfo;}
 public static void handle(S2COpenGui m,IPayloadContext ctx){ClientHandlers.openGui(m,ctx);}
}
