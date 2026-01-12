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

@Mixin(ChunkDataS2CPacket.class)
public class ChunkDataS2CPacketCtorSizeMixin {
	@Unique private int chunkdata_client$start = -1;

	@Inject(method = "<init>(Lnet/minecraft/network/RegistryByteBuf;)V", at = @At("HEAD"))
	private void chunkdata_client$head(RegistryByteBuf buf, CallbackInfo ci) {
		chunkdata_client$start = buf.readerIndex();
	}

	@Inject(method = "<init>(Lnet/minecraft/network/RegistryByteBuf;)V", at = @At("RETURN"))
	private void chunkdata_client$return(RegistryByteBuf buf, CallbackInfo ci) {
		try {
			ChunkDataS2CPacket self = (ChunkDataS2CPacket)(Object)this;
			long bytes = Math.max(0, buf.readerIndex() - chunkdata_client$start);

			int cx = self.getChunkX();
			int cz = self.getChunkZ();

			ChunkDataDebugState.ctorPackets++;
			ChunkDataDebugState.record("CTOR", cx, cz, bytes);

			ChunkDataStore.put(ChunkPos.toLong(cx, cz), bytes);
		} catch (Throwable t) {
			ChunkDataDebugState.error(t);
		}
	}
}
