package com.example.encumberingstone.registry;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.component.EncumberingStoneComponent;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModComponents {
	private ModComponents() {
	}

	public static final ComponentType<EncumberingStoneComponent> STONE_DATA = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			EncumberingStoneMod.id("stone_data"),
			ComponentType.<EncumberingStoneComponent>builder()
					.codec(EncumberingStoneComponent.CODEC)
					.packetCodec(EncumberingStoneComponent.PACKET_CODEC)
					.build()
	);

	public static void register() {

	}
}
