package com.gael.chunkdata;

import com.mojang.brigadier.CommandDispatcher;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

/**
 * Minimal client command registration so ChunkDataClientMod can compile.
 * Expand this with real commands / behavior as needed.
 */
public final class ChunkDataCommands {
    private ChunkDataCommands() {}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Register a no-op "chunkdata" command. Replace / extend with actual commands.
        dispatcher.register(
            literal("chunkdata")
                .executes(ctx -> {
                    // Optionally send client-side feedback here via ctx.getSource()
                    return 1;
                })
        );
    }
}
