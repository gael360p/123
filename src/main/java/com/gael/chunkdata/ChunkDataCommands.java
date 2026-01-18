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