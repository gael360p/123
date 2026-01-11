package com.gael.chunkdata;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ChunkDataClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ChunkDataKeys.init();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
			ChunkDataCommands.register(dispatcher)
		);

		HudRenderCallback.EVENT.register((drawContext, tickCounter) ->
			ChunkHeatmapOverlay.render(drawContext)
		);

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ChunkDataStore.clear());
	}
}
