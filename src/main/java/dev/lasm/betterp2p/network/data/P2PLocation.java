package dev.lasm.betterp2p.network.data;
import appeng.parts.p2p.P2PTunnelPart;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
public record P2PLocation(BlockPos pos, Direction facing, ResourceKey<Level> dim) {
 public static final Codec<P2PLocation> CODEC = RecordCodecBuilder.create(i -> i.group(
 BlockPos.CODEC.fieldOf("pos").forGetter(P2PLocation::pos), Direction.CODEC.fieldOf("facing").forGetter(P2PLocation::facing),
 ResourceKey.codec(Registries.DIMENSION).fieldOf("dim").forGetter(P2PLocation::dim)).apply(i, P2PLocation::new));
 public static final StreamCodec<ByteBuf,P2PLocation> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC,P2PLocation::pos,Direction.STREAM_CODEC,P2PLocation::facing,ResourceKey.streamCodec(Registries.DIMENSION),P2PLocation::dim,P2PLocation::new);
 public BlockPos getPos(){return pos;} public Direction getFacing(){return facing;} public ResourceKey<Level> getDim(){return dim;}
 @Override public int hashCode(){return Long.hashCode(HashHelper.hashP2P(pos,facing.ordinal(),dim));}
 public static P2PLocation toLoc(P2PTunnelPart<?> p){return new P2PLocation(p.getBlockEntity().getBlockPos(),p.getSide(),p.getLevel().dimension());}
}
