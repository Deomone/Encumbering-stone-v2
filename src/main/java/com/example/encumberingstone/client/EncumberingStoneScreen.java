package com.example.encumberingstone.client;

import com.example.encumberingstone.screen.EncumberingStoneScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EncumberingStoneScreen extends HandledScreen<EncumberingStoneScreenHandler> {
	private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/generic_54.png");

	private final int rows;

	public EncumberingStoneScreen(EncumberingStoneScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.rows = handler.getRows();
		this.backgroundHeight = 114 + this.rows * 18;
		this.playerInventoryTitleY = this.backgroundHeight - 94;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int left = (this.width - this.backgroundWidth) / 2;
		int top = (this.height - this.backgroundHeight) / 2;

		context.drawTexture(TEXTURE, left, top, 0, 0, this.backgroundWidth, this.rows * 18 + 17);

		context.drawTexture(TEXTURE, left, top + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
	}
}
