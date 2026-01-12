package com.gael.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class ChunkDataStore {
    private static final Long2LongOpenHashMap BYTES_BY_CHUNK = new Long2LongOpenHashMap();

    private ChunkDataStore() {}

    public static void put(long chunkPos, long bytes) {
        BYTES_BY_CHUNK.put(chunkPos, bytes);
    }

    public static long get(long chunkPos) {
        return BYTES_BY_CHUNK.getOrDefault(chunkPos, -1);
    }

    public static int size() {
        return BYTES_BY_CHUNK.size();
    }

    public static void clear() {
        BYTES_BY_CHUNK.clear();
    }

    public static List<Long> top(int count) {
        return BYTES_BY_CHUNK.values()
                             .stream()
                             .sorted(Comparator.reverseOrder())
                             .limit(count)
                             .collect(Collectors.toList());
    }

    public static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format("%.1f KB", kb);
        double mb = kb / 1024.0;
        return String.format("%.1f MB", mb);
    }

    public static void forEachBytes(java.util.function.Consumer<Long> action) {
        BYTES_BY_CHUNK.long2LongEntrySet().forEach(entry -> action.accept(entry.getLongValue()));
    }
}
