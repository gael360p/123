package com.gael.chunkdata.mixin;

import com.gael.chunkdata.ChunkDataStore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkDataS2CPacket.class)
public class ChunkDataS2CPacketMixin {
	@Inject(method = "apply", at = @At("HEAD"))
	private void chunkdata_client$capture(ClientPlayPacketListener listener, CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.world == null) return;

		ChunkDataS2CPacket self = (ChunkDataS2CPacket) (Object) this;

		ByteBuf raw = Unpooled.buffer();
		try {
			// RegistryByteBuf binds to a DynamicRegistryManager. :contentReference[oaicite:1]{index=1}
			RegistryByteBuf buf = new RegistryByteBuf(raw, mc.world.getRegistryManager());

			// Call the packet's private write(...) through the invoker. :contentReference[oaicite:2]{index=2}
			((ChunkDataS2CPacketInvoker) (Object) self).chunkdata_client$write(buf);

			long bytes = raw.readableBytes();
			ChunkDataStore.put(ChunkPos.toLong(self.getChunkX(), self.getChunkZ()), bytes);
		} finally {
			raw.release();
		}
	}
}
