package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;

public final class ChunkDataLocalCommandRouter {
	private ChunkDataLocalCommandRouter() {}

	// --- Intercept helpers ---

	// For ALLOW_COMMAND: string has NO leading slash.
	public static boolean shouldHandleNoSlash(String commandNoSlash) {
		String c = commandNoSlash.trim().toLowerCase();
		return c.equals("chunkdata") || c.startsWith("chunkdata ")
			|| c.equals("chuckdata") || c.startsWith("chuckdata "); // typo alias
	}

	public static void handleNoSlash(String commandNoSlash) {
		// Normalize typo
		String normalized = normalizeLeadingToken(commandNoSlash.trim());
		handleTokens(normalized.split("\\s+"));
	}

	// For ALLOW_CHAT: message may start with "/"
	public static boolean shouldHandleChatMessage(String msg) {
		String m = msg.trim().toLowerCase();
		return m.equals("/chunkdata") || m.startsWith("/chunkdata ")
			|| m.equals("/chuckdata") || m.startsWith("/chuckdata ");
	}

	public static void handleChatMessage(String msg) {
		String m = msg.trim();
		if (m.startsWith("/")) m = m.substring(1);
		m = normalizeLeadingToken(m);
		handleTokens(m.split("\\s+"));
	}

	private static String normalizeLeadingToken(String raw) {
		// Convert "chuckdata" -> "chunkdata"
		if (raw.equalsIgnoreCase("chuckdata")) return "chunkdata";
		if (raw.toLowerCase().startsWith("chuckdata ")) return "chunkdata " + raw.substring("chuckdata ".length());
		return raw;
	}

	// --- Actual command handling ---

	private static void handleTokens(String[] parts) {
		if (parts.length == 0) return;

		// parts[0] should be "chunkdata"
		if (parts.length == 1) {
			help();
			return;
		}

		String sub = parts[1].toLowerCase();

		switch (sub) {
			case "here" -> here();
			case "top" -> {
				int count = 10;
				if (parts.length >= 3) {
					try { count = Math.max(1, Math.min(50, Integer.parseInt(parts[2]))); }
					catch (NumberFormatException ignored) {}
				}
				top(count);
			}
			case "clear" -> {
				ChunkDataStore.clear();
				msg("Cleared stored results.");
			}
			case "heatmap" -> heatmap(parts);
			default -> help();
		}
	}

	private static void heatmap(String[] parts) {
		if (parts.length == 2) {
			msg("Heatmap is " + (ChunkDataConfig.HEATMAP_ENABLED ? "ON" : "OFF") +
				". Toggle display with H. Green-only is " + (ChunkDataConfig.ABNORMAL_ONLY_MODE ? "ON" : "OFF") + ".");
			return;
		}

		String a = parts[2].toLowerCase();

		if (a.equals("on") || a.equals("off") || a.equals("toggle")) {
			if (a.equals("toggle")) ChunkDataConfig.HEATMAP_ENABLED = !ChunkDataConfig.HEATMAP_ENABLED;
			else ChunkDataConfig.HEATMAP_ENABLED = a.equals("on");
			msg("Heatmap " + (ChunkDataConfig.HEATMAP_ENABLED ? "enabled" : "disabled") + ". Toggle display with H.");
			return;
		}

		// /chunkdata heatmap abnormalonly on|off|toggle
		if (a.equals("abnormalonly")) {
			if (parts.length < 4) {
				msg("Usage: /chunkdata heatmap abnormalonly on|off|toggle");
				return;
			}
			String v = parts[3].toLowerCase();
			if (v.equals("toggle")) ChunkDataConfig.ABNORMAL_ONLY_MODE = !ChunkDataConfig.ABNORMAL_ONLY_MODE;
			else if (v.equals("on")) ChunkDataConfig.ABNORMAL_ONLY_MODE = true;
			else if (v.equals("off")) ChunkDataConfig.ABNORMAL_ONLY_MODE = false;

			Text t = Text.literal("Green-only mode " + (ChunkDataConfig.ABNORMAL_ONLY_MODE ? "ON" : "OFF"));
			if (ChunkDataConfig.ABNORMAL_ONLY_MODE) t = t.copy().formatted(Formatting.GREEN);
			msg(t);
			return;
		}

		msg("Unknown heatmap option. Try: /chunkdata heatmap on|off|toggle");
	}

	private static void here() {
		var mc = MinecraftClient.getInstance();
		if (mc.player == null) return;
		ChunkPos pos = mc.player.getChunkPos();
		long bytes = ChunkDataStore.get(ChunkPos.toLong(pos.x, pos.z));
		msg("Chunk (" + pos.x + "," + pos.z + ") chunkDataBytes=" + ChunkDataStore.formatBytes(bytes) + "B");
	}

	private static void top(int count) {
		var mc = MinecraftClient.getInstance();
		ChunkPos center = (mc.player != null) ? mc.player.getChunkPos() : new ChunkPos(0, 0);
		var stats = ChunkDataAnalyzer.statsForArea(center, ChunkDataConfig.HEATMAP_RADIUS);

		msg("Top " + count + " tracked chunks. ABNORMAL = > mean + " + ChunkDataConfig.STD_MULT +
			"σ (samples=" + stats.samples() + ")");

		var rows = ChunkDataStore.top(count);
		for (int i = 0; i < rows.size(); i++) {
			var r = rows.get(i);
			int x = ChunkDataStore.packedX(r.pos());
			int z = ChunkDataStore.packedZ(r.pos());
			boolean abnormal = ChunkDataAnalyzer.isAbnormal(r.bytes(), stats);

			Text line = Text.literal((i + 1) + ") (" + x + "," + z + ") = " +
				ChunkDataStore.formatBytes(r.bytes()) + "B" + (abnormal ? "  [ABNORMAL]" : ""));

			if (abnormal) line = line.copy().formatted(Formatting.GREEN);
			msg(line);
		}
	}

	private static void help() {
		msg("Client-only commands:");
		msg(" /chunkdata here");
		msg(" /chunkdata top [count]");
		msg(" /chunkdata heatmap on|off|toggle");
		msg(" /chunkdata heatmap abnormalonly on|off|toggle");
		msg(" /chunkdata clear");
		msg("(Press H to toggle overlay.)");
	}

	private static void msg(String s) { msg(Text.literal(s)); }

	private static void msg(Text t) {
		var mc = MinecraftClient.getInstance();
		if (mc.player != null) mc.player.sendMessage(t, false);
	}
}
