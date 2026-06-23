package com.example.encumberingstone.logic;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class EncumberingPickupLogic {
	private EncumberingPickupLogic() {
	}

	public static boolean shouldBlockPickup(PlayerEntity player, ItemStack pickedUpStack) {
		if (pickedUpStack.isEmpty()) {
			return false;
		}

		Identifier pickedUpId = Registries.ITEM.getId(pickedUpStack.getItem());
		PlayerInventory inventory = player.getInventory();
		boolean anyActiveStone = false;

		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
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
