package com.rallycall.prospector.controllers;

import com.rallycall.prospector.ProspectorScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.controls.Controller;
import com.rallycallsoftware.cellojws.general.core.Environment;

public class ProspectorController extends Controller
{
	
	public static ProspectorScreenFactoryDispatcher getScreenFactoryDispatcher()
	{
		return (ProspectorScreenFactoryDispatcher)Environment.getEnvironment().getScreenFactoryDispatcher();
	}
	
}
