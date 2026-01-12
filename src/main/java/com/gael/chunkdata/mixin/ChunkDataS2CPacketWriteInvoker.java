package com.gael.chunkdata.mixin;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkDataS2CPacket.class)
public interface ChunkDataS2CPacketWriteInvoker {
  @Invoker("write")
  void chunkdata_client$write(RegistryByteBuf buf);
}
