package com.nubqol.config;

import com.nublib.config.ClientConfig;
import com.nublib.config.Option;
import com.nublib.config.provider.ConfigProvider;
import com.nublib.config.serialization.BooleanSerializer;

public class ModConfig extends ClientConfig {
	public final Option<Boolean> easyElytraLaunchEnabled = new Option<>(provider, "easyElytraLaunchEnabled", true, new BooleanSerializer());
	public final Option<Boolean> hitMobsThroughTransparentBlocksEnabled = new Option<>(provider, "hitMobsThroughTransparentBlocksEnabled", true, new BooleanSerializer());

	public ModConfig(ConfigProvider provider, String id) {
		super(provider, id);
	}
} 