package com.example.encumberingstone.logic;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.item.EncumberingStoneItem;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Inventory;

public final class EncumberingPenaltyLogic {
	private static final Identifier SPEED_MODIFIER_ID = EncumberingStoneMod.id("stone_weight_speed_penalty");
	private static final Identifier JUMP_MODIFIER_ID = EncumberingStoneMod.id("stone_weight_jump_penalty");
	private static final double PENALTY_PER_EXTRA_STONE = 0.5;
	private static final double MAX_PENALTY = 1.0;

	private EncumberingPenaltyLogic() {
	}

	public static void tick(ServerPlayer player) {
		int stones = countStones(player.getInventory());
		double value = stones >= 2
				? -Math.min(MAX_PENALTY, PENALTY_PER_EXTRA_STONE * (stones - 1))
				: 0.0;

		applyModifier(player, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID, value);
		applyModifier(player, Attributes.JUMP_STRENGTH, JUMP_MODIFIER_ID, value);
	}

	private static void applyModifier(ServerPlayer player, Holder<Attribute> attributeType,
			Identifier modifierId, double value) {
		AttributeInstance attribute = player.getAttribute(attributeType);
		if (attribute == null) {
			return;
		}
		AttributeModifier existing = attribute.getModifier(modifierId);
		if (value == 0.0) {
			if (existing != null) {
				attribute.removeModifier(modifierId);
			}
			return;
		}
		if (existing != null && existing.amount() == value) {
			return;
		}
		attribute.removeModifier(modifierId);
		attribute.addTransientModifier(new AttributeModifier(
				modifierId, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	}

	private static int countStones(Inventory inventory) {
		int count = 0;
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			if (inventory.getItem(i).getItem() instanceof EncumberingStoneItem) {
				count++;
			}
		}
		return count;
	}
}
