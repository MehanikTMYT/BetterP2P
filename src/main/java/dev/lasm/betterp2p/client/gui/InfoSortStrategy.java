package dev.lasm.betterp2p.client.gui;
import java.util.Comparator;
public enum InfoSortStrategy implements Comparator<InfoWrapper>{DEFAULT,BY_FREQUENCY;public int compare(InfoWrapper a,InfoWrapper b){return Short.compare(a.frequency,b.frequency);}}
