package com.example.encumberingstone.mixin;

import com.example.encumberingstone.logic.EncumberingPickupLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

	@Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
	private void encumberingStone$blockPickup(Player player, CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;

		if (player.level().isClientSide()) {
			return;
		}
		if (EncumberingPickupLogic.shouldBlockPickup(player, self.getItem())) {
			ci.cancel();
		}
	}
}
