package com.nubqol.config;

import com.nublib.config.Config;
import com.nublib.config.entry.ClientToggleConfigEntry;
import com.nublib.config.entry.IClientConfigEntry;
import com.nublib.config.provider.IStorageProvider;
import com.nublib.gui.ConfigScreen;
import com.nubqol.NubQolClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ModConfig extends Config {
    public final IClientConfigEntry<Boolean> EELEnabled = new ClientToggleConfigEntry(sp, "easyElytraLaunchEnabled", true, Text.translatable("nub-qol.config.eel_enabled.title"), Text.translatable("nub-qol.config.eel_enabled.description"));
    public final IClientConfigEntry<Boolean> EELMessagesEnabled = new ClientToggleConfigEntry(sp, "easyElytraLaunchMessagesEnabled.", false, Text.translatable("nub-qol.config.eel_messages_enabled.title"), Text.translatable("nub-qol.config.eel_messages_enabled.description"));
    public final IClientConfigEntry<Boolean> hitMobsThroughTransparentBlocksEnabled = new ClientToggleConfigEntry(sp, "hitMobsThroughTransparentBlocksEnabled", true, Text.translatable("nub-qol.config.attb_enabled.title"), Text.translatable("nub-qol.config.attb_enabled.description"));
    public final IClientConfigEntry<Boolean> ignoreStemsWithAxe = new ClientToggleConfigEntry(sp, "ignoreStemsWithAxe", true, Text.translatable("nub-qol.config.pswa_enabled.title"), Text.translatable("nub-qol.config.pswa_enabled.description"));

    public ModConfig(IStorageProvider storageProvider) {
        super(storageProvider);
    }

    public Screen createConfigScreen(@Nullable Screen parent) {
        return ConfigScreen
                .builder()
                .setParent(parent)
                .addPage(Text.translatable("nub-qol.config.ui.config_screen.title"), page -> page
                        .addEntries(entryList -> entryList
                                .addToggle(EELEnabled.get(), builder -> builder
                                        .setTitle(EELEnabled.guiConfigEntry().title())
                                        .setDescription(EELEnabled.guiConfigEntry().description())
                                        .onChange(EELEnabled::set)
                                        .addChildEntries(childEntryList -> {
                                            childEntryList.fromConfigEntry(EELMessagesEnabled);
                                        }))
                                .fromConfigEntry(hitMobsThroughTransparentBlocksEnabled)
                                .fromConfigEntry(ignoreStemsWithAxe)
                        ))
                .onSave(NubQolClient.CONFIG::save)
                .build();
    }
}
