package dev.lasm.betterp2p.network.data;
import appeng.me.GridNode;
import appeng.parts.p2p.P2PTunnelPart;
import dev.lasm.betterp2p.util.p2p.P2PUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
public record P2PInfo(short frequency, BlockPos pos, ResourceKey<Level> dim, Direction facing, String name, boolean output, boolean hasChannel, int channels, int type) {
 public short getFrequency(){return frequency;} public BlockPos getPos(){return pos;} public ResourceKey<Level> getDim(){return dim;} public Direction getFacing(){return facing;} public String getName(){return name;} public boolean isOutput(){return output;} public boolean getOutput(){return output;} public boolean getHasChannel(){return hasChannel;} public int getChannels(){return channels;} public int getType(){return type;}
 @Override public int hashCode(){return Long.hashCode(HashHelper.hashP2P(pos,facing.ordinal(),dim));}
 @Override public boolean equals(Object other){return other instanceof P2PInfo p && pos.equals(p.pos) && facing == p.facing && dim.equals(p.dim);}
 public static P2PInfo toInfo(P2PTunnelPart<?> p){return new P2PInfo(p.getFrequency(),p.getBlockEntity().getBlockPos(),p.getBlockEntity().getLevel().dimension(),p.getSide(),p.getCustomName()==null?"":p.getCustomName().getString(),p.isOutput(),P2PUtil.hasChannel(p),p.getExternalFacingNode() instanceof GridNode node?node.getUsedChannels():-1,P2PUtil.getTypeIndex(p));}
}
