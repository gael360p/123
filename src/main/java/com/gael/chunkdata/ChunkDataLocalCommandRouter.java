package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class ChunkDataLocalCommandRouter {
	private ChunkDataLocalCommandRouter() {}

	public static boolean isOurCommand(String cmdNoSlash) {
		String c = cmdNoSlash.trim().toLowerCase();
		return c.equals("chunkdata") || c.startsWith("chunkdata ")
			|| c.equals("chuckdata") || c.startsWith("chuckdata ")
			|| c.equals("chunckdata") || c.startsWith("chunckdata ");
	}

	public static void run(String cmdNoSlash) {
		String[] p = cmdNoSlash.trim().split("\\s+");
		if (p.length == 0) return;
		if (p.length == 1) { help(); return; }

		String sub = p[1].toLowerCase();
		switch (sub) {
			case "top" -> top(parseInt(p, 2, 10));
			case "clear" -> { ChunkDataStore.clear(); msg("Cleared tracked chunks."); }
			case "debug" -> debug(p);
			default -> help();
		}
	}

	private static void debug(String[] p) {
		String a = (p.length >= 3) ? p[2].toLowerCase() : "toggle";
		if (a.equals("on")) ChunkDataDebugState.DEBUG_ENABLED = true;
		else if (a.equals("off")) ChunkDataDebugState.DEBUG_ENABLED = false;
		else if (a.equals("dump")) {
			msg("trackedChunks=" + ChunkDataStore.size());
			msg("ctorPackets=" + ChunkDataDebugState.ctorPackets + " handlerPackets=" + ChunkDataDebugState.handlerPackets + " measuredWrite=" + ChunkDataDebugState.measuredWritePackets);
			msg("lastHook=" + ChunkDataDebugState.lastHook + " lastChunk=(" + ChunkDataDebugState.lastChunkX + "," + ChunkDataDebugState.lastChunkZ + ") lastBytes=" + ChunkDataStore.formatBytes(ChunkDataDebugState.lastBytes) + "B");
			if (!ChunkDataDebugState.lastError.isEmpty()) msg("error=" + ChunkDataDebugState.lastError);
			return;
		} else {
			ChunkDataDebugState.DEBUG_ENABLED = !ChunkDataDebugState.DEBUG_ENABLED;
		}
		msg("Debug " + (ChunkDataDebugState.DEBUG_ENABLED ? "ON" : "OFF") + " (F9 toggles too)");
	}

	private static void top(int n) {
		if (ChunkDataStore.size() == 0) {
			msg("No tracked chunks yet. Turn on debug (F9) and re-log.");
			return;
		}
		var rows = ChunkDataStore.top(Math.max(1, Math.min(50, n)));
		msg("Top " + rows.size() + " chunks:");
		for (int i = 0; i < rows.size(); i++) {
			var r = rows.get(i);
			msg((i+1) + ") pos=" + r.pos() + " bytes=" + ChunkDataStore.formatBytes(r.bytes()) + "B");
		}
	}

	private static int parseInt(String[] p, int idx, int def) {
		if (p.length <= idx) return def;
		try { return Integer.parseInt(p[idx]); } catch (Exception e) { return def; }
	}

	private static void help() {
		msg("Commands: /chunkdata top [n] | /chunkdata clear | /chunkdata debug on|off|toggle|dump");
	}

	private static void msg(String s) {
		var mc = MinecraftClient.getInstance();
		if (mc.player != null) mc.player.sendMessage(Text.literal(s), false);
	}
}
