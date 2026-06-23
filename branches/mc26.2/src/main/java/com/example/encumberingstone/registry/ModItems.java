package com.example.encumberingstone.registry;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public final class ModItems {
	private ModItems() {
	}

	private static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("tools_and_utilities"));

	public static final EncumberingStoneItem BASIC_ENCUMBERING_STONE = register("basic_encumbering_stone", 9);
	public static final EncumberingStoneItem ENHANCED_ENCUMBERING_STONE = register("enhanced_encumbering_stone", 18);
	public static final EncumberingStoneItem UPGRADED_ENCUMBERING_STONE = register("upgraded_encumbering_stone", 27);

	private static EncumberingStoneItem register(String name, int capacity) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, EncumberingStoneMod.id(name));
		EncumberingStoneItem item = new EncumberingStoneItem(capacity, new Item.Properties().setId(key));
		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static void register() {
		CreativeModeTabEvents.modifyOutputEvent(TOOLS_AND_UTILITIES).register(output -> {
			output.accept(BASIC_ENCUMBERING_STONE);
			output.accept(ENHANCED_ENCUMBERING_STONE);
			output.accept(UPGRADED_ENCUMBERING_STONE);
		});
	}
}
