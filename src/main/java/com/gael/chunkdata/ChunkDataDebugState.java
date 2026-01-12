package com.gael.chunkdata;

public final class ChunkDataDebugState {
    private ChunkDataDebugState() {}

    public static boolean DEBUG_ENABLED = false;

    public static volatile long ctorPackets = 0;
    public static volatile long handlerPackets = 0;
    public static volatile long measuredWritePackets = 0;

    public static volatile int lastChunkX = 0;
    public static volatile int lastChunkZ = 0;
    public static volatile long lastBytes = -1;
    public static volatile String lastHook = "";
    public static volatile String lastError = "";

    public static void record(String hook, int cx, int cz, long bytes) {
        lastHook = hook;
        lastChunkX = cx;
        lastChunkZ = cz;
        lastBytes = bytes;
    }

    public static void error(Throwable t) {
        lastError = t.getClass().getSimpleName() + ": " + (t.getMessage() == null ? "" : t.getMessage());
    }

    // Add the missing methods
    public static void recordPacket(int cx, int cz, long bytes) {
        record("PacketRecorded", cx, cz, bytes);
    }

    public static void recordError(Throwable t) {
        error(t);
    }
}
