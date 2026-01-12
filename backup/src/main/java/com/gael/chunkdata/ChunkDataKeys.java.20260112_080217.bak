package com.gael.chunkdata;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ChunkDataKeys {
	private ChunkDataKeys() {}

	public static KeyBinding SHOW_HEATMAP;

	public static void init() {
		SHOW_HEATMAP = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
				"key.chunkdata_client.show_heatmap",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.chunkdata_client"
			)
		);
	}
}
