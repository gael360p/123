package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class ChunkHeatmapOverlay {
	private ChunkHeatmapOverlay() {}

	public static void render(DrawContext ctx) {
		if (!ChunkDataConfig.HEATMAP_ENABLED) return;
		if (!ChunkDataConfig.HEATMAP_TOGGLED) return;

		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null) return;

		ctx.drawTextWithShadow(
			mc.textRenderer,
			Text.literal("ChunkData heatmap ON | tracked=" + ChunkDataStore.size() + " (H toggles)"),
			8,
			120,
			0xFFFFFFFF
		);
	}
}
