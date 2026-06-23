package com.example.encumberingstone.mixin;

import com.example.encumberingstone.logic.EncumberingPickupLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

	@Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
	private void encumberingStone$blockPickup(PlayerEntity player, CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;

		if (player.getWorld().isClient()) {
			return;
		}
		if (EncumberingPickupLogic.shouldBlockPickup(player, self.getStack())) {
			ci.cancel();
		}
	}
}
