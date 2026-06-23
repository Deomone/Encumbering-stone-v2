package com.example.encumberingstone.logic;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class EncumberingPenaltyLogic {
	private static final Identifier SPEED_MODIFIER_ID = EncumberingStoneMod.id("stone_weight_speed_penalty");
	private static final Identifier JUMP_MODIFIER_ID = EncumberingStoneMod.id("stone_weight_jump_penalty");
	private static final double PENALTY_PER_EXTRA_STONE = 0.5;
	private static final double MAX_PENALTY = 1.0;

	private EncumberingPenaltyLogic() {
	}

	public static void tick(ServerPlayerEntity player) {
		int stones = countStones(player.getInventory());
		double value = stones >= 2
				? -Math.min(MAX_PENALTY, PENALTY_PER_EXTRA_STONE * (stones - 1))
				: 0.0;

		applyModifier(player, EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER_ID, value);
		applyModifier(player, EntityAttributes.GENERIC_JUMP_STRENGTH, JUMP_MODIFIER_ID, value);
	}

	private static void applyModifier(ServerPlayerEntity player, RegistryEntry<EntityAttribute> attributeType,
			Identifier modifierId, double value) {
		EntityAttributeInstance attribute = player.getAttributeInstance(attributeType);
		if (attribute == null) {
			return;
		}
		EntityAttributeModifier existing = attribute.getModifier(modifierId);
		if (value == 0.0) {
			if (existing != null) {
				attribute.removeModifier(modifierId);
			}
			return;
		}
		if (existing != null && existing.value() == value) {
			return;
		}
		attribute.removeModifier(modifierId);
		attribute.addTemporaryModifier(new EntityAttributeModifier(
				modifierId, value, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}

	private static int countStones(PlayerInventory inventory) {
		int count = 0;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.getStack(i).getItem() instanceof EncumberingStoneItem) {
				count++;
			}
		}
		return count;
	}
}
