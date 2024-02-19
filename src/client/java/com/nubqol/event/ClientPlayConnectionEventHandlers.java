package com.nubqol.event;

import com.nubqol.NubQolClient;
import com.nubqol.networking.ClientModMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientPlayConnectionEventHandlers implements ClientPlayConnectionEvents.Disconnect, ClientPlayConnectionEvents.Join {
	@Override
	public void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
		NubQolClient.hasEelServerSupport = false;
		NubQolClient.inGame = false;
	}

	@Override
	public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
		NubQolClient.inGame = true;
		NubQolClient.hasEelServerSupport = false;
		ClientModMessages.syncEasyElytraLaunchStatus();

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.schedule(() -> {
			String message = NubQolClient.hasEelServerSupport
					? "NUB Qol: Full easy elytra launch functionality enabled."
					: "NUB Qol: Limited easy elytra launch functionality due to missing mod on the server.";

			if (client.player != null) {
				client.player.sendMessage(Text.literal(message));
			}
		}, 3, TimeUnit.SECONDS);
	}
}
