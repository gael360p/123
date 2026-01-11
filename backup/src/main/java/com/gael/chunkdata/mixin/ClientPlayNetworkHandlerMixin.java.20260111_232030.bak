package com.gael.chunkdata.mixin;

import com.gael.chunkdata.ChunkDataStore;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.network.ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Inject(method = "onChunkData", at = @At("HEAD"))
	private void chunkdata_client$capture(ChunkDataS2CPacket packet, CallbackInfo ci) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.world == null) return;

		ByteBuf raw = Unpooled.buffer();
		try {
			RegistryByteBuf buf = new RegistryByteBuf(raw, mc.world.getRegistryManager());

			// Measure full packet payload by calling the packet's private write(...)
			((ChunkDataS2CPacketInvoker) (Object) packet).chunkdata_client$write(buf);

			long bytes = raw.readableBytes();
			ChunkDataStore.put(ChunkPos.toLong(packet.getChunkX(), packet.getChunkZ()), bytes);
		} finally {
			raw.release();
		}
	}
}
