package com.nubqol.config;

import com.nublib.config.Config;
import com.nublib.config.annotation.ConfigOptionMetadata;
import com.nublib.config.option.BooleanConfigOption;
import com.nublib.config.provider.IStorageProvider;

public class ModConfig extends Config {
	@ConfigOptionMetadata(title = "Easy elytra launch enabled", description = "Lets the player right click with a rocket when standing on the ground or falling, to start elytra flight")
	public final BooleanConfigOption easyElytraLaunchEnabled = new BooleanConfigOption("easyElytraLaunchEnabled", true, storageProvider);
	
	@ConfigOptionMetadata(title = "Hit mobs through blocks enabled", description = "Lets the player hit mobs through non-solid blocks such as grass, tall grass, sugar cane etc.")
	public final BooleanConfigOption hitMobsThroughTransparentBlocksEnabled = new BooleanConfigOption("hitMobsThroughTransparentBlocksEnabled", true, storageProvider);

	public ModConfig(IStorageProvider storageProvider) {
		super(storageProvider);
	}
}
