package com.gael.chunkdata;

public final class ChunkDataConfig {
    private ChunkDataConfig() {}

    // master enable
    public static boolean HEATMAP_ENABLED = true;

    // toggled by H and by command
    public static boolean HEATMAP_TOGGLED = false;

    // Add the missing multiplier constant
    public static final double STD_MULT = 1.5; // Adjust value as appropriate
}
