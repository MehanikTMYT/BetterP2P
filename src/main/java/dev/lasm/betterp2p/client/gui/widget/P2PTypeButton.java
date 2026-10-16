package dev.lasm.betterp2p.client.gui.widget;
import static dev.lasm.betterp2p.client.gui.GuiHelper.*;
import appeng.parts.p2p.*;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.client.gui.GuiAdvancedMemoryCard;
import dev.lasm.betterp2p.network.packet.C2SRefreshP2PList;
import dev.lasm.betterp2p.util.p2p.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import java.util.*;
import java.util.function.Supplier;
public class P2PTypeButton extends IconButton implements ITypeReceiver {
 public final Supplier<ClientTunnelInfo> type;private final OnPress secondary;public final List<TunnelInfo> types;public int index;private final ClientTunnelInfo me,fluid,redstone;
 public P2PTypeButton(Supplier<ClientTunnelInfo> type,OnPress press,OnPress secondary){super(0,0,press);this.type=type;this.secondary=secondary;updateHoverText();types=BetterP2P.proxy.getP2PTypeList();index=type.get()==null?types.size():types.stream().filter(t->t.index==type.get().index).findFirst().orElseThrow().index;me=(ClientTunnelInfo)BetterP2P.proxy.getP2PFromClass(MEP2PTunnelPart.class);fluid=(ClientTunnelInfo)BetterP2P.proxy.getP2PFromClass(FluidP2PTunnelPart.class);redstone=(ClientTunnelInfo)BetterP2P.proxy.getP2PFromClass(RedstoneP2PTunnelPart.class);}
 public ClientTunnelInfo nextType(boolean reverse){index=(index+(reverse?-1:1))%(types.size()+1);return index>=0&&index<types.size()?(ClientTunnelInfo)types.get(index):null;}
 public boolean mouseClicked(MouseButtonEvent event,boolean twice){if(!active||!visible)return false;if(isMouseOver(event.x(),event.y())){if(event.button()==0){playButtonClickSound(Minecraft.getInstance().getSoundManager());onClick(event,twice);return true;}if(event.button()==1){playButtonClickSound(Minecraft.getInstance().getSoundManager());secondary.onPress(this);return true;}}return false;}
 public void extractContents(GuiGraphicsExtractor g,int mx,int my,float p){renderBackground(g,my,my,p);ClientTunnelInfo selected=type.get();if(selected!=null)drawBlockIcon(g,selected.icon.get(),getX()+2,getY()+2,28,28);else{drawBlockIcon(g,redstone.icon.get(),getX()+12,getY()+12,18,18);drawBlockIcon(g,fluid.icon.get(),getX()+7,getY()+7,18,18);drawBlockIcon(g,me.icon.get(),getX()+2,getY()+2,18,18);}}
 private void updateHoverText(){messages.set(0,Component.translatable("gui.advanced_memory_card.types.filtered",type.get()==null?Component.translatable("gui.advanced_memory_card.types.any"):type.get().stack.getDisplayName().copy().withStyle(ChatFormatting.GREEN)));}
 public void commitType(){updateHoverText();ClientPacketDistributor.sendToServer(new C2SRefreshP2PList(type.get()==null?dev.lasm.betterp2p.network.data.MemoryInfo.TUNNEL_ANY:type.get().index));playDownSound(Minecraft.getInstance().getSoundManager());}
 public void accept(ClientTunnelInfo type){if(Minecraft.getInstance().screen instanceof GuiAdvancedMemoryCard screen)screen.closeTypeSelector(type);}
}
