package com.nubqol.config;

import com.nublib.config.Config;
import com.nublib.config.entry.ClientToggleConfigEntry;
import com.nublib.config.entry.IClientConfigEntry;
import com.nublib.config.provider.IStorageProvider;
import net.minecraft.text.Text;

public class ModConfig extends Config {
    public final IClientConfigEntry<Boolean> easyElytraLaunchEnabled = new ClientToggleConfigEntry(storageProvider, "easyElytraLaunchEnabled", true, Text.translatable("nub-qol.config.eel_enabled.title"), Text.translatable("nub-qol.config.eel_enabled.description"));
    public final IClientConfigEntry<Boolean> hitMobsThroughTransparentBlocksEnabled = new ClientToggleConfigEntry(storageProvider, "hitMobsThroughTransparentBlocksEnabled", true, Text.translatable("nub-qol.config.attb_enabled.title"), Text.translatable("nub-qol.config.attb_enabled.description"));
    public final IClientConfigEntry<Boolean> ignoreStemsWithAxe = new ClientToggleConfigEntry(storageProvider, "ignoreStemsWithAxe", true, Text.translatable("nub-qol.config.pswa_enabled.title"), Text.translatable("nub-qol.config.pswa_enabled.description"));

    public ModConfig(IStorageProvider storageProvider) {
        super(storageProvider);
    }
}
