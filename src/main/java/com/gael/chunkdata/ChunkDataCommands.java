package com.gael.chunkdata;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

/**
 * Minimal client command registration so ChunkDataClientMod can compile.
 * Extend with real behavior as needed.
 */
public final class ChunkDataCommands {
    private ChunkDataCommands() {}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal("chunkdata")
                .executes(ctx -> {
                    // Client-side behavior can be added here if desired.
                    return 1;
                })
        );
    }
}
