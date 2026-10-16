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
public final class C2SCloseGui implements IC2SMessage {
 public static final CustomPacketPayload.Type<C2SCloseGui> TYPE=new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"close_gui"));
 public static final StreamCodec<ByteBuf,C2SCloseGui> STREAM_CODEC=StreamCodec.unit(new C2SCloseGui());
 @Override public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){return TYPE;}
 @Override public boolean equals(Object other){return other instanceof C2SCloseGui;}
 @Override public int hashCode(){return 0;}
 public static void handle(C2SCloseGui m,IPayloadContext ctx){ModNetwork.playerState.remove(ctx.player().getUUID());}
}
