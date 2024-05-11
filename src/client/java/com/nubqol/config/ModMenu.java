package com.nubqol.config;

import com.nublib.gui.ConfigScreenBuilder;
import com.nubqol.NubQolClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.Text;

public class ModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return screen -> new ConfigScreenBuilder(screen)
				.fromConfig(Text.literal("Nub's QoL Options"), NubQolClient.CONFIG)
				.onSave(NubQolClient.CONFIG::save)
				.build();
	}
}
