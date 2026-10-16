package dev.lasm.betterp2p.network.packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
public sealed interface IMessage extends CustomPacketPayload permits IC2SMessage, IS2CMessage {}
