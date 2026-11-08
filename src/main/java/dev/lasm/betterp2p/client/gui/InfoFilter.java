package dev.lasm.betterp2p.client.gui;
import java.util.*;
import java.util.regex.*;
public class InfoFilter {
 public static final Pattern SEARCH_REGEX=Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
 public final Map<Filter,List<String>> activeFilters=new LinkedHashMap<>();
 public void updateFilter(String query){activeFilters.clear();Matcher tokens=SEARCH_REGEX.matcher(query);while(tokens.find()){
 String token=tokens.group();if(token.startsWith("@")){for(Filter f:List.of(Filter.INPUT,Filter.OUTPUT,Filter.BOUND,Filter.UNBOUND,Filter.TYPE)){Matcher matcher=f.pattern.matcher(token);if(matcher.matches()){if(f==Filter.TYPE){List<String> list=new ArrayList<>();activeFilters.putIfAbsent(f,list);list.addAll(Arrays.asList(matcher.group(1).split(";",-1)));}else activeFilters.putIfAbsent(f,null);break;}}}
 else{List<String> list=new ArrayList<>();activeFilters.putIfAbsent(Filter.NAME,list);if(tokens.group(1)!=null)list.add(tokens.group(1));else if(tokens.group(2)!=null)list.add(tokens.group(2));else activeFilters.get(Filter.NAME).add(token);}
 }}
}
