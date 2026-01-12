package com.gael.chunkdata.mixin;

import com.gael.chunkdata.ChunkDataDebugState;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinPingMinecraftClient {
  @Inject(method = "tick", at = @At("HEAD"))
  private void chunkdata_client$ping(CallbackInfo ci) {
    ChunkDataDebugState.mixinPingTicks++;
  }
}
