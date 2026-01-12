package com.gael.chunkdata;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;

public final class ChunkDataCommands {
  private ChunkDataCommands() {}

  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    dispatcher.register(ClientCommandManager.literal("chunkdata")
      .then(ClientCommandManager.literal("top")
        .executes(ctx -> top(ctx.getSource(), 10))
        .then(ClientCommandManager.argument("count", IntegerArgumentType.integer(1, 50))
          .executes(ctx -> top(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "count")))
        )
      )
      .then(ClientCommandManager.literal("clear")
        .executes(ctx -> { ChunkDataStore.clear(); ctx.getSource().sendFeedback(Text.literal("Cleared.")); return 1; })
      )
    );
  }

  private static int top(FabricClientCommandSource src, int count) {
    if (ChunkDataStore.size() == 0) {
      src.sendFeedback(Text.literal("No tracked chunks yet."));
      return 1;
    }
    var rows = ChunkDataStore.top(count);
    src.sendFeedback(Text.literal("Top " + rows.size() + " chunks:"));
    for (int i = 0; i < rows.size(); i++) {
      var r = rows.get(i);
      int x = ChunkPos.getPackedX(r.pos());
      int z = ChunkPos.getPackedZ(r.pos());
      src.sendFeedback(Text.literal((i+1) + ") (" + x + "," + z + ") = " + ChunkDataStore.formatBytes(r.bytes()) + "B"));
    }
    return 1;
  }
}
