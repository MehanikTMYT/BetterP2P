package dev.lasm.betterp2p.client.gui.widget;
import static dev.lasm.betterp2p.client.gui.GuiHelper.*;
import dev.lasm.betterp2p.client.gui.GuiAdvancedMemoryCard;
import dev.lasm.betterp2p.util.p2p.ClientTunnelInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.*;
public class WidgetTypeSelector extends AbstractWidget {
 public static final int ICONS_PER_ROW=5;public final GuiAdvancedMemoryCard gui;public final List<ClientTunnelInfo> p2pTypes;public int hoveredIdx;public boolean useAny;public ITypeReceiver parent;private final List<List<Component>> translated=new ArrayList<>();
 public WidgetTypeSelector(int x,int y,GuiAdvancedMemoryCard gui,List<ClientTunnelInfo> types){super(x,y,Math.min(ICONS_PER_ROW,types.size())*18+8,(types.size()+ICONS_PER_ROW-1)*18/ICONS_PER_ROW+8,Component.empty());this.gui=gui;p2pTypes=types;types.forEach(t->translated.add(List.of(t.dispName)));translated.add(List.of(Component.translatable("gui.advanced_memory_card.types.any")));}
 public void setFocused(boolean focused){super.setFocused(focused);if(!focused)visible=false;}
 public void onClick(MouseButtonEvent event,boolean twice){if(hoveredIdx!=-1&&parent!=null)parent.accept(hoveredIdx<p2pTypes.size()?p2pTypes.get(hoveredIdx):null);super.onClick(event,twice);}
 public void extractWidgetRenderState(GuiGraphicsExtractor g,int mx,int my,float partial){g.fill(getX(),getY(),getX()+width,getY()+height,0xAA000000);hoveredIdx=-1;for(int i=0;i<p2pTypes.size()+(useAny?1:0);i++){int x=getX()+4+i%ICONS_PER_ROW*18,y=getY()+4+i/ICONS_PER_ROW*18;if(mx>x&&mx<x+18&&my>y&&my<y+18){hoveredIdx=i;g.fill(x,y,x+18,y+18,0xFF00FF00);}boolean any=i==p2pTypes.size();drawBlockIcon(g,any?Identifier.withDefaultNamespace("textures/block/coal_block.png"):p2pTypes.get(i).icon.get(),x+1,y+1);if(any)g.text(Minecraft.getInstance().font,"?",x+6,y+6,0xFFFF0000,false);}if(hoveredIdx!=-1)gui.drawTooltip(g,mx,my,translated.get(hoveredIdx));}
 public void updateWidgetNarration(NarrationElementOutput out){}
}
