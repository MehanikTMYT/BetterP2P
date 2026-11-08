package dev.lasm.betterp2p.network.data;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.lasm.betterp2p.client.gui.widget.GuiScale;
import dev.lasm.betterp2p.item.BetterMemoryCardModes;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
public record MemoryInfo(Optional<P2PLocation> selectedEntry, short frequency, BetterMemoryCardModes mode, GuiScale guiScale, int type) {
 public static final int TUNNEL_ANY = -1;
 public MemoryInfo(){this(Optional.empty(),(short)0,BetterMemoryCardModes.OUTPUT,GuiScale.DYNAMIC,TUNNEL_ANY);}
 public MemoryInfo(Optional<P2PLocation> selectedEntry,short frequency,BetterMemoryCardModes mode,GuiScale guiScale){this(selectedEntry,frequency,mode,guiScale,TUNNEL_ANY);}
 public Optional<P2PLocation> getSelectedEntry(){return selectedEntry;} public short getFrequency(){return frequency;} public BetterMemoryCardModes getMode(){return mode;} public GuiScale getGuiScale(){return guiScale;} public int getType(){return type;}
 public static final StreamCodec<ByteBuf,MemoryInfo> STREAM_CODEC=StreamCodec.composite(ByteBufCodecs.optional(P2PLocation.STREAM_CODEC),MemoryInfo::selectedEntry,ByteBufCodecs.SHORT,MemoryInfo::frequency,ByteBufCodecs.INT.map(i->BetterMemoryCardModes.values()[i],BetterMemoryCardModes::ordinal),MemoryInfo::mode,ByteBufCodecs.INT.map(i->GuiScale.values()[i],GuiScale::ordinal),MemoryInfo::guiScale,ByteBufCodecs.INT,MemoryInfo::type,MemoryInfo::new);
 // The persistent format intentionally omits the transient tunnel filter, as in the original.
 public static final Codec<MemoryInfo> CODEC=RecordCodecBuilder.create(i->i.group(Codec.optionalField("selectedEntry",P2PLocation.CODEC,false).forGetter(MemoryInfo::selectedEntry),Codec.SHORT.fieldOf("frequency").forGetter(MemoryInfo::frequency),Codec.INT.xmap(n->BetterMemoryCardModes.values()[n],BetterMemoryCardModes::ordinal).fieldOf("mode").forGetter(MemoryInfo::mode),Codec.INT.xmap(n->GuiScale.values()[n],GuiScale::ordinal).fieldOf("guiScale").forGetter(MemoryInfo::guiScale)).apply(i,MemoryInfo::new));
}
