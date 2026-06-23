package com.example.encumberingstone.mixin;

import com.example.encumberingstone.logic.HopperFilterLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {

	@Inject(
			method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;",
			at = @At("HEAD"),
			cancellable = true)
	private static void encumberingStone$filterTransfer(Container from, Container to, ItemStack stack,
			Direction direction, CallbackInfoReturnable<ItemStack> cir) {
		if (HopperFilterLogic.shouldBlockTransfer(from, to, stack)) {

			cir.setReturnValue(stack);
		}
	}

	@Inject(
			method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z",
			at = @At("HEAD"),
			cancellable = true)
	private static void encumberingStone$filterPickup(Container container, ItemEntity itemEntity,
			CallbackInfoReturnable<Boolean> cir) {
		if (HopperFilterLogic.shouldBlockPickup(container, itemEntity.getItem())) {
			cir.setReturnValue(false);
		}
	}
}
