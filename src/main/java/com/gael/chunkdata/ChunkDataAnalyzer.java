package com.gael.chunkdata;

import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public final class ChunkDataAnalyzer {
	private ChunkDataAnalyzer() {}

	public record Stats(double mean, double std, double threshold, int samples) {}

	public static Stats statsForArea(ChunkPos center, int radius) {
		List<Long> values = new ArrayList<>();
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dz = -radius; dz <= radius; dz++) {
				long key = ChunkPos.toLong(center.x + dx, center.z + dz);
				long b = ChunkDataStore.get(key);
				if (b >= 0) values.add(b);
			}
		}

		int n = values.size();
		if (n == 0) return new Stats(0, 0, Double.POSITIVE_INFINITY, 0);

		double mean = 0;
		for (long v : values) mean += v;
		mean /= n;

		double var = 0;
		for (long v : values) {
			double d = v - mean;
			var += d * d;
		}
		var /= n;

		double std = Math.sqrt(var);
		double threshold = mean + (ChunkDataConfig.STD_MULT * std);

		return new Stats(mean, std, threshold, n);
	}

	public static boolean isAbnormal(long bytes, Stats stats) {
		if (bytes < 0) return false;
		if (stats.samples < 12) return false;
		return bytes > stats.threshold;
	}
}
