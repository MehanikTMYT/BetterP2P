package dev.lasm.betterp2p.client.gui.widget;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.client.gui.*;
import dev.lasm.betterp2p.item.BetterMemoryCardModes;
import dev.lasm.betterp2p.network.packet.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import java.util.*;
import java.util.function.*;
public class WidgetP2PColumn extends AbstractWidget {
 public final GuiAdvancedMemoryCard gui;private final InfoList infos;private final Supplier<InfoWrapper> selectedInfo;public final Supplier<BetterMemoryCardModes> mode;private final WidgetScrollBar scrollBar;public final List<WidgetP2PDevice> entries=new ArrayList<>();private final RenameBar renameBar;
 public class RenameBar extends EditBox {public InfoWrapper info;RenameBar(){super(gui.getFont(),0,0,160,12,Component.empty());}}
 public WidgetP2PColumn(GuiAdvancedMemoryCard gui,InfoList infos,int x,int y,Supplier<InfoWrapper> selected,Supplier<BetterMemoryCardModes> mode,WidgetScrollBar bar){super(x,y,160,0,Component.empty());this.gui=gui;this.infos=infos;selectedInfo=selected;this.mode=mode;scrollBar=bar;renameBar=new RenameBar();renameBar.setMaxLength(50);renameBar.visible=false;renameBar.setCanLoseFocus(true);}
 public RenameBar getRenameBar(){return renameBar;}
 public void resize(GuiScale scale,int height){entries.clear();for(int i=0;i<scale.size(height);i++){final int index=i;entries.add(new WidgetP2PDevice(i,selectedInfo,mode,()->{int n=index+scrollBar.currentScroll;return n>=0&&n<infos.filtered.size()?infos.filtered.get(n):null;},this,getX(),getY()+i*(P2PEntryConstants.HEIGHT+1)));}}
 public void setPosition(int x,int y){super.setPosition(x,y);for(int i=0;i<entries.size();i++)entries.get(i).setPosition(x,y+i*(P2PEntryConstants.HEIGHT+1));}
 public void extractWidgetRenderState(GuiGraphicsExtractor g,int i,int j,float f){}public void updateWidgetNarration(NarrationElementOutput out){}
 public void finishRename(){if(!renameBar.visible)return;entries.forEach(w->w.renderNameTextfield=true);if(renameBar.info!=null&&!renameBar.getValue().isEmpty()&&!renameBar.info.name.equals(renameBar.getValue())){InfoWrapper info=renameBar.info;renameBar.setValue(renameBar.getValue().trim());ClientPacketDistributor.sendToServer(new C2SRenameP2P(info.loc,renameBar.getValue()));}renameBar.visible=false;renameBar.setValue("");renameBar.setFocused(false);renameBar.info=null;gui.setFocused(this);}
 public void onRenameButtonClicked(InfoWrapper info,int index){renameBar.visible=true;renameBar.setPosition(getX()+50,getY()+index*(P2PEntryConstants.HEIGHT+1)+1);renameBar.setValue(info.name);renameBar.setFocused(true);renameBar.setCursorPosition(0);renameBar.info=info;gui.setFocused(renameBar);}
 public void onClick(MouseButtonEvent event,boolean twice){if(renameBar.visible)finishRename();super.onClick(event,twice);}
 public void onBindButtonClicked(Button button,InfoWrapper info){button.active=false;if(infos.selectedEntry==null)return;switch(mode.get()){
 case INPUT->{BetterP2P.logger.debug("Bind {} as input",info.loc);ClientPacketDistributor.sendToServer(new C2SLinkP2P(info.loc,infos.selectedEntry));}
 case OUTPUT->{BetterP2P.logger.debug("Bind {} as output",info.loc);ClientPacketDistributor.sendToServer(new C2SLinkP2P(infos.selectedEntry,info.loc));}
 case COPY->{InfoWrapper selected=infos.getSelectedInfo();InfoWrapper input=findInput(selected==null?null:selected.frequency);if(input!=null)ClientPacketDistributor.sendToServer(new C2SLinkP2P(input.loc,info.loc));}
 default->BetterP2P.logger.debug("Somehow bind button was pressed while in UNBIND mode.");}gui.onRefresh(null);}
 public void onUnbindButtonClicked(Button button,InfoWrapper info){button.active=false;if(info.frequency!=0){ClientPacketDistributor.sendToServer(new C2SUnlinkP2P(info.loc,gui.getTypeID()));info.setFrequency((short)0);}gui.onRefresh(null);}
 public InfoWrapper findInput(Short frequency){return infos.filtered.stream().filter(i->frequency!=null&&i.frequency==frequency&&!i.output).findFirst().orElse(null);}
 public InfoWrapper findOutput(Short frequency){return infos.filtered.stream().filter(i->frequency!=null&&i.frequency==frequency&&i.output).findFirst().orElse(null);}
 public void visitWidgets(Consumer<AbstractWidget> consumer){super.visitWidgets(consumer);entries.forEach(e->e.visitWidgets(consumer));consumer.accept(renameBar);}public void onGuiClosed(){finishRename();}
}
