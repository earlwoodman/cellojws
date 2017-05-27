package com.rallycallsoftware.cellojwsdemo;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.core.ScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.windowing.WindowManager;
import com.rallycallsoftware.cellojwsdemo.views.FirstWindow;
import com.rallycallsoftware.cellojwsdemo.views.Wallpaper;

public class Application extends Environment {

	public Application(ScreenFactoryDispatcher screenFactoryDispatcher, String debugMode_) {
		super(screenFactoryDispatcher, debugMode_);
	}

	public static void main(String[] args)
	{
		final ScreenFactoryDispatcher dispatcher = new ScreenFactoryDispatcher() {
			
			public void completed() {
				
			}
			
			@Override
			public void refresh() {
				
			}
			
			@Override
			public Screen getScreen(Class<?> class1) {
				return null;
			}
		};
	
		final Application app = new Application(dispatcher, "FALSE");
		app.start();
	}
	
	@Override
	public void initialize() {
		
		final WindowManager winMgr = getWindowManager();
		
		final int height = winMgr.getScreenHeight();
		final int width = winMgr.getScreenWidth();
		
		final Wallpaper wallpaper = new Wallpaper(new AbsDims(0, 0, width, height));
		winMgr.addWindow(wallpaper);
		
        final FirstWindow firstWindow = new FirstWindow(new AbsDims(inch(), inch(), inch() * 5, inch() * 4));
        winMgr.addWindow(firstWindow);
		
	}

}
