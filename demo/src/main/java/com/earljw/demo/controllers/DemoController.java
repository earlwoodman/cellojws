package com.earljw.demo.controllers;

import com.cellojws.controls.Controller;
import com.cellojws.general.core.Environment;
import com.earljw.demo.ProspectorScreenFactoryDispatcher;

public class DemoController extends Controller
{
	
	public static ProspectorScreenFactoryDispatcher getScreenFactoryDispatcher()
	{
		return (ProspectorScreenFactoryDispatcher)Environment.getEnvironment().getScreenFactoryDispatcher();
	}
	
}
