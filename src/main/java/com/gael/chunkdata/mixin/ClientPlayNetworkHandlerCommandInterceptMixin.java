package com.gael.chunkdata.mixin;

import com.gael.chunkdata.ChunkDataLocalCommandRouter;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerCommandInterceptMixin {

	@Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
	private void chunkdata_client$sendChatCommand(String command, CallbackInfo ci) {
		// command has NO leading slash
		if (ChunkDataLocalCommandRouter.isOurCommand(command)) {
			ChunkDataLocalCommandRouter.run(normalize(command));
			ci.cancel();
		}
	}

	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
	private void chunkdata_client$sendChatMessage(String content, CallbackInfoReturnable<Boolean> cir) {
		// Some clients route "/cmd" through chat message paths
		if (content != null && content.startsWith("/")) {
			String cmd = content.substring(1);
			if (ChunkDataLocalCommandRouter.isOurCommand(cmd)) {
				ChunkDataLocalCommandRouter.run(normalize(cmd));
				cir.setReturnValue(true);
			}
		}
	}

	private static String normalize(String raw) {
		String low = raw.toLowerCase();
		if (low.startsWith("chuckdata ")) return "chunkdata " + raw.substring("chuckdata ".length());
		if (low.equals("chuckdata")) return "chunkdata";
		if (low.startsWith("chunckdata ")) return "chunkdata " + raw.substring("chunckdata ".length());
		if (low.equals("chunckdata")) return "chunkdata";
		return raw;
	}
}
