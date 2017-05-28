package com.rallycallsoftware.cellojwsdemo.controllers;

import com.rallycallsoftware.cellojws.controls.Controller;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojwsdemo.views.SecondWindow;

public class FirstWindowController extends Controller {

	public static void clickMe(final CommandToken<?> command)
	{
		popup(SecondWindow.class, null, Environment.getEnvironment().getSmallPopupDims());
	}
}
