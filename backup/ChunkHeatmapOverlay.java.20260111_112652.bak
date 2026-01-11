package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.ChunkPos;

public final class ChunkHeatmapOverlay {
	private ChunkHeatmapOverlay() {}

	public static void render(DrawContext ctx) {
		if (!ChunkDataConfig.HEATMAP_ENABLED) return;
		if (ChunkDataKeys.SHOW_HEATMAP == null || !ChunkDataKeys.SHOW_HEATMAP.isPressed()) return;

		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null) return;

		ChunkPos center = mc.player.getChunkPos();
		int r = ChunkDataConfig.HEATMAP_RADIUS;
		int cell = ChunkDataConfig.CELL_SIZE;
		int margin = ChunkDataConfig.MARGIN;

		int cells = (r * 2) + 1;
		int width = cells * cell;
		int height = cells * cell;

		int screenW = mc.getWindow().getScaledWidth();
		int x0 = screenW - margin - width;
		int y0 = margin;

		int panelAlpha = ChunkDataConfig.ABNORMAL_ONLY_MODE ? 0x18 : ChunkDataConfig.PANEL_ALPHA;
		ctx.fill(x0 - 2, y0 - 2, x0 + width + 2, y0 + height + 2, argb(panelAlpha, 0, 0, 0));
		ctx.drawBorder(x0 - 2, y0 - 2, width + 4, height + 4, argb(0x50, 255, 255, 255));

		var stats = ChunkDataAnalyzer.statsForArea(center, r);

		double minLog = Double.POSITIVE_INFINITY;
		double maxLog = Double.NEGATIVE_INFINITY;

		if (!ChunkDataConfig.ABNORMAL_ONLY_MODE) {
			for (int dx = -r; dx <= r; dx++) {
				for (int dz = -r; dz <= r; dz++) {
					long b = ChunkDataStore.get(ChunkPos.toLong(center.x + dx, center.z + dz));
					if (b < 0) continue;
					double v = Math.log10(b + 1.0);
					minLog = Math.min(minLog, v);
					maxLog = Math.max(maxLog, v);
				}
			}
			if (!Double.isFinite(minLog) || !Double.isFinite(maxLog) || minLog == maxLog) {
				minLog = 0;
				maxLog = 1;
			}
		}

		for (int dz = -r; dz <= r; dz++) {
			for (int dx = -r; dx <= r; dx++) {
				int cx = center.x + dx;
				int cz = center.z + dz;
				long key = ChunkPos.toLong(cx, cz);
				long bytes = ChunkDataStore.get(key);

				boolean abnormal = ChunkDataAnalyzer.isAbnormal(bytes, stats);

				int px = x0 + (dx + r) * cell;
				int py = y0 + (dz + r) * cell;

				if (bytes < 0) {
					if (!ChunkDataConfig.ABNORMAL_ONLY_MODE) {
						ctx.fill(px, py, px + cell, py + cell, argb(0x20, 20, 20, 20));
					}
					continue;
				}

				if (abnormal) {
					ctx.fill(px, py, px + cell, py + cell, argb(0x55, 0, 255, 0));
					ctx.drawBorder(px, py, cell, cell, argb(0xFF, 0, 255, 0));
					continue;
				}

				if (ChunkDataConfig.ABNORMAL_ONLY_MODE) continue;

				double v = Math.log10(bytes + 1.0);
				double t = (v - minLog) / (maxLog - minLog);
				t = clamp01(t);

				int gray = (int) (30 + t * 170);
				ctx.fill(px, py, px + cell, py + cell, argb(0x35, gray, gray, gray));
			}
		}

		int pcx = x0 + r * cell;
		int pcy = y0 + r * cell;
		ctx.drawBorder(pcx, pcy, cell, cell, argb(0xFF, 255, 255, 255));
	}

	private static int argb(int a, int r, int g, int b) {
		return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
	}

	private static double clamp01(double x) {
		if (x < 0) return 0;
		if (x > 1) return 1;
		return x;
	}
}
