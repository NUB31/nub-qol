package com.nubqol.config;

import com.nublib.config.Config;
import com.nublib.config.entry.ClientToggleConfigEntry;
import com.nublib.config.provider.IStorageProvider;
import net.minecraft.text.Text;

public class ModConfig extends Config {
	public final ClientToggleConfigEntry easyElytraLaunchEnabled = new ClientToggleConfigEntry(storageProvider, "easyElytraLaunchEnabled", true, Text.literal("Easy elytra launch"), Text.literal("Allows you to take off with an elytra without first leaving the ground and entering flight mode"));

	public final ClientToggleConfigEntry hitMobsThroughTransparentBlocksEnabled = new ClientToggleConfigEntry(storageProvider, "hitMobsThroughTransparentBlocksEnabled", true, Text.literal("Hit mobs through transparent blocks"), Text.literal("Allows you to hit mobs through non-solid blocks such as sugar cane, tall/non-tall grass, vines etc."));

	public ModConfig(IStorageProvider storageProvider) {
		super(storageProvider);
	}
}
