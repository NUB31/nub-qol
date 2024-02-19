package com.nubqol;

import com.nublib.config.provider.ConfigKeyValue;
import com.nublib.config.provider.FileConfigProvider;
import com.nublib.config.provider.IConfigProvider;
import com.nubqol.config.Config;
import com.nubqol.event.ClientPlayConnectionEventHandlers;
import com.nubqol.networking.ClientModMessages;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import java.util.Objects;

public class NubQolClient implements ClientModInitializer {
	public static boolean hasEelServerSupport = false;
	public static boolean inGame = false;

	private static void handleConfigChanged(ConfigKeyValue kvp) {
		if (Objects.equals(kvp.key, CONFIG.easyElytraLaunchEnabled.getKey())) {
			if (!inGame) return;
			ClientModMessages.syncEasyElytraLaunchStatus();
		}
	}

	@Override
	public void onInitializeClient() {
		NubQol.LOGGER.info(String.format("Initializing %s client", NubQol.MOD_ID));

		ClientModMessages.registerS2CPackets();

		ClientPlayConnectionEvents.DISCONNECT.register(new ClientPlayConnectionEventHandlers());
		ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionEventHandlers());
	}

	public static final IConfigProvider CONFIG_PROVIDER = FileConfigProvider.create(NubQol.MOD_ID, NubQolClient::handleConfigChanged);
	public static final Config CONFIG = new Config(CONFIG_PROVIDER);
}