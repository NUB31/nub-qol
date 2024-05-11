package com.nubqol;

import com.nublib.config.provider.FileStorageProvider;
import com.nublib.config.provider.IStorageProvider;
import com.nubqol.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class NubQolClient implements ClientModInitializer {
	public static final IStorageProvider CONFIG_PROVIDER = FileStorageProvider.create(NubQol.MOD_ID);
	public static final ModConfig CONFIG = new ModConfig(CONFIG_PROVIDER);

	@Override
	public void onInitializeClient() {
		NubQol.LOGGER.info(String.format("Initializing %s client", NubQol.MOD_ID));
	}
}