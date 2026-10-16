package dev.lasm.betterp2p.network.packet;
import dev.lasm.betterp2p.client.AdvancedMemoryCardMenu;
import dev.lasm.betterp2p.client.gui.GuiAdvancedMemoryCard;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;
/** Kept separate so payload codec initialization does not load client screens. */
final class ClientHandlers {
 private ClientHandlers() {}
 static void openGui(S2COpenGui message,IPayloadContext ctx){
  var minecraft=Minecraft.getInstance();
  if(minecraft.screen instanceof GuiAdvancedMemoryCard gui){gui.refreshInfo(message.infos());gui.memoryInfo=message.memoryInfo();}
  else minecraft.submit(()->{var menu=new AdvancedMemoryCardMenu(0,null);menu.memoryInfo=message.memoryInfo();menu.infos=message.infos();minecraft.setScreen(new GuiAdvancedMemoryCard(menu));});
 }
 static void updateP2P(S2CUpdateP2P message,IPayloadContext ctx){Minecraft.getInstance().submit(()->{if(Minecraft.getInstance().screen instanceof GuiAdvancedMemoryCard gui){if(message.clear())gui.refreshInfo(message.infos());else gui.updateInfo(message.infos());}});}
}
