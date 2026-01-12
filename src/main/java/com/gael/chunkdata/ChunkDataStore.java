// Updated source file: ChunkDataStore.java
package com.gael.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ChunkDataStore {
    private static final Long2LongOpenHashMap BYTES_BY_CHUNK = new Long2LongOpenHashMap();

    private ChunkDataStore() {}

    public static void put(long chunkPosLong, long bytes) {
        BYTES_BY_CHUNK.put(chunkPosLong, bytes);
    }

    public static long get(long chunkPosLong) {
        return BYTES_BY_CHUNK.getOrDefault(chunkPosLong, -1);
    }

    public static int size() {
        return BYTES_BY_CHUNK.size();
    }

    public static void clear() {
        BYTES_BY_CHUNK.clear();
    }

    public record Row(long pos, long bytes) {}

    public static List<Row> top(int count) {
        List<Row> rows = new ArrayList<>(BYTES_BY_CHUNK.size());
        for (Long2LongMap.Entry e : BYTES_BY_CHUNK.long2LongEntrySet()) {
            rows.add(new Row(e.getLongKey(), e.getLongValue()));
        }
        rows.sort(Comparator.comparingLong(Row::bytes).reversed());
        if (rows.size() > count) return rows.subList(0, count);
        return rows;
    }

    public static String formatBytes(long bytes) {
        if (bytes < 0) return "unknown";
        if (bytes < 1024) return Long.toString(bytes);
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format(java.util.Locale.ROOT, "%.1fK", kb);
        double mb = kb / 1024.0;
        if (mb < 1024) return String.format(java.util.Locale.ROOT, "%.1fM", mb);
        double gb = mb / 1024.0;
        return String.format(java.util.Locale.ROOT, "%.2fG", gb);
    }

    public static void forEachBytes(java.util.function.Consumer<Long> action) {
        BYTES_BY_CHUNK.values().forEach(action::accept);
    }
}
