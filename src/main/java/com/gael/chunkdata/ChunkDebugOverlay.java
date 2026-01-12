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

		int x = 8, y = 8;
		int c = 0xFFFFFFFF;

		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("[ChunkData DEBUG]"), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("trackedChunks=" + ChunkDataStore.size()), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("ctorPackets=" + ChunkDataDebugState.ctorPackets), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("handlerPackets=" + ChunkDataDebugState.handlerPackets), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("measuredWrite=" + ChunkDataDebugState.measuredWritePackets), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastHook=" + ChunkDataDebugState.lastHook), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastChunk=(" + ChunkDataDebugState.lastChunkX + "," + ChunkDataDebugState.lastChunkZ + ")"), x, y, c); y += 10;
		ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastBytes=" + ChunkDataStore.formatBytes(ChunkDataDebugState.lastBytes) + "B"), x, y, c); y += 10;
		if (!ChunkDataDebugState.lastError.isEmpty()) {
			ctx.drawTextWithShadow(mc.textRenderer, Text.literal("error=" + ChunkDataDebugState.lastError), x, y, 0xFFFFAAAA);
		}
	}
}
