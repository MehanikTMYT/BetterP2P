package dev.lasm.betterp2p.util;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.parts.AEBasePart;
import appeng.parts.ICableBusContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;

public final class CableBusUtil {
    private CableBusUtil() {}
    public static ICableBusContainer getCableBus(BlockGetter level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CableBusBlockEntity bus ? bus.getCableBus() : null;
    }
    public static IPart getPart(BlockGetter level, BlockPos pos, HitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof IPartHost host)) return null;
        var selected = host.selectPartWorld(hit.getLocation());
        return selected == null ? null : selected.part;
    }
    public static BlockPos getFacingPos(AEBasePart part) {
        var host = part.getHost();
        if (host == null || host.getLocation() == null) return null;
        return host.getLocation().getPos().offset((part.getSide() == null ? Direction.UP : part.getSide()).getUnitVec3i());
    }
    public static BlockEntity getFacingTile(AEBasePart part) {
        var host = part.getHost();
        if (host == null || !host.isInWorld()) return null;
        var pos = getFacingPos(part);
        return pos == null ? null : host.getLocation().getLevel().getBlockEntity(pos);
    }
}
