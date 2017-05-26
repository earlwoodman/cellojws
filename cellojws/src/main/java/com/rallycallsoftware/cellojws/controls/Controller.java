package com.rallycallsoftware.cellojws.controls;

import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.core.ScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public abstract class Controller {
	public static <T> Screen popup(Class<?> clazz, T payload) {
		final Screen screen = getScreenFactoryDispatcher().getScreen(clazz, payload);
		getEnvironment().getWindowManager().showPopup(screen);
		return screen;
	}

	public static WindowManager getWindowManager() {
		return Environment.getEnvironment().getWindowManager();
	}

	public static Environment getEnvironment() {
		return Environment.getEnvironment();
	}

	public static ScreenFactoryDispatcher getScreenFactoryDispatcher() {
		return Environment.getEnvironment().getScreenFactoryDispatcher();
	}

}
