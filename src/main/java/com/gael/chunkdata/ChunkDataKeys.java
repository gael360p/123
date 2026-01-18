package com.gael.chunkdata;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public final class ChunkDataKeys {
  private ChunkDataKeys() {}

  public static KeyBinding TOGGLE_HEATMAP;
  public static KeyBinding TOGGLE_DEBUG;
  public static KeyBinding DUMP_DEBUG;

  public static void init() {
    TOGGLE_HEATMAP = KeyBindingHelper.registerKeyBinding(new KeyBinding(
      "key.chunkdata_client.toggle_heatmap",
      GLFW.GLFW_KEY_H,
      "category.chunkdata_client"
    ));

    TOGGLE_DEBUG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
      "key.chunkdata_client.toggle_debug",
      GLFW.GLFW_KEY_F9,
      "category.chunkdata_client"
    ));

    DUMP_DEBUG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
      "key.chunkdata_client.dump_debug",
      GLFW.GLFW_KEY_F10,
      "category.chunkdata_client"
    ));
  }
}
