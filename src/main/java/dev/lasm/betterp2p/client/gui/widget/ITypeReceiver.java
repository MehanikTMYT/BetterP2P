package dev.lasm.betterp2p.client.gui.widget;
import net.minecraft.client.gui.layouts.LayoutElement;
import dev.lasm.betterp2p.util.p2p.ClientTunnelInfo;
public interface ITypeReceiver extends LayoutElement {void accept(ClientTunnelInfo type);}
