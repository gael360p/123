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

    int x = 8, y = 8, c = 0xFFFFFFFF;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("[ChunkData DEBUG]"), x, y, c); y += 10;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("mixinPingTicks=" + ChunkDataDebugState.mixinPingTicks), x, y, c); y += 10;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("trackedChunks=" + ChunkDataStore.size()), x, y, c); y += 10;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("onChunkDataCalls=" + ChunkDataDebugState.onChunkDataCalls), x, y, c); y += 10;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("measuredWriteCalls=" + ChunkDataDebugState.measuredWriteCalls), x, y, c); y += 10;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastChunk=(" + ChunkDataDebugState.lastChunkX + "," + ChunkDataDebugState.lastChunkZ + ")"), x, y, c); y += 10;
    ctx.drawTextWithShadow(mc.textRenderer, Text.literal("lastBytes=" + ChunkDataStore.formatBytes(ChunkDataDebugState.lastBytes) + "B"), x, y, c); y += 10;
    if (!ChunkDataDebugState.lastError.isEmpty()) {
      ctx.drawTextWithShadow(mc.textRenderer, Text.literal("error=" + ChunkDataDebugState.lastError), x, y, 0xFFFFAAAA);
    }
  }
}
