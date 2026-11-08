package dev.lasm.betterp2p.network.data;
import appeng.parts.p2p.P2PTunnelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
public final class HashHelper {
 private HashHelper() {}
 public static final long k2 = 0x9ae16a3b2f90404fL;
 public static long hashP2P(BlockPos pos, int facing, ResourceKey<Level> dim) {
  long lo = (long) pos.getX() | ((long) pos.getY() << 32);
  long hi = (long) pos.getZ() | ((long) dim.identifier().hashCode() << 32);
  long hash = hashLen16(lo, hi);
  hash ^= hash >>> 59;
  return ((long) facing << 60) | hash;
 }
 public static long hashP2P(P2PTunnelPart<?> p) { return hashP2P(p.getBlockEntity().getBlockPos(), p.getSide().ordinal(), p.getLevel().dimension()); }
 private static long hashLen16(long lo, long hi) {
  long mul = k2 + 32L, a = lo + k2;
  long c = Long.rotateRight(hi, 37) * mul + a;
  long d = (Long.rotateRight(a, 25) + hi) * mul;
  long e = (c ^ d) * mul; e ^= e >>> 47;
  long f = (d ^ e) * mul; f ^= f >>> 47;
  return f * mul;
 }
}
