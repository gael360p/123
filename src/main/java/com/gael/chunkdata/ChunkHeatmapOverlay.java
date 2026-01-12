package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class ChunkHeatmapOverlay {
  private ChunkHeatmapOverlay() {}

  public static boolean HEATMAP_TOGGLED = false;

  public static void render(DrawContext ctx) {
    if (!HEATMAP_TOGGLED) return;
    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc.player == null) return;

    ctx.drawTextWithShadow(
      mc.textRenderer,
      Text.literal("Heatmap ON | tracked=" + ChunkDataStore.size()),
      8, 110, 0xFFFFFFFF
    );
  }
}
