package com.rallycall.prospector;

import java.util.Map;

import com.rallycall.prospector.screens.StartScreen;
import com.rallycallsoftware.cellojws.controls.CompletionCallback;
import com.rallycallsoftware.cellojws.general.Event;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.core.ScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.stock.EventInfoScreen;
import com.rallycallsoftware.cellojws.stock.LogoScreen;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.stock.messagescreen.MessageScreen;
import com.rallycallsoftware.cellojws.stock.messagescreen.MessageScreenBean;

public class ProspectorScreenFactoryDispatcher extends ScreenFactoryDispatcher
{

	@Override
	public <T> Screen getScreen(final Class<?> clazz, final T payload)
	{

		Screen ret = null;
		
		if (clazz == MessageScreen.class)
		{
			if (payload instanceof MessageScreenBean)
			{
				final MessageScreenBean bean = (MessageScreenBean) payload;
				ret = new MessageScreen(Environment.getSmallPopupDims(), bean);
			}
		}
		else if (clazz == EventInfoScreen.class)
		{
			@SuppressWarnings("unchecked")
			final Screen screen = new EventInfoScreen(Environment.getSmallPopupDims(),
					(Event) ((Map<String, Object>) payload)
							.get("event"),
					(Image) ((Map<String, Object>) payload)
							.get("logo"));
			ret = screen;
		}
		else if (clazz == StartScreen.class)
		{
			ret = new StartScreen(
					Environment
							.getSmallPopupDims());
		}
		else if (clazz == LogoScreen.class)
		{
			ret = new LogoScreen(
					(CompletionCallback) payload);
		}
		else
		{
			throw new RuntimeException(
					"Unable to find screen class: "
							+ clazz.toString());
		}

		return ret;

	}

	@Override
	public Screen getScreen(Class<?> class1)
	{
		return getScreen(class1, null);
	}

	public ProspectorScreenFactoryDispatcher()
	{
		super();
	}

	@Override
	public void completed()
	{

	}

	@Override
	public void startOver()
	{
		final Environment environment = Environment.getEnvironment();
		super.startOver();
		environment.getWindowManager()
				.showPopup(environment.getScreenFactoryDispatcher().getScreen(StartScreen.class, null));

	}
}
