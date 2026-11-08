package dev.lasm.betterp2p.client.gui;
import java.util.*;
import java.util.regex.Pattern;
import dev.lasm.betterp2p.BetterP2P;
public enum Filter {
 INPUT("\\A@in\\z"),OUTPUT("\\A@out\\z"),BOUND("\\A@b\\z"),UNBOUND("\\A@u\\z"),TYPE("\\A@types*=(.+)\\z"),NAME("\"?.+\"?");
 public final Pattern pattern;Filter(String regex){pattern=Pattern.compile(regex);}
 public boolean filter(InfoWrapper info,List<String> strings){return switch(this){
 case INPUT->!info.output;case OUTPUT->info.output;case BOUND->info.frequency!=0;case UNBOUND->info.frequency==0||info.error;
 case TYPE->{String tags=BetterP2P.proxy.getP2PFromIndex(info.type).dispName.getString().toLowerCase(Locale.ROOT);yield strings.stream().anyMatch(s->tags.contains(s.toLowerCase(Locale.ROOT)));}
 case NAME->{String name=info.name.toLowerCase(Locale.ROOT);yield strings.stream().anyMatch(s->{String query=s.startsWith("\"")&&s.endsWith("\"")&&s.length()>=2?s.substring(1,s.length()-1):s;return name.contains(query);});}
 };}
}
