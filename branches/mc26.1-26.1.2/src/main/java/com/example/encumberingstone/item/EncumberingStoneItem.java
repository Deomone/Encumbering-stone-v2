package com.example.encumberingstone.item;

import java.util.function.Consumer;

import com.example.encumberingstone.component.EncumberingStoneComponent;
import com.example.encumberingstone.registry.ModComponents;
import com.example.encumberingstone.screen.EncumberingStoneScreenHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class EncumberingStoneItem extends Item {
	private final int capacity;

	public EncumberingStoneItem(int capacity, Properties properties) {
		super(properties
				.stacksTo(1)
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

		stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, next.enabled());
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (player.isShiftKeyDown()) {

			if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
				serverPlayer.openMenu(new SimpleMenuProvider(
						(syncId, inventory, p) -> EncumberingStoneScreenHandler.create(syncId, inventory, hand),
						Component.translatable("container.encumbering_stone.title")));
			}
			return InteractionResult.SUCCESS;
		}

		toggle(stack);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action,
			Player player, SlotAccess access) {
		if (action == ClickAction.SECONDARY && other.isEmpty()) {
			toggle(stack);
			return true;
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
			Consumer<Component> consumer, TooltipFlag flag) {
		EncumberingStoneComponent data = getData(stack);
		consumer.accept(data.enabled()
				? Component.translatable("tooltip.encumbering_stone.enabled").withStyle(ChatFormatting.GREEN)
				: Component.translatable("tooltip.encumbering_stone.disabled").withStyle(ChatFormatting.RED));
		consumer.accept(Component.translatable("tooltip.encumbering_stone.exceptions",
				data.exceptions().size(), data.capacity()).withStyle(ChatFormatting.GRAY));
		consumer.accept(Component.translatable("tooltip.encumbering_stone.hint_toggle").withStyle(ChatFormatting.DARK_GRAY));
		consumer.accept(Component.translatable("tooltip.encumbering_stone.hint_menu").withStyle(ChatFormatting.DARK_GRAY));
	}

}
