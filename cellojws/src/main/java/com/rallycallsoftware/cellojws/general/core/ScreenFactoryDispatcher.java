package com.rallycallsoftware.cellojws.general.core;

import java.util.HashMap;
import java.util.Map;

import com.rallycallsoftware.cellojws.controls.CompletionCallback;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.stock.DemoNotice;
import com.rallycallsoftware.cellojws.stock.KenBurnsScreen;
import com.rallycallsoftware.cellojws.stock.LogoScreen;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;


public abstract class ScreenFactoryDispatcher implements CompletionCallback
{	
            
	private Window panZoomScreen;

    public ScreenFactoryDispatcher()
    {
        
    }

	public void dispatch(final CommandToken<?> token)
	{
        if( token != null )
        {
            if( token.execute() )
            {
            	return;
            }
            else
            {
            	WorkerLog.error("Token " + token.toString() + " failed on execution.");
            }
        }
	}
	
	public void startOver()
	{
		final Environment environment = Environment.getEnvironment();
		environment.getWindowManager().removeAllWindows();
		environment.getWindowManager().nullAllWindows();
		environment.setTutorialMode(false);
	}		

	public void showDemoNotice() 
	{
		final Environment environment = Environment.getEnvironment();
		final DemoNotice screen = (DemoNotice)getScreen(DemoNotice.class, null);
		environment.getWindowManager().showPopup(screen);
	}
	
	public void showCompanyLogo()
	{
		final Environment environment = Environment.getEnvironment();
		environment.getWindowManager().addWindow(
				getScreen(LogoScreen.class,  
						new CompletionCallback()
						{

							@Override
							public void completed() 
							{
								showOpeningVideo();
							}
					
						}
				));
	}

	public void showOpeningVideo()
	{
		final Environment environment = Environment.getEnvironment();
		environment.getWindowManager().removeWindow(environment.getWindowManager().getScreen(LogoScreen.class));
		environment.getWindowManager().addWindow(panZoomScreen);
		((KenBurnsScreen)panZoomScreen).start();
	}

	public void preloadPanZoomScreen()
	{
		final Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("overlay", "/Graphics/Overlay.png");
		payload.put("file", "/Graphics/intro.anim");
		payload.put("completionCallback", new CompletionCallback() 
			{
				@Override
				public void completed() 
				{
					startOver();
				}
			}
		);

		// Load this screen. We'll need it after the logo screen is shown.
		panZoomScreen = getScreen(KenBurnsScreen.class, payload);		
	}
		
	public <T> Screen getScreen(Class<?> class1, T payload)
	{
		Screen screen = null;
		if( class1 == KenBurnsScreen.class )
		{
			@SuppressWarnings("unchecked")
			final Map<String, Object> mapPayload = (Map<String, Object>)payload;
			screen = new KenBurnsScreen((CompletionCallback)mapPayload.get("completionCallback"),
					(String)mapPayload.get("file"),
					(String)mapPayload.get("overlay"));
		}
		if( class1 == LogoScreen.class )
		{
			screen = new LogoScreen((CompletionCallback)payload);
		}

		return screen;
	}
	
	public abstract Screen getScreen(Class<?> class1);

	private ScreenFactoryDispatcher screenFactoryDispatcher;
	
	public ScreenFactoryDispatcher getScreenFactoryDispatcher()
	{
		return screenFactoryDispatcher;
	}
	
}
