package dev.lasm.betterp2p.client.gui.widget;
import static dev.lasm.betterp2p.client.gui.GuiHelper.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.function.Supplier;
public class WidgetTypeIcon extends AbstractWidget {
 public final Supplier<Identifier> iconSupplier;public WidgetTypeIcon(int x,int y,String tooltip,Supplier<Identifier> icon){super(x,y,18,18,Component.empty());iconSupplier=icon;setTooltip(Tooltip.create(Component.literal(tooltip)));}
 public void extractWidgetRenderState(GuiGraphicsExtractor g,int i,int j,float f){if(isHovered())g.fill(getX(),getY(),getX()+width,getY()+height,0xFF00FF00);drawBlockIcon(g,iconSupplier.get(),getX()+1,getY()+1);}
 public void updateWidgetNarration(NarrationElementOutput out){}
}
