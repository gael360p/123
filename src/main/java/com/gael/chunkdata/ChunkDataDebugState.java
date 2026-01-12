package com.gael.chunkdata;

public final class ChunkDataDebugState {
  private ChunkDataDebugState() {}

  public static boolean DEBUG_ENABLED = false;

  // proves whether mixins are running at all
  public static volatile long mixinPingTicks = 0;

  // tracking counters
  public static volatile long onChunkDataCalls = 0;
  public static volatile long measuredWriteCalls = 0;

  public static volatile int lastChunkX = 0;
  public static volatile int lastChunkZ = 0;
  public static volatile long lastBytes = -1;
  public static volatile String lastError = "";

  public static void record(int cx, int cz, long bytes) {
    lastChunkX = cx;
    lastChunkZ = cz;
    lastBytes = bytes;
  }

  public static void error(Throwable t) {
    lastError = t.getClass().getSimpleName() + ": " + (t.getMessage() == null ? "" : t.getMessage());
  }
}
