package dev.lasm.betterp2p.client.gui.widget;
import java.util.function.IntUnaryOperator;
public enum GuiScale {
 SMALL(h->4,0,"small"),NORMAL(h->6,326,"normal"),LARGE(h->8,409,"large"),DYNAMIC(h->h/P2PEntryConstants.HEIGHT,0,"dynamic");
 public final IntUnaryOperator size;public final int minHeight;public final String unlocalizedName;
 GuiScale(IntUnaryOperator size,int minHeight,String name){this.size=size;this.minHeight=minHeight;this.unlocalizedName="gui.advanced_memory_card.gui_scale."+name;}
 public int size(int height){return size.applyAsInt(height);}
}
