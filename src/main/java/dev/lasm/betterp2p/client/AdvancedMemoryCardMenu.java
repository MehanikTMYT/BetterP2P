package dev.lasm.betterp2p.client;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.network.data.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import java.util.List;
public class AdvancedMemoryCardMenu extends AbstractContainerMenu {
 public List<P2PInfo> infos=List.of(); public MemoryInfo memoryInfo=new MemoryInfo();
 public AdvancedMemoryCardMenu(int id, Inventory inventory){super(BetterP2P.ADVANCED_MEMORY_CARD_MENU.get(),id);}
 public ItemStack quickMoveStack(Player player,int slot){return ItemStack.EMPTY;}
 public boolean stillValid(Player player){return true;}
 public DataSlot addDataSlot(DataSlot slot){return super.addDataSlot(slot);}
 public List<P2PInfo> getInfos(){return infos;} public void setInfos(List<P2PInfo> v){infos=v;}
 public MemoryInfo getMemoryInfo(){return memoryInfo;} public void setMemoryInfo(MemoryInfo v){memoryInfo=v;}
}
