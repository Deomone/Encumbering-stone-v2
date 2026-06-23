package com.example.encumberingstone.client;

import com.example.encumberingstone.registry.ModScreenHandlers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class EncumberingStoneClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HandledScreens.register(ModScreenHandlers.ENCUMBERING_STONE, EncumberingStoneScreen::new);
	}
}
