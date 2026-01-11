package com.gael.chunkdata;

public final class ChunkDataDebugState {
	private ChunkDataDebugState() {}

	public static boolean DEBUG_ENABLED = false;

	public static volatile long packetsSeen = 0;
	public static volatile long bytesLast = -1;
	public static volatile int lastChunkX = 0;
	public static volatile int lastChunkZ = 0;
	public static volatile long lastTimeMs = 0;
	public static volatile String lastThread = "";
	public static volatile String lastError = "";

	public static void recordPacket(int cx, int cz, long bytes) {
		packetsSeen++;
		lastChunkX = cx;
		lastChunkZ = cz;
		bytesLast = bytes;
		lastTimeMs = System.currentTimeMillis();
		lastThread = Thread.currentThread().getName();
	}

	public static void recordError(Throwable t) {
		lastError = t.getClass().getSimpleName() + ": " + (t.getMessage() == null ? "" : t.getMessage());
	}
}
