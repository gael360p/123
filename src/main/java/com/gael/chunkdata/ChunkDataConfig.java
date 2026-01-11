package com.gael.chunkdata;

public final class ChunkDataConfig {
	private ChunkDataConfig() {}

	public static boolean HEATMAP_ENABLED = true;

	// If true, ONLY abnormal chunks are drawn (green-only mode)
	public static boolean ABNORMAL_ONLY_MODE = false;

	public static int HEATMAP_RADIUS = 6;
	public static int CELL_SIZE = 5;
	public static int MARGIN = 8;

	public static double STD_MULT = 2.0;
	public static int PANEL_ALPHA = 0x40;
}
