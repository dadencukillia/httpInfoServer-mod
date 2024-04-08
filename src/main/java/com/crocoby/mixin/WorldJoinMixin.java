package com.crocoby.mixin;

import com.crocoby.HttpInfoServer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class WorldJoinMixin {
	@Inject(at = @At("HEAD"), method = "joinWorld")
	private void init(CallbackInfo info) {
		if (!HttpInfoServer.connectedWebSocket) {
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("["+HttpInfoServer.MODID+"] Client is not connected to HTTP info API").copy().withColor(0xFF0000).formatted(Formatting.BOLD));
		}
	}
}