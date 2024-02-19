package com.nubqol;

import com.nubqol.networking.ServerModMessages;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class NubQol implements ModInitializer {
	public static final String MOD_ID = "nub-qol";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final HashMap<UUID, Boolean> USER_EEL_ENABLED = new HashMap<>();


	@Override
	public void onInitialize() {
		LOGGER.info(String.format("Initializing %s", MOD_ID));

		ServerModMessages.registerC2SPackets();
	}
}