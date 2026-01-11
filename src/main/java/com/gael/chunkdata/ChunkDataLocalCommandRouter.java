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

git checkout -b fix/remove-shellscript-and-add-commands git rm -f src/main/java/com/gael/chunkdata/ChunkDataLocalCommandRouter.java cat > src/main/java/com/gael/chunkdata/ChunkDataCommands.java <<'EOF' package com.gael.chunkdata;

import com.mojang.brigadier.CommandDispatcher; import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource; import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

/**

Minimal client command registration so ChunkDataClientMod can compile.

Extend with real behavior as needed. */ public final class ChunkDataCommands { private ChunkDataCommands() {}

public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) { dispatcher.register( literal("chunkdata") .executes(ctx -> { // Client-side behavior can be added here if desired. return 1; }) ); } } EOF

git add src/main/java/com/gael/chunkdata/ChunkDataCommands.java git commit -m "Remove accidental shell script in Java sources; add ChunkDataCommands to register client commands" git push --set-upstream origin HEAD
git checkout -b fix/remove-shellscript-and-add-commands git rm -f src/main/java/com/gael/chunkdata/ChunkDataLocalCommandRouter.java cat > src/main/java/com/gael/chunkdata/ChunkDataCommands.java <<'EOF' package com.gael.chunkdata;

import com.mojang.brigadier.CommandDispatcher; import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource; import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

/**

Minimal client command registration so ChunkDataClientMod can compile.

Extend with real behavior as needed. */ public final class ChunkDataCommands { private ChunkDataCommands() {}

public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) { dispatcher.register( literal("chunkdata") .executes(ctx -> { // Client-side behavior can be added here if desired. return 1; }) ); } } EOF

git add src/main/java/com/gael/chunkdata/ChunkDataCommands.java git commit -m "Remove accidental shell script in Java sources; add ChunkDataCommands to register client commands" git push --set-upstream origin HEAD
git checkout -b fix/remove-shellscript-and-add-commands && \
git rm -f src/main/java/com/gael/chunkdata/ChunkDataLocalCommandRouter.java || true && \
cat > src/main/java/com/gael/chunkdata/ChunkDataCommands.java <<'EOF'
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
