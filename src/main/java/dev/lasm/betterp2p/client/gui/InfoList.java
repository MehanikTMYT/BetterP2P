package dev.lasm.betterp2p.client.gui;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import dev.lasm.betterp2p.client.gui.widget.WidgetScrollBar;
import dev.lasm.betterp2p.item.BetterMemoryCardModes;
import dev.lasm.betterp2p.network.data.P2PLocation;
public class InfoList {
 private final Map<P2PLocation,InfoWrapper> masterMap=new HashMap<>();private final Supplier<String> search;private final Supplier<BetterMemoryCardModes> mode;
 public final List<InfoWrapper> sorted=new ArrayList<>();public List<InfoWrapper> filtered=List.of();private final InfoFilter filter=new InfoFilter();public P2PLocation selectedEntry;
 public InfoList(Collection<InfoWrapper> initial,Supplier<String> search,Supplier<BetterMemoryCardModes> mode){this.search=search;this.mode=mode;initial.forEach(i->masterMap.put(i.loc,i));}
 public InfoWrapper getSelectedInfo(){return masterMap.get(selectedEntry);}public int getSize(){return masterMap.size();}
 private boolean sameFrequency(InfoWrapper i){InfoWrapper selected=getSelectedInfo();return i.frequency!=0&&selected!=null&&i.frequency==selected.frequency;}
 public void resort(){sorted.sort(Comparator.comparingInt(i->{if(i.loc.equals(selectedEntry))return -2;if(sameFrequency(i)&&!i.output)return -3;if(sameFrequency(i))return -1;if(i.frequency==0&&mode.get()==BetterMemoryCardModes.INPUT)return 0;return 1+i.frequency+Short.MAX_VALUE;}));}
 private long filterOrder(InfoWrapper i){if(i.loc.equals(selectedEntry))return Long.MIN_VALUE+1;if(sameFrequency(i)&&!i.output)return Long.MIN_VALUE;if(sameFrequency(i))return Long.MIN_VALUE+2;
 if(filter.activeFilters.containsKey(Filter.NAME)){long hits=0;String name=i.name;for(String term:filter.activeFilters.get(Filter.NAME)){var matcher=Pattern.compile(Pattern.quote(term),Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE).matcher(name);if(matcher.find()){hits++;name=name.substring(0,matcher.start())+name.substring(matcher.end());}}return -(hits*hits)+name.length();}return i.frequency+Long.MAX_VALUE-(i.output?0:1);}
 public void refilter(){filter.updateFilter(search.get().toLowerCase(Locale.getDefault()));filtered=sorted.stream().filter(i->i.loc.equals(selectedEntry)||filter.activeFilters.entrySet().stream().allMatch(e->e.getKey().filter(i,e.getValue()))).sorted(Comparator.comparingLong(this::filterOrder)).toList();}
 public void refresh(){sorted.clear();sorted.addAll(masterMap.values());resort();refilter();}
 public void rebuild(Collection<InfoWrapper> updates,WidgetScrollBar bar,int count){masterMap.clear();update(updates,bar,count);}
 public void update(Collection<InfoWrapper> updates,WidgetScrollBar bar,int count){updates.forEach(i->masterMap.put(i.loc,i));refresh();bar.setRange(0,Math.max(0,masterMap.size()-count),23);}
 public void select(P2PLocation location){InfoWrapper info=masterMap.get(location);selectedEntry=info==null?null:info.loc;}
}
