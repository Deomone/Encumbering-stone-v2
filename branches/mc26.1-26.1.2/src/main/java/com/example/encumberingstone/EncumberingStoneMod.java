package com.example.encumberingstone;

import com.example.encumberingstone.logic.EncumberingPenaltyLogic;
import com.example.encumberingstone.registry.ModComponents;
import com.example.encumberingstone.registry.ModItems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncumberingStoneMod implements ModInitializer {
	public static final String MOD_ID = "encumbering_stone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModComponents.register();
		ModItems.register();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				EncumberingPenaltyLogic.tick(player);
			}
		});

		LOGGER.info("Encumbering Stone initialized.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
