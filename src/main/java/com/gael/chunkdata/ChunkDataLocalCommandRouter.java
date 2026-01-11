package com.gael.chunkdata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;

public final class ChunkDataLocalCommandRouter {
	private ChunkDataLocalCommandRouter() {}

	public static void handle(String commandNoSlash) {
		String[] parts = commandNoSlash.trim().split("\\s+");
		if (parts.length == 0) return;

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
				" (hold H). Green-only is " + (ChunkDataConfig.ABNORMAL_ONLY_MODE ? "ON" : "OFF") + ".");
			return;
		}

		String a = parts[2].toLowerCase();

		if (a.equals("on") || a.equals("off") || a.equals("toggle")) {
			if (a.equals("toggle")) ChunkDataConfig.HEATMAP_ENABLED = !ChunkDataConfig.HEATMAP_ENABLED;
			else ChunkDataConfig.HEATMAP_ENABLED = a.equals("on");
			msg("Heatmap " + (ChunkDataConfig.HEATMAP_ENABLED ? "enabled" : "disabled") + " (hold H).");
			return;
		}

		if (a.equals("abnormalonly")) {
			if (parts.length < 4) {
				msg("Usage: /chunkdata heatmap abnormalonly on|off|toggle");
				return;
			}
			String v = parts[3].toLowerCase();
			if (v.equals("toggle")) ChunkDataConfig.ABNORMAL_ONLY_MODE = !ChunkDataConfig.ABNORMAL_ONLY_MODE;
			else if (v.equals("on")) ChunkDataConfig.ABNORMAL_ONLY_MODE = true;
			else if (v.equals("off")) ChunkDataConfig.ABNORMAL_ONLY_MODE = false;

			Text t = Text.literal("Green-only mode " + (Chun

set -e

echo "== Pull latest =="
git pull

echo "== Make folders =="
mkdir -p src/main/java/com/gael/chunkdata
mkdir -p src/main/java/com/gael/chunkdata/mixin

echo "== Update ChunkDataClientMod.java (adds client command interception) =="
cat > src/main/java/com/gael/chunkdata/ChunkDataClientMod.java <<'EOF'
package com.gael.chunkdata;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;

public class ChunkDataClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ChunkDataKeys.init();

		// Keep existing client commands (works in singleplayer / some servers)
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
			ChunkDataCommands.register(dispatcher)
		);

		// Make /chunkdata work on ANY server by handling it client-side before it reaches the server.
		// The string has NO leading slash. Example: "chunkdata top 10"
		ClientSendMessageEvents.ALLOW_COMMAND.register(command -> {
			if (command.equals("chunkdata") || command.startsWith("chunkdata ")) {
				ChunkDataLocalCommandRouter.handle(command);
				return false; // block sending to server
			}
			return true;
		});

		HudRenderCallback.EVENT.register((drawContext, tickCounter) ->
			ChunkHeatmapOverlay.render(drawContext)
		);

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ChunkDataStore.clear());
	}
}
