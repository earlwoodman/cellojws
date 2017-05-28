package com.rallycallsoftware.cellojws.controls;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.core.ScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojws.windowing.WindowBean;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public abstract class Controller {
	public static <T extends WindowBean, U extends Window<T>> Window<T> popup(final Class<U> clazz, final T bean, final AbsDims dims) {
		final Window<T> window = getScreenFactoryDispatcher().getScreen(clazz, bean, dims);
		getEnvironment().getWindowManager().showPopup(window);
		return window;
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
