package com.example.encumberingstone.registry;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.component.EncumberingStoneComponent;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public final class ModComponents {
	private ModComponents() {
	}

	public static final DataComponentType<EncumberingStoneComponent> STONE_DATA = Registry.register(
			BuiltInRegistries.DATA_COMPONENT_TYPE,
			EncumberingStoneMod.id("stone_data"),
			DataComponentType.<EncumberingStoneComponent>builder()
					.persistent(EncumberingStoneComponent.CODEC)
					.networkSynchronized(EncumberingStoneComponent.STREAM_CODEC)
					.build()
	);

	public static void register() {
	}
}
