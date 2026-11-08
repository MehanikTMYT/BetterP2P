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
public record C2SUpdateMemoryInfo(MemoryInfo info) implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SUpdateMemoryInfo> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"update_memory_info"));
 public static final StreamCodec<ByteBuf,C2SUpdateMemoryInfo> STREAM_CODEC=StreamCodec.composite(MemoryInfo.STREAM_CODEC,C2SUpdateMemoryInfo::info,C2SUpdateMemoryInfo::new);
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
public C2SUpdateMemoryInfo(){this(new MemoryInfo());}
 public MemoryInfo getInfo(){return info;}
 public static void handle(C2SUpdateMemoryInfo m,IPayloadContext ctx){var stack=ctx.player().getMainHandItem();if(stack.has(BetterP2P.MEMORY_INFO))stack.update(BetterP2P.MEMORY_INFO.get(),new MemoryInfo(),old->m.info());}
}
