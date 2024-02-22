package com.nubqol;

import com.nublib.config.provider.ConfigProvider;
import com.nublib.config.provider.FileConfigProvider;
import com.nubqol.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class NubQolClient implements ClientModInitializer {
	public static final ConfigProvider CONFIG_PROVIDER = FileConfigProvider.create(NubQol.MOD_ID);
	public static final ModConfig CONFIG = new ModConfig(CONFIG_PROVIDER, NubQol.MOD_ID);

	@Override
	public void onInitializeClient() {
		NubQol.LOGGER.info(String.format("Initializing %s client", NubQol.MOD_ID));
	}
}