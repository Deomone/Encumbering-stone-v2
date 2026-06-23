package com.example.encumberingstone.logic;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class EncumberingPickupLogic {
	private EncumberingPickupLogic() {
	}

	public static boolean shouldBlockPickup(Player player, ItemStack pickedUpStack) {
		if (pickedUpStack.isEmpty()) {
			return false;
		}

		Identifier pickedUpId = BuiltInRegistries.ITEM.getKey(pickedUpStack.getItem());
		Inventory inventory = player.getInventory();
		boolean anyActiveStone = false;

		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (!(stack.getItem() instanceof EncumberingStoneItem)) {
				continue;
			}
			EncumberingStoneComponent data = EncumberingStoneItem.getData(stack);
			if (!data.enabled()) {
				continue;
			}
			if (data.contains(pickedUpId)) {
				return false;
			}
			anyActiveStone = true;
		}

		return anyActiveStone;
	}
}
