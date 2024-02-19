package com.nubqol.networking;

import com.nubqol.NubQol;
import com.nubqol.networking.packet.SynClientEelEnabledC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ServerModMessages {
	public static final Identifier SYN_EEL_SERVER_SUPPORT_PACKET_ID = new Identifier(NubQol.MOD_ID, "syn-eel-server-support");
	public static final Identifier ACK_EEL_SERVER_SUPPORT_PACKET_ID = new Identifier(NubQol.MOD_ID, "ack-eel-server-support");

	public static void registerC2SPackets() {
		ServerPlayNetworking.registerGlobalReceiver(ServerModMessages.SYN_EEL_SERVER_SUPPORT_PACKET_ID, SynClientEelEnabledC2SPacket::receive);
	}
}