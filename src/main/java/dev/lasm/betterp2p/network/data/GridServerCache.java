package dev.lasm.betterp2p.network.data;

import appeng.api.networking.IGrid;
import appeng.api.parts.IPartItem;
import appeng.me.service.P2PService;
import appeng.parts.p2p.P2PTunnelPart;
import appeng.util.Platform;
import dev.lasm.betterp2p.BetterP2P;
import dev.lasm.betterp2p.util.p2p.TunnelInfo;
import dev.lasm.betterp2p.util.p2p.P2PUtil;
import java.util.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/** Server-side grid and dirty tunnel cache for an open memory-card GUI. */
public final class GridServerCache {
 private final IGrid grid;
 public final Player player;
 public int type;
 private final Map<P2PLocation,P2PTunnelPart<?>> listP2P = new LinkedHashMap<>();
 private final Set<P2PLocation> dirtyP2P = new LinkedHashSet<>();
 public GridServerCache(IGrid grid,Player player,int type){this.grid=grid;this.player=player;this.type=type;rebuildList(type);}
 public Player getPlayer(){return player;} public int getType(){return type;} public void setType(int type){this.type=type;}
 private void rebuildList(int type){
  synchronized(listP2P){
   listP2P.clear();dirtyP2P.clear();
   for(var clazz:grid.getMachineClasses()){
    var selected=BetterP2P.proxy.getP2PFromIndex(type);
    if(P2PTunnelPart.class.isAssignableFrom(clazz) && (type==MemoryInfo.TUNNEL_ANY || selected!=null && selected.clazz==clazz)){
     for(var machine:grid.getMachines(clazz)){var p=(P2PTunnelPart<?>)machine;listP2P.put(P2PLocation.toLoc(p),p);}
    }
   }
  }
 }
 public List<P2PInfo> retrieveP2PList(){
  rebuildList(type);var result=new ArrayList<P2PInfo>();
  for(var p:listP2P.values()){
   var info=BetterP2P.proxy.getP2PFromClass(p.getClass());int index=info==null?MemoryInfo.TUNNEL_ANY:info.index;
   if(type==MemoryInfo.TUNNEL_ANY || index==type)result.add(P2PInfo.toInfo(p));
  }return result;
 }
 public void markDirty(P2PLocation key,P2PTunnelPart<?> p){synchronized(listP2P){if(type==MemoryInfo.TUNNEL_ANY || P2PUtil.getTypeIndex(p)==type){listP2P.put(key,p);dirtyP2P.add(key);}}}
 public List<P2PInfo> getP2PUpdates(){var result=new ArrayList<P2PInfo>();for(var key:dirtyP2P){var p=listP2P.get(key);if(p!=null)result.add(P2PInfo.toInfo(p));}dirtyP2P.clear();return result;}
 public P2PTunnelPart<?> getTunnel(P2PLocation location) {
  synchronized (listP2P) { return listP2P.get(location); }
 }
 public record LinkedTunnels(P2PTunnelPart<?> input,P2PTunnelPart<?> output){}
 public LinkedTunnels linkP2P(P2PLocation inputIndex,P2PLocation outputIndex){
  var input=listP2P.get(inputIndex);var output=listP2P.get(outputIndex);if(input==null || output==null)return null;
  if(input.getClass()!=output.getClass()){output=changeP2PType(output,Objects.requireNonNull(BetterP2P.proxy.getP2PFromClass(input.getClass())));if(output==null)return null;}
  if(input==output)return null;
  short frequency=input.getFrequency();var cache=P2PService.get(Objects.requireNonNull(input.getGridNode()).getGrid());
  if(frequency==0 || input.isOutput())frequency=cache.newFrequency();
  var originalInput=cache.getInput(frequency);
  if(originalInput!=null && originalInput!=input)updateP2P(P2PLocation.toLoc(originalInput),originalInput,frequency,true,input.getCustomName());
  var inputResult=updateP2P(inputIndex,input,frequency,false,input.getCustomName());
  var outputResult=updateP2P(outputIndex,output,frequency,true,input.getCustomName());
  return new LinkedTunnels(inputResult,outputResult);
 }
 public P2PTunnelPart<?> unlinkP2P(P2PLocation key){var p=listP2P.get(key);if(p==null || p.getFrequency()==0)return p;return updateP2P(key,p,(short)0,false,p.getCustomName());}
 private P2PTunnelPart<?> updateP2P(P2PLocation key,P2PTunnelPart<?> p,short frequency,boolean output,Component name){
  var service=P2PService.get(p.getMainNode().getGrid());P2PUtil.pleaseSetTheFuckingOutputState(p,output);P2PUtil.setCustomName(p,name);service.updateFreq(p,frequency);Platform.notifyBlocksOfNeighbors(p.getBlockEntity().getLevel(),p.getBlockEntity().getBlockPos());return p;
 }
 private P2PTunnelPart<?> changeP2PType(P2PTunnelPart<?> tunnel,TunnelInfo newType){
  if(Objects.equals(BetterP2P.proxy.getP2PFromClass(tunnel.getClass()),newType)){player.sendSystemMessage(Component.translatable("gui.advanced_memory_card.error.same_type"));return null;}
  var blockEntity=tunnel.getBlockEntity();
  if(blockEntity!=null && blockEntity.getLevel() instanceof ServerLevel level){
   P2PTunnelPart<?>[] result={null};
   level.getServer().executeBlocking(()->{
    boolean oldOutput=tunnel.isOutput();short freq=tunnel.getFrequency();Objects.requireNonNull(tunnel.getBlockEntity());
    if(!(newType.stack.getItem() instanceof IPartItem<?> partItem)){BetterP2P.logger.error("Attempt to assign a invalid type {} to tunnel {}, this shouldn\u0027t happen!",newType,tunnel.getBlockEntity());return;}
    if(!P2PTunnelPart.class.isAssignableFrom(partItem.getPartClass())){BetterP2P.logger.error("Attempt to assign a invalid type {} to tunnel {}, this shouldn\u0027t happen!",partItem.getPartClass(),tunnel.getBlockEntity());return;}
    result[0]=tunnel;
    if(tunnel.getPartItem()!=partItem){
     var replaced=(P2PTunnelPart<?>)Objects.requireNonNull(tunnel.getHost().replacePart(partItem,tunnel.getSide(),player,InteractionHand.MAIN_HAND));
     P2PUtil.pleaseSetTheFuckingOutputState(replaced,oldOutput);replaced.onTunnelNetworkChange();replaced.setFrequency(freq);result[0]=replaced;
    }
    Platform.notifyBlocksOfNeighbors(tunnel.getLevel(),tunnel.getBlockEntity().getBlockPos());
   });return result[0];
  }return tunnel;
 }
 public boolean changeAllP2Ps(P2PLocation key,TunnelInfo newType){
  var tunnel=listP2P.get(key);if(tunnel==null)return false;
  try{if(tunnel.isOutput() && tunnel.getInput()!=null)tunnel=tunnel.getInput();var outputs=new ArrayList<P2PTunnelPart<?>>();for(var p:tunnel.getOutputs())outputs.add(p);changeP2PType(tunnel,newType);for(var p:outputs)changeP2PType(p,newType);return true;}catch(Exception ignored){return false;}
 }
}
