package com.example.encumberingstone.item;

import java.util.function.Consumer;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.registry.ModComponents;
import com.example.encumberingstone.screen.EncumberingStoneOpenData;
import com.example.encumberingstone.screen.EncumberingStoneScreenHandler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class EncumberingStoneItem extends Item {
	private final int capacity;

	public EncumberingStoneItem(int capacity, Settings settings) {
		super(settings
				.maxCount(1)
				.component(ModComponents.STONE_DATA, EncumberingStoneComponent.createDefault(capacity)));
		this.capacity = capacity;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public static EncumberingStoneComponent getData(ItemStack stack) {
		EncumberingStoneComponent component = stack.get(ModComponents.STONE_DATA);
		if (component != null) {
			return component;
		}
		int capacity = stack.getItem() instanceof EncumberingStoneItem stone ? stone.getCapacity() : 9;
		return EncumberingStoneComponent.createDefault(capacity);
	}

	public static void toggle(ItemStack stack) {
		EncumberingStoneComponent next = getData(stack).toggled();
		stack.set(ModComponents.STONE_DATA, next);

		stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, next.enabled());
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);

		if (user.isSneaking()) {

			if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
				serverPlayer.openHandledScreen(createScreenFactory(hand));
			}
			return ActionResult.SUCCESS;
		}

		toggle(stack);
		return ActionResult.SUCCESS;
	}

	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType,
			PlayerEntity player, StackReference cursorStackReference) {
		if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
			toggle(stack);
			return true;
		}
		return false;
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent,
			Consumer<Text> textConsumer, TooltipType type) {
		EncumberingStoneComponent data = getData(stack);
		textConsumer.accept(data.enabled()
				? Text.translatable("tooltip.encumbering_stone.enabled").formatted(Formatting.GREEN)
				: Text.translatable("tooltip.encumbering_stone.disabled").formatted(Formatting.RED));
		textConsumer.accept(Text.translatable("tooltip.encumbering_stone.exceptions",
				data.exceptions().size(), data.capacity()).formatted(Formatting.GRAY));
		textConsumer.accept(Text.translatable("tooltip.encumbering_stone.hint_toggle").formatted(Formatting.DARK_GRAY));
		textConsumer.accept(Text.translatable("tooltip.encumbering_stone.hint_menu").formatted(Formatting.DARK_GRAY));
	}

	private static ExtendedScreenHandlerFactory<EncumberingStoneOpenData> createScreenFactory(Hand hand) {
		return new ExtendedScreenHandlerFactory<>() {
			@Override
			public EncumberingStoneOpenData getScreenOpeningData(ServerPlayerEntity player) {
				return new EncumberingStoneOpenData(hand == Hand.MAIN_HAND);
			}

			@Override
			public Text getDisplayName() {
				return Text.translatable("container.encumbering_stone.title");
			}

			@Override
			public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
				return new EncumberingStoneScreenHandler(syncId, playerInventory, hand);
			}
		};
	}
}
