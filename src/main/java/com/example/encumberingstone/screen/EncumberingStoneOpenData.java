package com.example.encumberingstone.screen;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record EncumberingStoneOpenData(boolean mainHand) {
	public static final PacketCodec<RegistryByteBuf, EncumberingStoneOpenData> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.BOOL, EncumberingStoneOpenData::mainHand,
			EncumberingStoneOpenData::new
	);
}
