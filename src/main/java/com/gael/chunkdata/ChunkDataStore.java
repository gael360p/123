package com.gael.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.function.Consumer;

public final class ChunkDataStore {
    private static final Long2LongOpenHashMap BYTES_BY_CHUNK = new Long2LongOpenHashMap();

    private ChunkDataStore() {}

    // Existing methods
    public static void put(long chunkPos, long bytes) {
        BYTES_BY_CHUNK.put(chunkPos, bytes);
    }

    public static long get(long chunkPos) {
        return BYTES_BY_CHUNK.getOrDefault(chunkPos, -1);
    }

    public static int size() {
        return BYTES_BY_CHUNK.size();
    }

    // Add the missing method
    public static void forEachBytes(Consumer<Long> action) {
        BYTES_BY_CHUNK.long2LongEntrySet().forEach(entry -> action.accept(entry.getLongValue()));
    }
}
