package com.example.encumberingstone;

import com.example.encumberingstone.logic.EncumberingPenaltyLogic;
import com.example.encumberingstone.registry.ModComponents;
import com.example.encumberingstone.registry.ModItems;
import com.example.encumberingstone.registry.ModScreenHandlers;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncumberingStoneMod implements ModInitializer {
	public static final String MOD_ID = "encumbering_stone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModComponents.register();
		ModItems.register();
		ModScreenHandlers.register();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				EncumberingPenaltyLogic.tick(player);
			}
		});

		LOGGER.info("Encumbering Stone initialized.");
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
