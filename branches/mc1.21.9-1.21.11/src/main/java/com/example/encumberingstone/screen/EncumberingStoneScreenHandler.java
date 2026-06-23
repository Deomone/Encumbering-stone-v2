package com.example.encumberingstone.screen;

import java.util.List;
import java.util.function.UnaryOperator;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.item.EncumberingStoneItem;
import com.example.encumberingstone.registry.ModComponents;
import com.example.encumberingstone.registry.ModScreenHandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class EncumberingStoneScreenHandler extends ScreenHandler {
	private final PlayerEntity player;
	private final Hand hand;
	private final int capacity;
	private final int rows;
	private final SimpleInventory ghostInventory;

	public EncumberingStoneScreenHandler(int syncId, PlayerInventory playerInventory, EncumberingStoneOpenData data) {
		this(syncId, playerInventory, data.mainHand() ? Hand.MAIN_HAND : Hand.OFF_HAND);
	}

	public EncumberingStoneScreenHandler(int syncId, PlayerInventory playerInventory, Hand hand) {
		super(ModScreenHandlers.ENCUMBERING_STONE, syncId);
		this.player = playerInventory.player;
		this.hand = hand;

		EncumberingStoneComponent data = EncumberingStoneItem.getData(getStone());
		this.capacity = data.capacity();
		this.rows = Math.max(1, (this.capacity + 8) / 9);
		this.ghostInventory = new SimpleInventory(this.rows * 9);
		refreshGhosts(data);

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < 9; col++) {
				int index = col + row * 9;
				this.addSlot(new GhostSlot(this.ghostInventory, index, 8 + col * 18, 18 + row * 18, index < this.capacity));
			}
		}

		int yOffset = (this.rows - 4) * 18;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + yOffset));
			}
		}
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 161 + yOffset));
		}
	}

	public int getRows() {
		return this.rows;
	}

	private ItemStack getStone() {
		return this.player.getStackInHand(this.hand);
	}

	private void refreshGhosts(EncumberingStoneComponent data) {
		List<Identifier> exceptions = data.exceptions();
		for (int i = 0; i < this.ghostInventory.size(); i++) {
			if (i < exceptions.size()) {
				this.ghostInventory.setStack(i, new ItemStack(Registries.ITEM.get(exceptions.get(i))));
			} else {
				this.ghostInventory.setStack(i, ItemStack.EMPTY);
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
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		if (slotIndex < 0 || slotIndex >= this.slots.size()) {
			return;
		}
		if (!(getStone().getItem() instanceof EncumberingStoneItem)) {
			return;
		}

		int ghostSlotCount = this.rows * 9;
		if (slotIndex < ghostSlotCount) {

			ItemStack ghost = this.ghostInventory.getStack(slotIndex);
			if (!ghost.isEmpty()) {
				Identifier id = Registries.ITEM.getId(ghost.getItem());
				updateComponent(component -> component.withoutException(id));
			}
		} else if (actionType == SlotActionType.PICKUP || actionType == SlotActionType.QUICK_MOVE) {

			ItemStack clicked = this.slots.get(slotIndex).getStack();
			if (!clicked.isEmpty()) {
				Identifier id = Registries.ITEM.getId(clicked.getItem());
				updateComponent(component -> component.withException(id));
			}
		}
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return getStone().getItem() instanceof EncumberingStoneItem;
	}

	private static class GhostSlot extends Slot {
		private final boolean active;

		GhostSlot(Inventory inventory, int index, int x, int y, boolean active) {
			super(inventory, index, x, y);
			this.active = active;
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false;
		}

		@Override
		public boolean canTakeItems(PlayerEntity player) {
			return false;
		}

		@Override
		public boolean isEnabled() {
			return this.active;
		}
	}
}
