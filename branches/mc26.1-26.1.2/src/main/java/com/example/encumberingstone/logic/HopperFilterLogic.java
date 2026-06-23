package com.example.encumberingstone.logic;

import java.util.HashSet;
import java.util.Set;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.Hopper;

public final class HopperFilterLogic {
	private HopperFilterLogic() {
	}

	public static boolean shouldBlockTransfer(Container from, Container to, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}

		if (stack.getItem() instanceof EncumberingStoneItem
				&& (from instanceof Hopper || to instanceof Hopper)) {
			return true;
		}

		Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());

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

	public static boolean shouldBlockPickup(Container container, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if (stack.getItem() instanceof EncumberingStoneItem && container instanceof Hopper) {
			return true;
		}
		Set<Identifier> filter = activeFilter(container);
		return filter != null && !filter.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()));
	}

	private static Set<Identifier> activeFilter(Container container) {
		if (!(container instanceof Hopper)) {
			return null;
		}
		Set<Identifier> union = null;
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack slot = container.getItem(i);
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
