package dev.lasm.betterp2p.client.gui;
import appeng.client.gui.widgets.ITooltip;
import appeng.client.gui.style.Blitter;
import appeng.parts.p2p.*;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.client.*;
import dev.lasm.betterp2p.client.gui.widget.*;
import dev.lasm.betterp2p.item.BetterMemoryCardModes;
import dev.lasm.betterp2p.network.data.*;
import dev.lasm.betterp2p.network.packet.*;
import dev.lasm.betterp2p.util.p2p.ClientTunnelInfo;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import java.util.*;
public class GuiAdvancedMemoryCard extends Screen implements MenuAccess<AdvancedMemoryCardMenu> {
 public static final Identifier TEXTURE=Identifier.fromNamespaceAndPath(BetterP2P.MOD_ID,"textures/gui/advanced_memory_card.png");public static final int GUI_WIDTH=288,GUI_TEX_HEIGHT=264;public static final Blitter TEXTURE_BLITTER=Blitter.texture(TEXTURE,GUI_WIDTH,GUI_TEX_HEIGHT);
 public final AdvancedMemoryCardMenu theMenu;private int ySize,leftPos,topPos;private final int tableX=9,tableY=19;private ClientTunnelInfo type;public MemoryInfo memoryInfo;public GuiScale scale;private BetterMemoryCardModes mode;public final IconButton resizeButton,refreshButton;public final P2PTypeButton typeButton;private final InfoList infos;private final WidgetTypeSelector typeSelector;public final WidgetScrollBar scrollBar=new WidgetScrollBar(tableX,tableY);public EditBox searchBar;public IconButton modeButton;public WidgetP2PColumn col;
 private final List<Component> sortRules=new ArrayList<>();private final List<List<Component>> modeDescriptions=new ArrayList<>();
 public GuiAdvancedMemoryCard(AdvancedMemoryCardMenu menu){super(Component.empty());theMenu=menu;memoryInfo=menu.memoryInfo;scale=memoryInfo.guiScale();mode=memoryInfo.mode();type=BetterP2P.proxy.getP2PFromIndex(memoryInfo.type()) instanceof ClientTunnelInfo t?t:null;resizeButton=new IconButton(scale.ordinal()*32,200,this::onResize);typeButton=new P2PTypeButton(()->type,this::onChangeType,this::openTypeSelector);refreshButton=new IconButton(160,200,this::onRefresh);infos=new InfoList(menu.infos.stream().map(InfoWrapper::new).toList(),()->searchBar.getValue(),()->mode);
 var types=new ArrayList<ClientTunnelInfo>();for(Class<?> clazz:List.of(MEP2PTunnelPart.class,FluidP2PTunnelPart.class)){var candidate=BetterP2P.proxy.getP2PFromClass(clazz);if(candidate instanceof ClientTunnelInfo t)types.add(t);}for(var tunnel:BetterP2P.proxy.getP2PTypeList())if(!types.contains(tunnel))types.add((ClientTunnelInfo)tunnel);typeSelector=new WidgetTypeSelector(0,0,this,types);
 sortRules.add(Component.translatable("gui.advanced_memory_card.sortinfo1").withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withUnderlined(true)));String[] tags={"@in","@out","@b","@u","@type=<name1>[;<name2>;]..."};ChatFormatting[] colors={ChatFormatting.BLUE,ChatFormatting.GOLD,ChatFormatting.GREEN,ChatFormatting.RED,ChatFormatting.YELLOW};for(int i=0;i<tags.length;i++)sortRules.add(Component.literal(tags[i]).withStyle(colors[i]).append(Component.literal(" - ").withStyle(ChatFormatting.GRAY).append(Component.translatable("gui.advanced_memory_card.sortinfo"+(i+2)))));sortRules.add(Component.translatable("gui.advanced_memory_card.sortinfo7").withStyle(ChatFormatting.GRAY));
 for(var m:BetterMemoryCardModes.values())modeDescriptions.add(fmtTooltips(m.unlocalizedName,40,m.unlocalizedDesc));
 }
 private class SearchBar extends EditBox implements ITooltip {SearchBar(){super(GuiAdvancedMemoryCard.this.font,0,0,100,10,Component.empty());}public List<Component> getTooltipMessage(){return new ArrayList<>(sortRules);}public Rect2i getTooltipArea(){return new Rect2i(getX(),getY(),getWidth(),getHeight());}public boolean isTooltipAreaVisible(){return visible;}}
 private void ensureWidgets(){if(searchBar==null)searchBar=new SearchBar();if(modeButton==null)modeButton=new IconButton((mode.ordinal()+3)*32,232,this::onChangeMode);if(col==null)col=new WidgetP2PColumn(this,infos,0,0,infos::getSelectedInfo,()->mode,scrollBar);}
 public Font getFont(){return font;}public AdvancedMemoryCardMenu getMenu(){return theMenu;}
 public void onRefresh(Button ignored){ClientPacketDistributor.sendToServer(new C2SRefreshP2PList(type==null?MemoryInfo.TUNNEL_ANY:type.index));}
 private void onChangeType(Button button){type=((P2PTypeButton)button).nextType(false);((P2PTypeButton)button).commitType();}
 public void onChangeMode(Button button){mode=mode.next();IconButton icon=(IconButton)button;icon.texX=(mode.ordinal()+3)*32;icon.texY=232;icon.messages=new ArrayList<>(modeDescriptions.get(mode.ordinal()));syncMemoryInfo();}
 public void onResize(Button button){scale=switch(scale){case DYNAMIC->GuiScale.LARGE;case LARGE->GuiScale.NORMAL;case NORMAL->GuiScale.SMALL;case SMALL->GuiScale.DYNAMIC;};repositionElements();}
 protected void init(){ensureWidgets();checkInfo();int h=Math.max(height,256);if(scale.minHeight>h)scale=GuiScale.DYNAMIC;int count=scale.size(height-75);ySize=count*P2PEntryConstants.HEIGHT+75+count-1;leftPos=(width-GUI_WIDTH)/2;topPos=(height-ySize)/2;searchBar.setPosition(leftPos+163,topPos+5);searchBar.setResponder(value->{infos.refresh();col.entries.forEach(WidgetP2PDevice::updateButtonVisibility);scrollBar.setHeight(count*P2PEntryConstants.HEIGHT+count-1-7);scrollBar.setRange(0,Math.max(0,infos.filtered.size()-count),23);});scrollBar.setPosition(leftPos+268,topPos+19);resizeButton.setPosition(leftPos-32,topPos+2);resizeButton.texX=scale.ordinal()*32;resizeButton.messages.set(0,Component.literal("Resize"));modeButton.setPosition(leftPos-32,topPos+34);modeButton.texX=(mode.ordinal()+3)*32;modeButton.messages=new ArrayList<>(modeDescriptions.get(mode.ordinal()));typeButton.setPosition(leftPos-32,topPos+66);refreshButton.setPosition(leftPos-32,topPos+98);refreshButton.messages.set(0,Component.literal("Refresh"));col.resize(scale,h-75);col.setPosition(leftPos+tableX,topPos+tableY);infos.select(memoryInfo.selectedEntry().orElse(null));infos.refresh();scrollBar.setHeight(count*P2PEntryConstants.HEIGHT+count-1-7);scrollBar.setRange(0,Math.max(0,infos.filtered.size()-count),23);col.entries.forEach(WidgetP2PDevice::updateButtonVisibility);typeSelector.parent=typeButton;typeSelector.visible=false;checkInfo();refreshOverlay();selectInfo(memoryInfo.selectedEntry().orElse(null));col.visitWidgets(this::addRenderableWidget);addRenderableWidget(refreshButton);addRenderableWidget(scrollBar);addRenderableWidget(typeButton);addRenderableWidget(resizeButton);addRenderableWidget(modeButton);addRenderableWidget(searchBar);addRenderableWidget(typeSelector);}
 public boolean isPauseScreen(){return false;}
 private void drawPanel(GuiGraphicsExtractor g){g.blit(RenderPipelines.GUI_TEXTURED,TEXTURE,leftPos,topPos,0f,0f,GUI_WIDTH,60,288,264);for(int i=0;i<scale.size(ySize-75)-2;i++)g.blit(RenderPipelines.GUI_TEXTURED,TEXTURE,leftPos,topPos+60+(P2PEntryConstants.HEIGHT+1)*i,0f,60f,GUI_WIDTH,42,288,264);g.blit(RenderPipelines.GUI_TEXTURED,TEXTURE,leftPos,topPos+ySize-98,0f,102f,GUI_WIDTH,98,288,264);}
 public boolean mouseScrolled(double x,double y,double sx,double sy){return scrollBar.mouseScrolled(x,y,sx,sy)||super.mouseScrolled(x,y,sx,sy);}
 public void extractRenderState(GuiGraphicsExtractor g,int x,int y,float partial){drawPanel(g);g.text(font,Component.translatable("item.betterp2p.advanced_memory_card"),leftPos+tableX,topPos+6,0xFF404040,false);super.extractRenderState(g,x,y,partial);renderTooltips(g,x,y);}
 private void syncMemoryInfo(){InfoWrapper selected=infos.getSelectedInfo();ClientPacketDistributor.sendToServer(new C2SUpdateMemoryInfo(new MemoryInfo(Optional.ofNullable(infos.selectedEntry),selected==null?(short)0:selected.frequency,mode,scale,type==null?MemoryInfo.TUNNEL_ANY:type.index)));}
 private void checkInfo(){for(var info:infos.filtered)info.error=info.frequency!=0&&(info.output?col.findInput(info.frequency)==null:col.findOutput(info.frequency)==null);}
 public void refreshInfo(List<P2PInfo> list){infos.rebuild(list.stream().map(InfoWrapper::new).toList(),scrollBar,scale.size(height-75));checkInfo();refreshOverlay();}public void updateInfo(List<P2PInfo> list){infos.update(list.stream().map(InfoWrapper::new).toList(),scrollBar,scale.size(height-75));checkInfo();refreshOverlay();}
 private void renderTooltips(GuiGraphicsExtractor g,int x,int y){for(var child:children())if(child instanceof ITooltip tooltip&&tooltip.isTooltipAreaVisible()){Rect2i area=tooltip.getTooltipArea();if(x>=area.getX()&&y>=area.getY()&&x<area.getX()+area.getWidth()&&y<area.getY()+area.getHeight())drawTooltipWithHeader(g,x,y,tooltip.getTooltipMessage());}}
 public void onClose(){ClientCache.searchText=searchBar.getValue();col.onGuiClosed();syncMemoryInfo();ClientPacketDistributor.sendToServer(new C2SCloseGui());super.onClose();}
 public void drawTooltip(GuiGraphicsExtractor g,int x,int y,List<Component> lines){if(lines.isEmpty())return;List<FormattedCharSequence> styled=new ArrayList<>();for(Component line:lines)styled.addAll(ComponentRenderUtils.wrapComponents(line,width/2-40,font));g.setTooltipForNextFrame(styled,x,y);}
 public void drawTooltipWithHeader(GuiGraphicsExtractor g,int x,int y,List<Component> lines){List<Component> formatted=new ArrayList<>();for(int i=0;i<lines.size();i++){final boolean first=i==0;formatted.add(lines.get(i).copy().withStyle(s->first?s.withColor(ChatFormatting.WHITE):s.getColor()!=null?s:s.withColor(ChatFormatting.GRAY)));}drawTooltip(g,x,y,formatted);}
 private void refreshOverlay(){InfoWrapper selected=infos.getSelectedInfo();ClientCache.selectedPosition=selected==null?null:selected.loc.pos();ClientCache.selectedFacing=selected==null?null:selected.loc.facing();ClientCache.positions.clear();if(selected==null||minecraft==null||minecraft.player==null)return;for(var info:infos.sorted){if(info.frequency==selected.frequency&&!info.equals(selected)&&info.loc.dim().equals(minecraft.player.level().dimension())&&info.loc.pos().distSqr(minecraft.player.blockPosition())<50){ClientCache.positions.add(new ClientCache.Position(info.loc.pos(),info.loc.facing()));if(ClientCache.positions.size()==200)break;}}}
 public void openTypeSelector(ITypeReceiver parent,boolean any){typeSelector.parent=parent;typeSelector.setPosition(parent.getX()+(parent instanceof WidgetP2PDevice?20:parent.getWidth()),parent.getY());typeSelector.useAny=any;typeSelector.visible=true;}
 public void openTypeSelector(Button ignored){openTypeSelector(typeButton,true);}
 public boolean mouseClicked(MouseButtonEvent e,boolean twice){if(typeSelector.mouseClicked(e,twice))return true;closeTypeSelector(type);return super.mouseClicked(e,twice);}
 public void closeTypeSelector(ClientTunnelInfo type){if(this.type!=type){this.type=type;ClientPacketDistributor.sendToServer(new C2SRefreshP2PList(type==null?MemoryInfo.TUNNEL_ANY:type.index));}typeSelector.visible=false;}
 public int getTypeID(){return 0;}public void selectInfo(P2PLocation location){infos.select(location);syncMemoryInfo();refreshOverlay();}
 public static List<Component> fmtTooltips(String title,int maxChars,String... keys){List<Component> result=new ArrayList<>();result.add(Component.translatable(title));for(String key:keys){String[] words=I18n.get(key).split(" ",-1);int i=0;if(key.length()<maxChars)result.add(Component.literal(key));while(i<words.length){StringBuilder text=new StringBuilder();while(text.length()<maxChars){text.append(words[i++]);if(i>=words.length)break;text.append(' ');}var component=Component.literal(text.toString());result.add(text.toString().startsWith("§")?component:component.withStyle(ChatFormatting.GRAY));}}return result;}
}
