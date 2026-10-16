package dev.lasm.betterp2p.client.gui.widget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
public class WidgetScrollBar extends AbstractWidget {
 public int pageSize=1,maxScroll,minScroll,currentScroll;public Runnable onScroll=()->{};
 public static final Identifier SCROLLER=Identifier.withDefaultNamespace("container/creative_inventory/scroller"),SCROLLER_DISABLED=Identifier.withDefaultNamespace("container/creative_inventory/scroller_disabled");
 public WidgetScrollBar(int x,int y){super(x,y,12,15,Component.empty());}
 public void extractWidgetRenderState(GuiGraphicsExtractor g,int x,int y,float partial){int offset=getRange()==0?0:(currentScroll-minScroll)*(height-15)/getRange();g.blitSprite(RenderPipelines.GUI_TEXTURED,getRange()==0?SCROLLER_DISABLED:SCROLLER,getX(),getY()+offset,12,15);}
 private int getRange(){return maxScroll-minScroll;}public void setRange(int min,int max,int pageSize){minScroll=min;maxScroll=Math.max(min,max);this.pageSize=pageSize;applyRange();}
 private void applyRange(){currentScroll=Math.clamp(currentScroll,minScroll,maxScroll);onScroll.run();}
 public void onDrag(MouseButtonEvent event,double dx,double dy){if(getRange()==0)return;currentScroll=(int)(event.y()-getY());currentScroll=minScroll+currentScroll*2*getRange()/height;currentScroll=(currentScroll+1)>>1;applyRange();super.onDrag(event,dx,dy);}
 public boolean mouseScrolled(double x,double y,double sx,double sy){currentScroll+=Math.clamp(-(int)sy,-1,1);applyRange();return true;}
 public void updateWidgetNarration(NarrationElementOutput out){}
}
