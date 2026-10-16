package dev.lasm.betterp2p.network;
import appeng.api.networking.IGrid;
import dev.lasm.betterp2p.client.AdvancedMemoryCardMenu;
import dev.lasm.betterp2p.network.data.GridServerCache;
import dev.lasm.betterp2p.network.data.MemoryInfo;
import dev.lasm.betterp2p.network.packet.*;
import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
public final class ModNetwork {
 public static final long NETWORK_CD=200L;
 public static final Map<UUID,PlayerRequest> playerState=Collections.synchronizedMap(new WeakHashMap<>());
 private ModNetwork() {}
 public static void registerNetwork(RegisterPayloadHandlersEvent event){
  var reg=event.registrar("1").executesOn(HandlerThread.MAIN);
  reg.playToClient(S2COpenGui.TYPE,S2COpenGui.STREAM_CODEC,S2COpenGui::handle);
  reg.playToClient(S2CUpdateP2P.TYPE,S2CUpdateP2P.STREAM_CODEC,S2CUpdateP2P::handle);
  reg.playToServer(C2SLinkP2P.TYPE,C2SLinkP2P.STREAM_CODEC,C2SLinkP2P::handle);
  reg.playToServer(C2SCloseGui.TYPE,C2SCloseGui.STREAM_CODEC,C2SCloseGui::handle);
  reg.playToServer(C2SUpdateMemoryInfo.TYPE,C2SUpdateMemoryInfo.STREAM_CODEC,C2SUpdateMemoryInfo::handle);
  reg.playToServer(C2SRenameP2P.TYPE,C2SRenameP2P.STREAM_CODEC,C2SRenameP2P::handle);
  reg.playToServer(C2SRefreshP2PList.TYPE,C2SRefreshP2PList.STREAM_CODEC,C2SRefreshP2PList::handle);
  reg.playToServer(C2SUnlinkP2P.TYPE,C2SUnlinkP2P.STREAM_CODEC,C2SUnlinkP2P::handle);
  reg.playToServer(C2SChangeP2PType.TYPE,C2SChangeP2PType.STREAM_CODEC,C2SChangeP2PType::handle);
 }
 public static void requestP2PList(Player player,int type){
  var state=playerState.get(player.getUUID());if(state==null)return;var cache=state.gridCache;cache.type=type;
  if(state.updateReady+NETWORK_CD<System.currentTimeMillis()){
   PacketDistributor.sendToPlayer((ServerPlayer)player,new S2CUpdateP2P(cache.retrieveP2PList(),true));state.updateReady=System.currentTimeMillis()+NETWORK_CD;
  }else if(!state.updatePending){state.updatePending=true;PacketDistributor.sendToPlayer((ServerPlayer)player,new S2CUpdateP2P(cache.retrieveP2PList(),true));state.updatePending=false;}
 }
 public static void requestP2PUpdate(Player player){
  var state=playerState.get(player.getUUID());if(state==null)return;var cache=state.gridCache;
  if(state.updateReady+NETWORK_CD<System.currentTimeMillis()){
   PacketDistributor.sendToPlayer((ServerPlayer)player,new S2CUpdateP2P(cache.getP2PUpdates()));state.updateReady=System.currentTimeMillis()+NETWORK_CD;
  }else if(!state.updatePending){state.updatePending=true;PacketDistributor.sendToPlayer((ServerPlayer)player,new S2CUpdateP2P(cache.getP2PUpdates()));state.updatePending=false;}
 }
 public static void initConnection(Player player,IGrid grid,MemoryInfo info){
  var cache=new GridServerCache(grid,player,info.type());playerState.put(player.getUUID(),new PlayerRequest(cache));
  if(!(player instanceof ServerPlayer serverPlayer))return;
  player.openMenu(new MenuProvider(){
   @Override public AbstractContainerMenu createMenu(int id,Inventory inventory,Player player){var menu=new AdvancedMemoryCardMenu(id,inventory);menu.memoryInfo=info;menu.infos=cache.retrieveP2PList();return menu;}
   @Override public Component getDisplayName(){return Component.translatable("container.examplemod.example_menu");}
  });
  PacketDistributor.sendToPlayer(serverPlayer,new S2COpenGui(cache.retrieveP2PList(),info));
 }
 public static void removeConnection(Player player){playerState.remove(player.getUUID());}
}
