package dev.lasm.betterp2p.network;
import dev.lasm.betterp2p.network.data.GridServerCache;
/** Mutable per-player network request lifecycle. */
public final class PlayerRequest {
 public boolean updatePending;
 public long updateReady;
 public final GridServerCache gridCache;
 public PlayerRequest(GridServerCache gridCache){this(false,System.currentTimeMillis(),gridCache);}
 public PlayerRequest(boolean updatePending,long updateReady,GridServerCache gridCache){this.updatePending=updatePending;this.updateReady=updateReady;this.gridCache=gridCache;}
 public GridServerCache getGridCache(){return gridCache;}
}
