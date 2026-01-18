package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class ChunkHeatmapOverlay {
  private ChunkHeatmapOverlay() {}

  // Top-left small panel
  private static final int X = 8;
  private static final int Y = 8;

  public static void render(DrawContext ctx) {
    if (!ChunkDataConfig.HEATMAP_ENABLED) return;
    if (!ChunkDataConfig.HEATMAP_TOGGLED) return;

    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc.player == null) return;

    int tracked = ChunkDataStore.size();

    // Always show a visible header so you KNOW the overlay is working.
    ctx.drawTextWithShadow(mc.textRenderer,
      Text.literal("ChunkData Heatmap (H)"),
      X, Y, 0xFFFFFFFF);

    ctx.drawTextWithShadow(mc.textRenderer,
      Text.literal("tracked=" + tracked + (ChunkDataConfig.ABNORMAL_ONLY ? " (abnormal-only)" : "")),
      X, Y + 10, 0xFFFFFFFF);

    // If no data yet, still show a hint so it's not “blank”
    if (tracked == 0) {
      ctx.drawTextWithShadow(mc.textRenderer,
        Text.literal("No samples yet."),
        X, Y + 20, 0xFFFFFFFF);
      return;
    }

    // (Your real colored grid rendering can go here later.)
    // For now, show a simple dot so you can confirm it’s drawing.
    ctx.drawTextWithShadow(mc.textRenderer,
      Text.literal("• overlay drawing OK"),
      X, Y + 20, 0xFFAAFFAA);
  }
}
