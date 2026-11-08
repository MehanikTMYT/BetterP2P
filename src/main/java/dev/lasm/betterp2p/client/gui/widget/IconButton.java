package dev.lasm.betterp2p.client.gui.widget;
import appeng.client.gui.widgets.ITooltip;
import static dev.lasm.betterp2p.client.gui.GuiAdvancedMemoryCard.*;
import static dev.lasm.betterp2p.client.gui.GuiHelper.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import java.util.*;
public class IconButton extends Button implements ITooltip {
 public int texX,texY;public List<Component> messages=new ArrayList<>(List.of(getMessage()));
 public IconButton(int texX,int texY,OnPress press){super(0,0,32,32,Component.empty(),press,DEFAULT_NARRATION);this.texX=texX;this.texY=texY;}
 public void extractContents(GuiGraphicsExtractor g,int mouseX,int mouseY,float partial){renderBackground(g,mouseY,mouseY,partial);drawTexturedQuad(g,TEXTURE,getX()+1,getY()+1,getX()+width-1,getY()+height-1,texX/(float)GUI_WIDTH,texY/(float)GUI_TEX_HEIGHT,(texX+width)/(float)GUI_WIDTH,(texY+height)/(float)GUI_TEX_HEIGHT);}
 public int getHoverState(){return !active?0:isHovered()?2:1;}
 public void renderBackground(GuiGraphicsExtractor g,int x,int y,float partial){int k=getHoverState();drawTexturedQuad(g,TEXTURE,getX(),getY(),getX()+width,getY()+height,32f*k/GUI_WIDTH,232f/GUI_TEX_HEIGHT,32f*(k+1)/GUI_WIDTH,(232f+height)/GUI_TEX_HEIGHT);}
 public List<Component> getTooltipMessage(){return messages;}public Rect2i getTooltipArea(){return new Rect2i(getX(),getY(),width,height);}public boolean isTooltipAreaVisible(){return visible;}
}
