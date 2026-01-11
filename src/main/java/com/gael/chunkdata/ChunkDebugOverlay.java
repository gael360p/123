package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class ChunkDebugOverlay {
	private ChunkDebugOverlay() {}

	public static void render(DrawContext ctx) {
		if (!ChunkDataDebugState.DEBUG_ENABLED) return;

		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null) return;

		int x = 8;
		int y = 8 + 90; // below heatmap area (roughly), still top-left
		int color = 0xFFFFFFFF;

		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("[ChunkData DEBUG]"), x, y, color); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("trackedChunks=" + ChunkDataStore.size()), x, y, color); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("packetsSeen=" + ChunkDataDebugState.packetsSeen), x, y, color); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastChunk=(" + ChunkDataDebugState.lastChunkX + "," + ChunkDataDebugState.lastChunkZ + ")"), x, y, color); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastBytes=" + ChunkDataStore.formatBytes(ChunkDataDebugState.bytesLast) + "B"), x, y, color); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastThread=" + ChunkDataDebugState.lastThread), x, y, color); y += 10;
		if (!ChunkDataDebugState.lastError.isEmpty()) {
			ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastError=" + ChunkDataDebugState.lastError), x, y, 0xFFFFAAAA);
		}
	}
}
