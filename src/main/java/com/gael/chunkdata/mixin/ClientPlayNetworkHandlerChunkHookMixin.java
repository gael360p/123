package com.gael.chunkdata.mixin;

import com.gael.chunkdata.ChunkDataDebugState;
import com.gael.chunkdata.ChunkDataStore;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerChunkHookMixin {
  @Inject(method = "onChunkData", at = @At("HEAD"))
  private void chunkdata_client$onChunkData(ChunkDataS2CPacket packet, CallbackInfo ci) {
    ChunkDataDebugState.onChunkDataCalls++;
    try {
      var raw = Unpooled.buffer();
      try {
        var handler = (ClientPlayNetworkHandler)(Object)this;
        RegistryByteBuf buf = new RegistryByteBuf(raw, handler.getRegistryManager());
        ((ChunkDataS2CPacketWriteInvoker)(Object)packet).chunkdata_client$write(buf);
        long bytes = raw.readableBytes();

        ChunkDataDebugState.measuredWriteCalls++;
        ChunkDataDebugState.record(packet.getChunkX(), packet.getChunkZ(), bytes);

        ChunkDataStore.put(ChunkPos.toLong(packet.getChunkX(), packet.getChunkZ()), bytes);
      } finally {
        raw.release();
      }
    } catch (Throwable t) {
      ChunkDataDebugState.error(t);
    }
  }
}
