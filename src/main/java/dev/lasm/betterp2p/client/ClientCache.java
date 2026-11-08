package dev.lasm.betterp2p.client;
import net.minecraft.core.*;
import java.util.*;
public final class ClientCache {
 private ClientCache(){} public record Position(BlockPos pos, Direction facing){}
 public static final List<Position> positions=new ArrayList<>();
 public static BlockPos selectedPosition; public static Direction selectedFacing; public static String searchText="";
 public static void clear(){positions.clear();selectedPosition=null;selectedFacing=null;searchText="";}
}
