package com.nubqol.networking.packet;

import com.nubqol.NubQol;
import com.nubqol.networking.ServerModMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SynClientEelEnabledC2SPacket {
	public static void receive(MinecraftServer client, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
		Boolean value = buf.readBoolean();
		NubQol.LOGGER.info(String.format("Sync easy elytra launch status for player \"%s\": \"%s\"", player.getUuid().toString(), value));
		NubQol.USER_EEL_ENABLED.put(player.getUuid(), value);
		responseSender.sendPacket(responseSender.createPacket(ServerModMessages.ACK_EEL_SERVER_SUPPORT_PACKET_ID, PacketByteBufs.create()));
	}
}