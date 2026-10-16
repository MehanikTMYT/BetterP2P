package dev.lasm.betterp2p.client.gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
public final class GuiHelper {
 private GuiHelper(){} public static final Identifier OVERLAY=Identifier.fromNamespaceAndPath("ae2","textures/part/p2p_tunnel_front.png");
 public static void drawTexturedQuad(GuiGraphicsExtractor g,Identifier t,float x0,float y0,float x1,float y1,float u0,float v0,float u1,float v1){g.blit(t,(int)x0,(int)y0,(int)x1,(int)y1,u0,u1,v0,v1);}
 public static void drawIcon(GuiGraphicsExtractor g,int sx,int sy,int x,int y){drawIcon(g,sx,sy,x,y,16,16);}
 public static void drawIcon(GuiGraphicsExtractor g,int sx,int sy,int x,int y,int w,int h){g.blit(RenderPipelines.GUI_TEXTURED,GuiAdvancedMemoryCard.TEXTURE,x,y,(float)sx,(float)sy,w,h,288,264);}
 public static boolean isClicked(AbstractWidget w,double x,double y){return w.isActive()&&w.visible&&x>=w.getX()&&y>=w.getY()&&x<w.getX()+w.getWidth()&&y<w.getY()+w.getHeight();}
 public static void drawBlockIcon(GuiGraphicsExtractor g,Identifier icon,int x,int y){drawBlockIcon(g,icon,OVERLAY,x,y,16,16);}
 public static void drawBlockIcon(GuiGraphicsExtractor g,Identifier icon,int x,int y,int w,int h){drawBlockIcon(g,icon,OVERLAY,x,y,w,h);}
 public static void drawBlockIcon(GuiGraphicsExtractor g,Identifier icon,Identifier overlay,int x,int y){drawBlockIcon(g,icon,overlay,x,y,16,16);}
 public static void drawBlockIcon(GuiGraphicsExtractor g,Identifier icon,Identifier overlay,int x,int y,int w,int h){drawTexturedQuad(g,icon,x+2,y+2,x+w-2,y+h-2,0,0,1,1);drawTexturedQuad(g,overlay,x,y,x+w,y+h,0,0,1,1);}
}
