package com.nubqol;

import com.nublib.config.provider.FileStorageProvider;
import com.nublib.config.provider.IStorageProvider;
import com.nublib.util.BindingUtil;
import com.nubqol.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.glfw.GLFW;

public class NubQolClient implements ClientModInitializer {
    public static final IStorageProvider CONFIG_PROVIDER = FileStorageProvider.create(NubQol.MOD_ID);
    public static final ModConfig CONFIG = new ModConfig(CONFIG_PROVIDER);

    @Override
    public void onInitializeClient() {
        NubQol.LOGGER.info(String.format("Initializing %s client", NubQol.MOD_ID));
        BindingUtil.bindScreenToKey(GLFW.GLFW_KEY_N, () -> CONFIG.createConfigScreen(null), "nub-qol.config.ui.config_screen.title");
    }
}