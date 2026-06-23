package com.example.encumberingstone.logic;

import java.util.HashSet;
import java.util.Set;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.minecraft.block.entity.Hopper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class HopperFilterLogic {
	private HopperFilterLogic() {
	}

	public static boolean shouldBlockTransfer(Inventory from, Inventory to, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		if (stack.getItem() instanceof EncumberingStoneItem
				&& (from instanceof Hopper || to instanceof Hopper)) {
			return true;
		}

		Identifier id = Registries.ITEM.getId(stack.getItem());

		Set<Identifier> fromFilter = activeFilter(from);
		if (fromFilter != null && !fromFilter.contains(id)) {
			return true;
		}
		Set<Identifier> toFilter = activeFilter(to);
		if (toFilter != null && !toFilter.contains(id)) {
			return true;
		}
		return false;
	}

	public static boolean shouldBlockPickup(Inventory inventory, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if (stack.getItem() instanceof EncumberingStoneItem && inventory instanceof Hopper) {
			return true;
		}
		Set<Identifier> filter = activeFilter(inventory);
		return filter != null && !filter.contains(Registries.ITEM.getId(stack.getItem()));
	}

	private static Set<Identifier> activeFilter(Inventory inv) {
		if (!(inv instanceof Hopper)) {
			return null;
		}
		Set<Identifier> union = null;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack slot = inv.getStack(i);
			if (slot.getItem() instanceof EncumberingStoneItem) {
				EncumberingStoneComponent data = EncumberingStoneItem.getData(slot);
				if (data.enabled()) {
					if (union == null) {
						union = new HashSet<>();
					}
					union.addAll(data.exceptions());
				}
			}
		}
		return union;
	}
}
