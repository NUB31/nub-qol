package com.nubqol.networking;

import com.nubqol.NubQolClient;
import com.nubqol.networking.packet.AckClientEelEnabledS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public class ClientModMessages {
	public static void syncEasyElytraLaunchStatus() {
		PacketByteBuf buffer = PacketByteBufs.create();
		buffer.writeBoolean(NubQolClient.CONFIG.easyElytraLaunchEnabled.get());
		ClientPlayNetworking.send(ServerModMessages.SYN_EEL_SERVER_SUPPORT_PACKET_ID, buffer);
	}

	public static void registerS2CPackets() {
		ClientPlayNetworking.registerGlobalReceiver(ServerModMessages.ACK_EEL_SERVER_SUPPORT_PACKET_ID, AckClientEelEnabledS2CPacket::receive);
	}
}