package com.example.encumberingstone.registry;

import com.example.encumberingstone.EncumberingStoneMod;
import com.example.encumberingstone.screen.EncumberingStoneOpenData;
import com.example.encumberingstone.screen.EncumberingStoneScreenHandler;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public final class ModScreenHandlers {
	private ModScreenHandlers() {
	}

	public static final ScreenHandlerType<EncumberingStoneScreenHandler> ENCUMBERING_STONE = Registry.register(
			Registries.SCREEN_HANDLER,
			EncumberingStoneMod.id("encumbering_stone"),
			new ExtendedScreenHandlerType<>(EncumberingStoneScreenHandler::new, EncumberingStoneOpenData.PACKET_CODEC)
	);

	public static void register() {

	}
}
