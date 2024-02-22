package com.nubqol.config;

import com.nublib.config.modmenu.ModMenuIntegration;
import com.nublib.config.provider.ConfigProvider;
import com.nubqol.NubQolClient;

public class ModMenu implements ModMenuIntegration {
	@Override
	public ConfigProvider configProvider() {
		return NubQolClient.CONFIG_PROVIDER;
	}
}
