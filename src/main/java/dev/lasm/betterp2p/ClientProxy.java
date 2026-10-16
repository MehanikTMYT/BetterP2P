package dev.lasm.betterp2p;
import appeng.parts.p2p.P2PTunnelPart;
import dev.lasm.betterp2p.client.ClientCache;
import dev.lasm.betterp2p.util.p2p.ClientTunnelInfo;
import dev.lasm.betterp2p.util.p2p.TunnelInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public final class ClientProxy extends CommonProxy {
    @Override protected TunnelInfo createInfo(int index, ItemStack stack, Class<? extends P2PTunnelPart<?>> clazz) {
        String texture = switch (index) {
            case 0 -> "ae2:textures/block/quartz_block.png";
            case 1 -> "minecraft:textures/block/gold_block.png";
            case 2 -> "minecraft:textures/block/redstone_block.png";
            case 3 -> "minecraft:textures/block/lapis_block.png";
            case 4 -> "minecraft:textures/block/hopper_outside.png";
            case 5 -> "minecraft:textures/block/quartz_block_top.png";
            case 6 -> "mekanism:textures/block/block_osmium.png";
            case 7 -> "ae2:textures/block/pattern_provider.png";
            case 8 -> "minecraft:textures/block/copper_block.png";
            default -> throw new IllegalArgumentException("Unknown tunnel type " + index);
        };
        return new ClientTunnelInfo(index, stack, clazz, () -> Identifier.parse(texture));
    }
    @Override public void clearClientCache() { ClientCache.clear(); }
}
