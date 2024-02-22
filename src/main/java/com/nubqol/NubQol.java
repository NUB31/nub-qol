package com.nubqol;

import com.nublib.config.sync.ConfigSync;
import com.nubqol.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NubQol implements ModInitializer {
	public static final String MOD_ID = "nub-qol";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigSync<ModConfig> CONFIG_SYNC = new ConfigSync<>(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info(String.format("Initializing %s", MOD_ID));
	}
}