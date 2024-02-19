package com.nubqol.event;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class ServerPlayConnectionEventHandlers implements ServerPlayConnectionEvents.Disconnect, ServerPlayConnectionEvents.Join {
	@Override
	public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {

	}

	@Override
	public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {

	}
}
