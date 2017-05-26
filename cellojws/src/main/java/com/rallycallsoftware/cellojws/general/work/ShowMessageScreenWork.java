package com.rallycallsoftware.cellojws.general.work;

import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.stock.messagescreen.MessageScreen;
import com.rallycallsoftware.cellojws.stock.messagescreen.MessageScreenBean;

public class ShowMessageScreenWork implements Work {

	private MessageScreenBean bean;

	@Override
	public boolean doWork() {

		final Environment environment = Environment.getEnvironment();
		environment.getWindowManager()
				.showPopup(environment.getScreenFactoryDispatcher().getScreen(MessageScreen.class, bean));

		return true;
	}

	public void setMessageScreenBean(MessageScreenBean bean) {
		this.bean = bean;

	}

}
