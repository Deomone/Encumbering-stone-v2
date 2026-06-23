package com.example.encumberingstone.screen;

import java.util.List;
import java.util.function.UnaryOperator;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.item.EncumberingStoneItem;
import com.example.encumberingstone.registry.ModComponents;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class EncumberingStoneScreenHandler extends ChestMenu {
	private final Player player;
	private final InteractionHand hand;
	private final int rows;
	private final SimpleContainer ghostInventory;

	public static EncumberingStoneScreenHandler create(int syncId, Inventory playerInventory, InteractionHand hand) {
		ItemStack stone = playerInventory.player.getItemInHand(hand);
		int capacity = EncumberingStoneItem.getData(stone).capacity();
		int rows = Math.max(1, Math.min(3, capacity / 9));
		return new EncumberingStoneScreenHandler(syncId, playerInventory, hand, rows, new SimpleContainer(rows * 9));
	}

	private EncumberingStoneScreenHandler(int syncId, Inventory playerInventory, InteractionHand hand,
			int rows, SimpleContainer ghostInventory) {
		super(menuTypeFor(rows), syncId, playerInventory, ghostInventory, rows);
		this.player = playerInventory.player;
		this.hand = hand;
		this.rows = rows;
		this.ghostInventory = ghostInventory;
		refreshGhosts(EncumberingStoneItem.getData(getStone()));
	}

	private static MenuType<ChestMenu> menuTypeFor(int rows) {
		return switch (rows) {
			case 1 -> MenuType.GENERIC_9x1;
			case 2 -> MenuType.GENERIC_9x2;
			default -> MenuType.GENERIC_9x3;
		};
	}

	private ItemStack getStone() {
		return this.player.getItemInHand(this.hand);
	}

	private void refreshGhosts(EncumberingStoneComponent data) {
		List<Identifier> exceptions = data.exceptions();
		for (int i = 0; i < this.ghostInventory.getContainerSize(); i++) {
			if (i < exceptions.size()) {
				this.ghostInventory.setItem(i, new ItemStack(BuiltInRegistries.ITEM.getValue(exceptions.get(i))));
			} else {
				this.ghostInventory.setItem(i, ItemStack.EMPTY);
			}
		}
	}

	private void updateComponent(UnaryOperator<EncumberingStoneComponent> operation) {
		ItemStack stone = getStone();
		if (!(stone.getItem() instanceof EncumberingStoneItem)) {
			return;
		}
		EncumberingStoneComponent before = EncumberingStoneItem.getData(stone);
		EncumberingStoneComponent after = operation.apply(before);
		if (!after.equals(before)) {
			stone.set(ModComponents.STONE_DATA, after);
		}
		refreshGhosts(after);
	}

	@Override
	public void clicked(int slotIndex, int button, ContainerInput input, Player player) {
		if (slotIndex < 0 || slotIndex >= this.slots.size()) {
			return;
		}
		if (!(getStone().getItem() instanceof EncumberingStoneItem)) {
			return;
		}

		int ghostSlotCount = this.rows * 9;
		if (slotIndex < ghostSlotCount) {

			ItemStack ghost = this.ghostInventory.getItem(slotIndex);
			if (!ghost.isEmpty()) {
				Identifier id = BuiltInRegistries.ITEM.getKey(ghost.getItem());
				updateComponent(component -> component.withoutException(id));
			}
		} else if (input == ContainerInput.PICKUP || input == ContainerInput.QUICK_MOVE) {

			ItemStack clickedStack = this.slots.get(slotIndex).getItem();
			if (!clickedStack.isEmpty()) {
				Identifier id = BuiltInRegistries.ITEM.getKey(clickedStack.getItem());
				updateComponent(component -> component.withException(id));
			}
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return getStone().getItem() instanceof EncumberingStoneItem;
	}
}
