package dev.lasm.betterp2p.network.data;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
public final class BetterP2PCodecs {
 private BetterP2PCodecs() {}
 public static final StreamCodec<ByteBuf,P2PInfo> P2P_INFO_STREAM = new StreamCodec<>() {
 public P2PInfo decode(ByteBuf b){return new P2PInfo(ByteBufCodecs.SHORT.decode(b),BlockPos.STREAM_CODEC.decode(b),ResourceKey.streamCodec(Registries.DIMENSION).decode(b),Direction.STREAM_CODEC.decode(b),ByteBufCodecs.STRING_UTF8.decode(b),ByteBufCodecs.BOOL.decode(b),ByteBufCodecs.BOOL.decode(b),ByteBufCodecs.INT.decode(b),ByteBufCodecs.INT.decode(b));}
 public void encode(ByteBuf b,P2PInfo p){ByteBufCodecs.SHORT.encode(b,p.frequency());BlockPos.STREAM_CODEC.encode(b,p.pos());ResourceKey.streamCodec(Registries.DIMENSION).encode(b,p.dim());Direction.STREAM_CODEC.encode(b,p.facing());ByteBufCodecs.STRING_UTF8.encode(b,p.name());ByteBufCodecs.BOOL.encode(b,p.output());ByteBufCodecs.BOOL.encode(b,p.hasChannel());ByteBufCodecs.INT.encode(b,p.channels());ByteBufCodecs.INT.encode(b,p.type());}
 };
}
