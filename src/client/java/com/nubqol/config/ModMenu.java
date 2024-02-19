package com.nubqol.config;

import com.nublib.config.modmenu.ModMenuIntegration;
import com.nublib.config.provider.IConfigProvider;
import com.nubqol.NubQolClient;

public class ModMenu implements ModMenuIntegration {
	@Override
	public IConfigProvider configProvider() {
		return NubQolClient.CONFIG_PROVIDER;
	}
}
