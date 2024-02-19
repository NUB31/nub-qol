package com.nubqol.config;

import com.nublib.config.Option;
import com.nublib.config.provider.IConfigProvider;
import com.nublib.config.serialization.BooleanSerializer;
import com.nubqol.networking.ClientModMessages;

public class Config {
	public final Option<Boolean> easyElytraLaunchEnabled;
	public final Option<Boolean> hitMobsThroughTransparentBlocksEnabled;

	public Config(IConfigProvider configProvider) {
		this.easyElytraLaunchEnabled = new Option<>(configProvider, "easyElytraLaunchEnabled", true, new BooleanSerializer(), (v) -> ClientModMessages.syncEasyElytraLaunchStatus());
		this.hitMobsThroughTransparentBlocksEnabled = new Option<>(configProvider, "hitMobsThroughTransparentBlocksEnabled", true, new BooleanSerializer());
	}
} 