package dev.lasm.betterp2p.client.gui.widget;
import appeng.client.gui.widgets.ITooltip;
import static dev.lasm.betterp2p.client.gui.GuiHelper.*;
import dev.lasm.betterp2p.client.gui.InfoWrapper;
import dev.lasm.betterp2p.item.BetterMemoryCardModes;
import dev.lasm.betterp2p.network.packet.C2SChangeP2PType;
import dev.lasm.betterp2p.util.p2p.ClientTunnelInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.*;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;
import java.util.*;
import java.util.function.*;
public class WidgetP2PDevice extends AbstractWidget implements ITypeReceiver,ITooltip {
 private final int index;private final Supplier<InfoWrapper> selectedInfo;public final Supplier<BetterMemoryCardModes> modeSupplier;public final Supplier<InfoWrapper> infoSupplier;public final WidgetP2PColumn col;public boolean renderNameTextfield=true;public final Button bindButton,unbindButton;
 public WidgetP2PDevice(int index,Supplier<InfoWrapper> selected,Supplier<BetterMemoryCardModes> mode,Supplier<InfoWrapper> info,WidgetP2PColumn col,int x,int y){super(x,y,P2PEntryConstants.WIDTH,P2PEntryConstants.HEIGHT,Component.empty());this.index=index;selectedInfo=selected;modeSupplier=mode;infoSupplier=info;this.col=col;bindButton=Button.builder(Component.translatable("gui.advanced_memory_card.bind"),b->col.onBindButtonClicked(b,infoSupplier.get())).size(56,20).build();unbindButton=Button.builder(Component.translatable("gui.advanced_memory_card.unbind"),b->col.onUnbindButtonClicked(b,infoSupplier.get())).size(56,20).build();}
 public void updateButtonVisibility(){InfoWrapper info=infoSupplier.get(),selected=selectedInfo.get();BetterMemoryCardModes mode=modeSupplier.get();if(info==null||selected==null||mode==BetterMemoryCardModes.COPY&&((!info.output&&info.frequency!=0)||selected.output)){bindButton.visible=false;unbindButton.visible=false;}else if(mode==BetterMemoryCardModes.UNBIND){bindButton.visible=false;unbindButton.visible=info.frequency!=0;unbindButton.active=unbindButton.visible;}else{bindButton.visible=!info.loc.equals(selected.loc)&&(selected.frequency==0||info.frequency!=selected.frequency);bindButton.active=bindButton.visible;unbindButton.visible=false;}}
 public void updateWidgetNarration(NarrationElementOutput out){}
 public void extractWidgetRenderState(GuiGraphicsExtractor g,int mx,int my,float partial){InfoWrapper info=infoSupplier.get(),selected=selectedInfo.get();if(info==null)return;int x=getX(),y=getY();Integer color=selected!=null&&selected.loc.equals(info.loc)?P2PEntryConstants.SELECTED_COLOR:info.error?P2PEntryConstants.ERROR_COLOR:!info.hasChannel&&info.frequency!=0?P2PEntryConstants.INACTIVE_COLOR:selected!=null&&selected.frequency==info.frequency&&info.frequency!=0?P2PEntryConstants.OUTPUT_COLOR:null;if(color!=null)g.fill(x,y,x+P2PEntryConstants.WIDTH,y+P2PEntryConstants.HEIGHT,color);if(overName(mx,my))g.fill(x+50,y+1,x+210,y+13,0x6E000000);
 drawBlockIcon(g,info.icon,info.overlay,x+3,y+3);drawIcon(g,info.output?144:128,200,x,y+4);drawIcon(g,info.error||info.frequency==0||!info.hasChannel?144:128,216,x+3,y+20);Font font=Minecraft.getInstance().font;int left=x+P2PEntryConstants.LEFT_ALIGN;g.text(font,Component.translatable("gui.advanced_memory_card.name",renderNameTextfield?info.name:""),left,y+2,0xFF404040,false);g.text(font,info.description,left,y+12,0xFF404040,false);g.text(font,info.freqDisplay,left,y+22,0xFF404040,false);if(info.channels!=null)g.text(font,info.channels,left,y+32,0xFF404040,false);updateButtonVisibility();}
 private boolean overName(double x,double y){return isHovered()&&x>getX()+50&&x<getX()+210&&y>getY()+1&&y<getY()+14;}
 public boolean clicked(double x,double y){return isClicked(bindButton,x,y)||isClicked(unbindButton,x,y)||active&&visible&&isMouseOver(x,y);}
 public boolean mouseClicked(MouseButtonEvent e,boolean twice){if(clicked(e.x(),e.y())&&e.button()==GLFW.GLFW_MOUSE_BUTTON_RIGHT){playButtonClickSound(Minecraft.getInstance().getSoundManager());col.gui.openTypeSelector(this,false);return true;}return bindButton.mouseClicked(e,twice)||unbindButton.mouseClicked(e,twice)||super.mouseClicked(e,twice);}
 public boolean keyPressed(KeyEvent e){if(col.getRenameBar().visible){col.getRenameBar().setFocused(true);if(e.key()==GLFW.GLFW_KEY_ENTER)col.finishRename();else return col.getRenameBar().keyPressed(e);return true;}return super.keyPressed(e);}
 public boolean charTyped(CharacterEvent e){if(col.getRenameBar().visible){col.getRenameBar().setFocused(true);return col.getRenameBar().charTyped(e);}return super.charTyped(e);}
 public void onClick(MouseButtonEvent e,boolean twice){InfoWrapper info=infoSupplier.get();if(info==null)return;if(overName(e.x(),e.y()))col.onRenameButtonClicked(info,index);else{col.gui.selectInfo(info.loc);col.finishRename();}}
 public void visitWidgets(Consumer<AbstractWidget> consumer){super.visitWidgets(consumer);bindButton.setPosition(getX()+190,getY()+14);bindButton.setWidth(56);unbindButton.setPosition(getX()+190,getY()+14);unbindButton.setWidth(56);consumer.accept(bindButton);consumer.accept(unbindButton);}
 public void accept(ClientTunnelInfo type){ClientPacketDistributor.sendToServer(new C2SChangeP2PType(type==null?dev.lasm.betterp2p.network.data.MemoryInfo.TUNNEL_ANY:type.index,infoSupplier.get().loc));col.gui.closeTypeSelector(type);}
 public List<Component> getTooltipMessage(){return new ArrayList<>(infoSupplier.get().hoverInfo);}public Rect2i getTooltipArea(){return new Rect2i(getX(),getY(),20,height);}public boolean isTooltipAreaVisible(){return visible&&infoSupplier.get()!=null;}
}
