package dev.lasm.betterp2p.client.gui;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.network.data.*;
import dev.lasm.betterp2p.util.p2p.ClientTunnelInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.*;
public class InfoWrapper {
 public short frequency;public final boolean hasChannel,output;public final P2PLocation loc;public final int type;public String name;public boolean error;
 public Identifier icon,overlay=GuiHelper.OVERLAY;public final Component description,freqDisplay,channels;public final List<Component> hoverInfo=new ArrayList<>();
 public InfoWrapper(P2PInfo info){
 frequency=info.frequency();hasChannel=info.hasChannel();loc=new P2PLocation(info.pos(),info.facing(),info.dim());output=info.output();type=info.type();name=info.name();
 ClientTunnelInfo tunnel=(ClientTunnelInfo)BetterP2P.proxy.getP2PFromIndex(type);icon=tunnel.icon.get();
 description=Component.literal("Type: ").append(tunnel.dispName).append(" - ").append(Component.translatable("gui.advanced_memory_card.p2p_status."+(output?"output":"input")));
 // Kotlin UInt shift counts are masked to five bits, including the original shift by 32.
 String hex=Integer.toUnsignedString(frequency,16).toUpperCase(Locale.ROOT);
 freqDisplay=Component.translatable("item.betterp2p.advanced_memory_card.selected").append(" ").append(frequency!=0?Component.literal(format4(hex+hex)):Component.translatable("gui.advanced_memory_card.desc.not_set"));
 channels=info.channels()>=0?Component.translatable("gui.advanced_memory_card.extra.channel",info.channels()):null;
 hoverInfo.add(Component.literal("P2P - ").withStyle(ChatFormatting.AQUA).append(tunnel.dispName));
 hoverInfo.add(Component.translatable("gui.advanced_memory_card.pos",info.pos().getX(),info.pos().getY(),info.pos().getZ()).withStyle(ChatFormatting.YELLOW));
 hoverInfo.add(Component.translatable("gui.advanced_memory_card.side",info.facing().name()).withStyle(ChatFormatting.YELLOW));
 hoverInfo.add(Component.translatable("gui.advanced_memory_card.dim",info.dim().identifier().toString()).withStyle(ChatFormatting.YELLOW));
 hoverInfo.add(status());if(!hasChannel)hoverInfo.add(Component.translatable("gui.advanced_memory_card.p2p_status.offline").withStyle(ChatFormatting.RED));
 }
 private Component status(){return Component.translatable("gui.advanced_memory_card.p2p_status."+(error||frequency==0?"unbound":"bound")).withStyle(error||frequency==0?ChatFormatting.RED:ChatFormatting.GREEN);}
 public void setFrequency(short value){frequency=value;hoverInfo.set(4,status());}
 public short getFrequency(){return frequency;}
 public static String format4(String value){StringBuilder result=new StringBuilder();for(int i=0;i<value.length();i++){if(i!=0&&i%4==0)result.append(' ');result.append(value.charAt(i));}return result.toString();}
 @Override public int hashCode(){return loc.hashCode();}@Override public boolean equals(Object other){return other instanceof InfoWrapper wrapper&&getClass()==other.getClass()&&loc.equals(wrapper.loc);}
}
