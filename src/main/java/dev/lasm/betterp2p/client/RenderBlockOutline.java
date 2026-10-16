package dev.lasm.betterp2p.client;
import appeng.parts.BusCollisionHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lasm.betterp2p.item.ItemAdvancedMemoryCard;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.*;
public final class RenderBlockOutline {
 private RenderBlockOutline(){}
 public static void showPartPlacementPreview(Player player,PoseStack stack,MultiBufferSource buffers,Vec3 camera){if(player==null||!(player.getMainHandItem().getItem() instanceof ItemAdvancedMemoryCard))return;if(ClientCache.selectedPosition!=null)renderPart(stack,buffers,camera,ClientCache.selectedPosition,ClientCache.selectedFacing,0x45,0xDA,0x75);for(var entry:List.copyOf(ClientCache.positions))renderPart(stack,buffers,camera,entry.pos(),entry.facing(),0x66,0xCC,0xFF);}
 private static void renderPart(PoseStack stack,MultiBufferSource buffers,Vec3 camera,BlockPos pos,net.minecraft.core.Direction side,int r,int g,int b){var boxes=new ArrayList<AABB>();var helper=new BusCollisionHelper(boxes,side,true);helper.addBox(5,5,12,11,11,13);helper.addBox(3,3,13,13,13,14);helper.addBox(2,2,14,14,14,16);renderBoxes(stack,buffers,camera,pos,boxes,r,g,b,true);}
 public static void renderBoxes(PoseStack stack,MultiBufferSource buffers,Vec3 camera,BlockPos pos,List<AABB> boxes,int red,int green,int blue,boolean insideBlock){var buffer=buffers.getBuffer(insideBlock?RenderTypes.linesTranslucent():RenderTypes.lines());int alpha=(int)((insideBlock?0.6f:0.8f)*255);for(AABB box:boxes){double x=pos.getX()-camera.x,y=pos.getY()-camera.y,z=pos.getZ()-camera.z;var pose=stack.last();Shapes.create(box).forAllEdges((k,l,m,n,o,p)->{float q=(float)(n-k),r=(float)(o-l),s=(float)(p-m);float length=net.minecraft.util.Mth.sqrt(q*q+r*r+s*s);q/=length;r/=length;s/=length;buffer.addVertex(pose.pose(),(float)(k+x),(float)(l+y),(float)(m+z)).setColor(red,green,blue,alpha).setNormal(pose,q,r,s).setLineWidth(2);buffer.addVertex(pose.pose(),(float)(n+x),(float)(o+y),(float)(p+z)).setColor(red,green,blue,alpha).setNormal(pose,q,r,s).setLineWidth(2);});}}
}
