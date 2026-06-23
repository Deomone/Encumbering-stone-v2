package com.example.encumberingstone.registry;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModItems {
	private ModItems() {
	}

	public static final EncumberingStoneItem BASIC_ENCUMBERING_STONE = register("basic_encumbering_stone", 9);
	public static final EncumberingStoneItem ENHANCED_ENCUMBERING_STONE = register("enhanced_encumbering_stone", 18);
	public static final EncumberingStoneItem UPGRADED_ENCUMBERING_STONE = register("upgraded_encumbering_stone", 27);

	private static EncumberingStoneItem register(String name, int capacity) {
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, EncumberingStoneMod.id(name));
		EncumberingStoneItem item = new EncumberingStoneItem(capacity, new Item.Settings().registryKey(key));
		return Registry.register(Registries.ITEM, key, item);
	}

	public static void register() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
			entries.add(BASIC_ENCUMBERING_STONE);
			entries.add(ENHANCED_ENCUMBERING_STONE);
			entries.add(UPGRADED_ENCUMBERING_STONE);
		});
	}
}
