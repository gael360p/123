package com.gael.chunkdata;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;

public class ChunkDataClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ChunkDataKeys.init();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			while (ChunkDataKeys.TOGGLE_HEATMAP.wasPressed()) {
				ChunkDataConfig.HEATMAP_TOGGLED = !ChunkDataConfig.HEATMAP_TOGGLED;
				client.player.sendMessage(Text.literal("Heatmap " + (ChunkDataConfig.HEATMAP_TOGGLED ? "ON" : "OFF")), false);
			}

			while (ChunkDataKeys.TOGGLE_DEBUG.wasPressed()) {
				ChunkDataDebugState.DEBUG_ENABLED = !ChunkDataDebugState.DEBUG_ENABLED;
				client.player.sendMessage(Text.literal("Debug " + (ChunkDataDebugState.DEBUG_ENABLED ? "ON" : "OFF")), false);
			}

			while (ChunkDataKeys.DUMP_DEBUG.wasPressed()) {
				client.player.sendMessage(Text.literal("DEBUG dump: tracked=" + ChunkDataStore.size()
					+ " ctor=" + ChunkDataDebugState.ctorPackets
					+ " handler=" + ChunkDataDebugState.handlerPackets
					+ " write=" + ChunkDataDebugState.measuredWritePackets
					+ " lastHook=" + ChunkDataDebugState.lastHook
					+ " lastBytes=" + ChunkDataStore.formatBytes(ChunkDataDebugState.lastBytes) + "B"), false);
				if (!ChunkDataDebugState.lastError.isEmpty()) {
					client.player.sendMessage(Text.literal("DEBUG error: " + ChunkDataDebugState.lastError), false);
				}
			}
		});

		HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
			ChunkHeatmapOverlay.render(drawContext);
			ChunkDebugOverlay.render(drawContext);
		});
	}
}
