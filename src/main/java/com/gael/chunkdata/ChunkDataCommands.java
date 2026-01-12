package com.gael.chunkdata;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;

public final class ChunkDataCommands {
  private ChunkDataCommands() {}

  public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
    // main command
    dispatcher.register(ClientCommandManager.literal("chunkdata")
      .then(ClientCommandManager.literal("here")
        .executes(ctx -> here(ctx.getSource()))
      )
      .then(ClientCommandManager.literal("top")
        .executes(ctx -> top(ctx.getSource(), 10))
        .then(ClientCommandManager.argument("count", IntegerArgumentType.integer(1, 50))
          .executes(ctx -> top(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "count")))
        )
      )
      .then(ClientCommandManager.literal("clear")
        .executes(ctx -> { ChunkDataStore.clear(); ctx.getSource().sendFeedback(Text.literal("Cleared tracked chunks.")); return 1; })
      )
      .then(ClientCommandManager.literal("heatmap")
        .then(ClientCommandManager.literal("on").executes(ctx -> { ChunkDataConfig.HEATMAP_ENABLED = true; ctx.getSource().sendFeedback(Text.literal("Heatmap enabled.")); return 1; }))
        .then(ClientCommandManager.literal("off").executes(ctx -> { ChunkDataConfig.HEATMAP_ENABLED = false; ChunkDataConfig.HEATMAP_TOGGLED = false; ctx.getSource().sendFeedback(Text.literal("Heatmap disabled.")); return 1; }))
        .then(ClientCommandManager.literal("toggle").executes(ctx -> {
          if (!ChunkDataConfig.HEATMAP_ENABLED) {
            ChunkDataConfig.HEATMAP_ENABLED = true;
            ChunkDataConfig.HEATMAP_TOGGLED = true;
          } else {
            ChunkDataConfig.HEATMAP_TOGGLED = !ChunkDataConfig.HEATMAP_TOGGLED;
          }
          ctx.getSource().sendFeedback(Text.literal("Heatmap " + (ChunkDataConfig.HEATMAP_TOGGLED ? "ON" : "OFF")));
          return 1;
        }))
      )
      .then(ClientCommandManager.literal("debug")
        .then(ClientCommandManager.literal("on").executes(ctx -> { ChunkDataDebugState.DEBUG_ENABLED = true; ctx.getSource().sendFeedback(Text.literal("Debug ON (F9 toggles).")); return 1; }))
        .then(ClientCommandManager.literal("off").executes(ctx -> { ChunkDataDebugState.DEBUG_ENABLED = false; ctx.getSource().sendFeedback(Text.literal("Debug OFF.")); return 1; }))
        .then(ClientCommandManager.literal("toggle").executes(ctx -> {
          ChunkDataDebugState.DEBUG_ENABLED = !ChunkDataDebugState.DEBUG_ENABLED;
          ctx.getSource().sendFeedback(Text.literal("Debug " + (ChunkDataDebugState.DEBUG_ENABLED ? "ON" : "OFF")));
          return 1;
        }))
        .then(ClientCommandManager.literal("dump").executes(ctx -> {
          dump(ctx.getSource());
          return 1;
        }))
      )
    );

    // alias because you typed chunkbase
    dispatcher.register(ClientCommandManager.literal("chunkbase")
      .then(ClientCommandManager.literal("top")
        .executes(ctx -> top(ctx.getSource(), 10))
        .then(ClientCommandManager.argument("count", IntegerArgumentType.integer(1, 50))
          .executes(ctx -> top(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "count")))
        )
      )
      .then(ClientCommandManager.literal("clear")
        .executes(ctx -> { ChunkDataStore.clear(); ctx.getSource().sendFeedback(Text.literal("Cleared tracked chunks.")); return 1; })
      )
    );
  }

  private static int here(FabricClientCommandSource src) {
    MinecraftClient mc = MinecraftClient.getInstance();
    if (mc.player == null) return 0;
    ChunkPos pos = mc.player.getChunkPos();
    long bytes = ChunkDataStore.get(ChunkPos.toLong(pos.x, pos.z));
    src.sendFeedback(Text.literal("Here: chunk(" + pos.x + "," + pos.z + ") bytes=" + ChunkDataStore.formatBytes(bytes) + "B"));
    return 1;
  }

  private static int top(FabricClientCommandSource src, int count) {
    if (ChunkDataStore.size() == 0) {
      src.sendFeedback(Text.literal("No tracked chunks yet (tracking is currently 0)."));
      src.sendFeedback(Text.literal("Tip: turn debug on with F9 and press F10 to dump."));
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

  private static void dump(FabricClientCommandSource src) {
    src.sendFeedback(Text.literal("DEBUG dump:"));
    src.sendFeedback(Text.literal(" trackedChunks=" + ChunkDataStore.size()));
    src.sendFeedback(Text.literal(" mixinPingTicks=" + ChunkDataDebugState.mixinPingTicks));
    src.sendFeedback(Text.literal(" onChunkDataCalls=" + ChunkDataDebugState.onChunkDataCalls));
    src.sendFeedback(Text.literal(" measuredWriteCalls=" + ChunkDataDebugState.measuredWriteCalls));
    src.sendFeedback(Text.literal(" lastBytes=" + ChunkDataStore.formatBytes(ChunkDataDebugState.lastBytes) + "B"));
    if (!ChunkDataDebugState.lastError.isEmpty()) src.sendFeedback(Text.literal(" lastError=" + ChunkDataDebugState.lastError));
  }
}
