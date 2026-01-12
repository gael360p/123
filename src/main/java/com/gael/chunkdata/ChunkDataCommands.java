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
        .executes(ctx -> {
          MinecraftClient mc = MinecraftClient.getInstance();
          if (mc.player == null) return 0;
          ChunkPos pos = mc.player.getChunkPos();
          long bytes = ChunkDataStore.get(ChunkPos.toLong(pos.x, pos.z));
          ctx.getSource().sendFeedback(Text.literal("Here: chunk(" + pos.x + "," + pos.z + ") bytes=" + ChunkDataStore.formatBytes(bytes) + "B"));
          return 1;
        })
      )
      .then(ClientCommandManager.literal("top")
        .executes(ctx -> top(ctx.getSource(), 10))
        .then(ClientCommandManager.argument("count", IntegerArgumentType.integer(1, 50))
          .executes(ctx -> top(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "count")))
        )
      )
      .then(ClientCommandManager.literal("clear")
        .executes(ctx -> {
          ChunkDataStore.clear();
          ctx.getSource().sendFeedback(Text.literal("Cleared tracked chunks."));
          return 1;
        })
      )
      .then(ClientCommandManager.literal("heatmap")
        .then(ClientCommandManager.literal("on").executes(ctx -> {
          ChunkDataConfig.HEATMAP_ENABLED = true;
          ctx.getSource().sendFeedback(Text.literal("Heatmap enabled."));
          return 1;
        }))
        .then(ClientCommandManager.literal("off").executes(ctx -> {
          ChunkDataConfig.HEATMAP_ENABLED = false;
          ChunkDataConfig.HEATMAP_TOGGLED = false;
          ctx.getSource().sendFeedback(Text.lit

set -e
git checkout main
git pull

mkdir -p backup
TS="$(date +%Y%m%d_%H%M%S)"
for f in gradle.properties src/main/resources/chunkdata_client.mixins.json src/main/java/com/gael/chunkdata/mixin/MixinPingMinecraftClient.java src/main/java/com/gael/chunkdata/ChunkDataCommands.java; do
  if [ -f "$f" ]; then
    mkdir -p "backup/$(dirname "$f")"
    cp "$f" "backup/$f.$TS.bak"
  fi
done

# 1) Match your installed Fabric API version (you have 0.118.0+1.21.4)
# This prevents weird mismatches when you build.
if grep -q "^fabric_version=" gradle.properties; then
  sed -i 's/^fabric_version=.*/fabric_version=0.118.0+1.21.4/' gradle.properties
else
  echo "fabric_version=0.118.0+1.21.4" >> gradle.properties
fi

# 2) Make mixins OPTIONAL and ONLY load the safe "ping" mixin.
# This prevents crashes if any tracking mixin is broken.
cat > src/main/resources/chunkdata_client.mixins.json <<'EOF'
{
  "required": false,
  "package": "com.gael.chunkdata.mixin",
  "compatibilityLevel": "JAVA_21",
  "client": [
    "MixinPingMinecraftClient"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
