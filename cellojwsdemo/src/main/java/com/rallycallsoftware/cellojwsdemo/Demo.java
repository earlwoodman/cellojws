package com.rallycallsoftware.cellojwsdemo;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.core.ScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.windowing.WindowManager;
import com.rallycallsoftware.cellojwsdemo.models.FirstWindowBean;
import com.rallycallsoftware.cellojwsdemo.models.WallpaperBean;
import com.rallycallsoftware.cellojwsdemo.views.FirstWindow;
import com.rallycallsoftware.cellojwsdemo.views.Wallpaper;

public class Demo extends Environment {

	public Demo(ScreenFactoryDispatcher screenFactoryDispatcher, String debugMode_) {
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
		};
	
		final Demo app = new Demo(dispatcher, "FALSE");
		app.start();
	}
	
	@Override
	public void initialize() {
		
		final WindowManager winMgr = getWindowManager();
		
		final int height = winMgr.getScreenHeight();
		final int width = winMgr.getScreenWidth();
		
		final WallpaperBean wallpaperBean = new WallpaperBean();
		final Wallpaper wallpaper = new Wallpaper(new AbsDims(0, 0, width, height), wallpaperBean);
		winMgr.addWindow(wallpaper);
		
		final FirstWindowBean firstWindowBean = new FirstWindowBean();
        final FirstWindow firstWindow = new FirstWindow(new AbsDims(inch(), inch(), inch() * 5, inch() * 4), firstWindowBean);
        winMgr.addWindow(firstWindow);
		
	}

}
