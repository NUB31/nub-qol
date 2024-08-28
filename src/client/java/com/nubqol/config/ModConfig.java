package com.nubqol.config;

import com.nublib.config.Config;
import com.nublib.config.entry.BooleanConfigEntry;
import com.nublib.config.entry.IConfigEntry;
import com.nublib.config.provider.IStorageProvider;
import com.nublib.gui.ConfigScreen;
import com.nubqol.NubQolClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ModConfig extends Config {
    public final IConfigEntry<Boolean> EELEnabled = new BooleanConfigEntry(sp, "easyElytraLaunchEnabled", true);
    public final IConfigEntry<Boolean> EELMessagesEnabled = new BooleanConfigEntry(sp, "easyElytraLaunchMessagesEnabled", false);
    public final IConfigEntry<Boolean> hitMobsThroughTransparentBlocksEnabled = new BooleanConfigEntry(sp, "hitMobsThroughTransparentBlocksEnabled", true);
    public final IConfigEntry<Boolean> ignoreStemsWithAxe = new BooleanConfigEntry(sp, "ignoreStemsWithAxe", true);

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
                                        .setTitle(Text.translatable("nub-qol.config.eel_enabled.title"))
                                        .setDescription(Text.translatable("nub-qol.config.eel_enabled.description"))
                                        .onChange(EELEnabled::set)
                                        .addChildEntries(childEntryList -> childEntryList
                                                .addToggle(EELMessagesEnabled.get(), toggleBuilder -> toggleBuilder
                                                        .setTitle(Text.translatable("nub-qol.config.eel_messages_enabled.title"))
                                                        .setDescription(Text.translatable("nub-qol.config.eel_messages_enabled.description"))
                                                        .onChange(EELMessagesEnabled::set))))
                                .addToggle(hitMobsThroughTransparentBlocksEnabled.get(), toggleBuilder -> toggleBuilder
                                        .setTitle(Text.translatable("nub-qol.config.attb_enabled.title"))
                                        .setDescription(Text.translatable("nub-qol.config.attb_enabled.description"))
                                        .onChange(hitMobsThroughTransparentBlocksEnabled::set))
                                .addToggle(ignoreStemsWithAxe.get(), toggleBuilder -> toggleBuilder
                                        .setTitle(Text.translatable("nub-qol.config.pswa_enabled.title"))
                                        .setDescription(Text.translatable("nub-qol.config.pswa_enabled.description"))
                                        .onChange(ignoreStemsWithAxe::set))
                        ))
                .onSave(NubQolClient.CONFIG::save)
                .build();
    }
}
