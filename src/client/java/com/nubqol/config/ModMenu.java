package com.nubqol.config;

import com.nublib.config.Config;
import com.nublib.config.modmenu.ModMenuIntegration;
import com.nubqol.NubQolClient;

public class ModMenu implements ModMenuIntegration {
	@Override
	public Config getConfig() {
		return NubQolClient.CONFIG;
	}
}
