package com.example.encumberingstone.registry;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class ModItems {
	private ModItems() {
	}

	public static final EncumberingStoneItem BASIC_ENCUMBERING_STONE = register(
			"basic_encumbering_stone", new EncumberingStoneItem(9, new Item.Settings()));

	public static final EncumberingStoneItem ENHANCED_ENCUMBERING_STONE = register(
			"enhanced_encumbering_stone", new EncumberingStoneItem(18, new Item.Settings()));

	public static final EncumberingStoneItem UPGRADED_ENCUMBERING_STONE = register(
			"upgraded_encumbering_stone", new EncumberingStoneItem(27, new Item.Settings()));

	private static <T extends Item> T register(String name, T item) {
		return Registry.register(Registries.ITEM, EncumberingStoneMod.id(name), item);
	}

	public static void register() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.add(BASIC_ENCUMBERING_STONE);
			entries.add(ENHANCED_ENCUMBERING_STONE);
			entries.add(UPGRADED_ENCUMBERING_STONE);
		});
	}
}
