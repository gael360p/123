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
