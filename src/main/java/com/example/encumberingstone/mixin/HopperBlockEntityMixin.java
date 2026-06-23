package com.example.encumberingstone.mixin;

import com.example.encumberingstone.logic.HopperFilterLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {

	@Inject(
			method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;",
			at = @At("HEAD"),
			cancellable = true)
	private static void encumberingStone$filterTransfer(Inventory from, Inventory to, ItemStack stack,
			Direction side, CallbackInfoReturnable<ItemStack> cir) {
		if (HopperFilterLogic.shouldBlockTransfer(from, to, stack)) {

			cir.setReturnValue(stack);
		}
	}

	@Inject(
			method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z",
			at = @At("HEAD"),
			cancellable = true)
	private static void encumberingStone$filterPickup(Inventory inventory, ItemEntity itemEntity,
			CallbackInfoReturnable<Boolean> cir) {
		if (HopperFilterLogic.shouldBlockPickup(inventory, itemEntity.getStack())) {
			cir.setReturnValue(false);
		}
	}
}
