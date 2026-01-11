package com.gael.chunkdata.mixin;

import com.gael.chunkdata.ChunkDataDebugState;
import com.gael.chunkdata.ChunkDataStore;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * In Yarn 1.21.4, ChunkDataS2CPacket has a private constructor ChunkDataS2CPacket(RegistryByteBuf buf). :contentReference[oaicite:2]{index=2}
 * We measure bytes consumed from the buf readerIndex during decoding.
 */
@Mixin(ChunkDataS2CPacket.class)
public class ChunkDataS2CPacketSizeMixin {

	@Unique private int chunkdata_client$startIndex = -1;

	@Inject(method = "<init>(Lnet/minecraft/network/RegistryByteBuf;)V", at = @At("HEAD"))
	private void chunkdata_client$head(RegistryByteBuf buf, CallbackInfo ci) {
		// RegistryByteBuf extends PacketByteBuf/ByteBuf, so readerIndex is available. :contentReference[oaicite:3]{index=3}
		chunkdata_client$startIndex = buf.readerIndex();
	}

	@Inject(method = "<init>(Lnet/minecraft/network/RegistryByteBuf;)V", at = @At("RETURN"))
	private void chunkdata_client$return(RegistryByteBuf buf, CallbackInfo ci) {
		try {
			int end = buf.readerIndex();
			long bytes = (chunkdata_client$startIndex >= 0) ? (long)(end - chunkdata_client$startIndex) : -1L;

			ChunkDataS2CPacket self = (ChunkDataS2CPacket)(Object)this;
			int cx = self.getChunkX(); // getter exists in 1.21.4 :contentReference[oaicite:4]{index=4}
			int cz = self.getChunkZ(); // getter exists in 1.21.4 :contentReference[oaicite:5]{index=5}

			if (bytes < 0) bytes = 0;
			ChunkDataStore.put(ChunkPos.toLong(cx, cz), bytes);
			ChunkDataDebugState.recordPacket(cx, cz, bytes);
		} catch (Throwable t) {
			ChunkDataDebugState.recordError(t);
		}
	}
}
