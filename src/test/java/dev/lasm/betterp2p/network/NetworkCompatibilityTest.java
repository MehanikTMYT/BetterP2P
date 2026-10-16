package dev.lasm.betterp2p.network;

import com.mojang.serialization.JsonOps;
import dev.lasm.betterp2p.client.gui.widget.GuiScale;
import dev.lasm.betterp2p.item.BetterMemoryCardModes;
import dev.lasm.betterp2p.network.data.*;
import dev.lasm.betterp2p.network.packet.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class NetworkCompatibilityTest {
    private static P2PLocation location() {
        return new P2PLocation(new BlockPos(-120, 64, 30), Direction.EAST, Level.NETHER);
    }

    @Test void memoryWireOrderAndEnumOrdinalsRemainCompatible() {
        MemoryInfo info = new MemoryInfo(Optional.of(location()), (short) 0x8123,
                BetterMemoryCardModes.COPY, GuiScale.DYNAMIC, 7);
        ByteBuf buffer = Unpooled.buffer();
        try {
            MemoryInfo.STREAM_CODEC.encode(buffer, info);
            int length = buffer.writerIndex();
            assertTrue(buffer.readBoolean());
            assertEquals(location(), P2PLocation.STREAM_CODEC.decode(buffer));
            assertEquals((short) 0x8123, buffer.readShort());
            assertEquals(2, buffer.readInt());
            assertEquals(GuiScale.DYNAMIC.ordinal(), buffer.readInt());
            assertEquals(7, buffer.readInt());
            assertEquals(length, buffer.readerIndex());
            buffer.readerIndex(0);
            assertEquals(info, MemoryInfo.STREAM_CODEC.decode(buffer));
            assertFalse(buffer.isReadable());
        } finally { buffer.release(); }
    }

    @Test void persistentMemoryFormatStillOmitsTransientType() {
        MemoryInfo info = new MemoryInfo(Optional.of(location()), (short) 123,
                BetterMemoryCardModes.UNBIND, GuiScale.DYNAMIC, 17);
        var json = MemoryInfo.CODEC.encodeStart(JsonOps.INSTANCE, info).getOrThrow().getAsJsonObject();
        assertEquals(4, json.size());
        assertTrue(json.has("selectedEntry"));
        assertTrue(json.has("frequency"));
        assertTrue(json.has("mode"));
        assertTrue(json.has("guiScale"));
        assertFalse(json.has("type"));
        MemoryInfo decoded = MemoryInfo.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
        assertEquals(new MemoryInfo(info.selectedEntry(), info.frequency(), info.mode(), info.guiScale()), decoded);
        assertEquals(-1, decoded.type());
    }

    @Test void tunnelWireOrderIncludesEveryField() {
        P2PInfo info = new P2PInfo((short) 0x9012, location().pos(), location().dim(),
                location().facing(), "Туннель", true, false, 32, 4);
        ByteBuf buffer = Unpooled.buffer();
        try {
            BetterP2PCodecs.P2P_INFO_STREAM.encode(buffer, info);
            assertEquals(info.frequency(), buffer.readShort());
            assertEquals(info.pos(), BlockPos.STREAM_CODEC.decode(buffer));
            assertEquals(info.dim(), ResourceKey.streamCodec(Registries.DIMENSION).decode(buffer));
            assertEquals(info.facing(), Direction.STREAM_CODEC.decode(buffer));
            assertEquals(info.name(), ByteBufCodecs.STRING_UTF8.decode(buffer));
            assertTrue(buffer.readBoolean());
            assertFalse(buffer.readBoolean());
            assertEquals(32, buffer.readInt());
            assertEquals(4, buffer.readInt());
            assertFalse(buffer.isReadable());
            buffer.readerIndex(0);
            P2PInfo decoded = BetterP2PCodecs.P2P_INFO_STREAM.decode(buffer);
            // P2PInfo equality deliberately covers location only, so compare the other fields too.
            assertEquals(info, decoded);
            assertEquals(info.frequency(), decoded.frequency());
            assertEquals(info.name(), decoded.name());
            assertEquals(info.output(), decoded.output());
            assertEquals(info.hasChannel(), decoded.hasChannel());
            assertEquals(info.channels(), decoded.channels());
            assertEquals(info.type(), decoded.type());
        } finally { buffer.release(); }
    }

    @Test void tunnelIdentityIgnoresMutableDisplayState() {
        P2PLocation loc = location();
        P2PInfo before = new P2PInfo((short) 1, loc.pos(), loc.dim(), loc.facing(), "old", false, false, 0, 1);
        P2PInfo after = new P2PInfo((short) 2, loc.pos(), loc.dim(), loc.facing(), "new", true, true, 32, 4);
        assertEquals(before, after);
        assertEquals(before.hashCode(), after.hashCode());
        assertEquals(loc.hashCode(), before.hashCode());
        assertNotEquals(before, new P2PInfo((short) 1, loc.pos(), Level.OVERWORLD, loc.facing(), "old", false, false, 0, 1));
        assertNotEquals(loc, new P2PLocation(loc.pos(), Direction.WEST, loc.dim()));
    }

    @Test void payloadIdentifiersAndListEnvelopeRemainCompatible() {
        List<IMessage> packets = List.of(new C2SChangeP2PType(1, location()), new C2SCloseGui(),
                new C2SLinkP2P(location(), location()), new C2SRefreshP2PList(),
                new C2SRenameP2P(location(), "name"), new C2SUnlinkP2P(location()),
                new C2SUpdateMemoryInfo(), new S2COpenGui(), new S2CUpdateP2P());
        List<String> paths = List.of("change_p2p_type", "close_gui", "link_p2p", "refresh_p2p_list",
                "rename_p2p", "unlink_p2p", "update_memory_info", "open_gui", "update_p2p");
        for (int i = 0; i < packets.size(); i++) {
            assertEquals("betterp2p:" + paths.get(i), packets.get(i).type().id().toString());
        }
        ByteBuf buffer = Unpooled.buffer();
        try {
            S2CUpdateP2P.STREAM_CODEC.encode(buffer, new S2CUpdateP2P(List.of(), true));
            assertEquals(0, buffer.readByte()); // Empty collection VarInt length.
            assertEquals(1, buffer.readByte()); // Clear flag follows the list.
            assertFalse(buffer.isReadable());
            C2SCloseGui.STREAM_CODEC.encode(buffer, new C2SCloseGui());
            assertFalse(buffer.isReadable()); // Unit payload has no bytes.
        } finally { buffer.release(); }
    }
}
