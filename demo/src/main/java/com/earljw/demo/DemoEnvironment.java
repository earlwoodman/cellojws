/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.earljw.demo;

import java.awt.Color;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.cellojws.controls.button.Button;
import com.cellojws.general.core.Environment;
import com.cellojws.general.core.ScreenFactoryDispatcher;

public class DemoEnvironment extends Environment
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000L;

	private DemoEnvironment(final ScreenFactoryDispatcher dispatcher, final ProspectorSettings settings)
	{
		super(dispatcher, false, settings);
	}

	private static final Color hbLightBlue = new Color(53, 104, 73);
	
	private static final Color hbLightBlueClicking = hbLightBlue.darker().darker();
	
	private static final Color hbLightBlueMouseover = hbLightBlue.brighter().brighter(); 

	@Override 
	public Color getNormalColour()
	{
		return hbLightBlue;
	}
	
	@Override 
	public Color getClickingColour()
	{
		return hbLightBlueClicking;
	}

	@Override 
	public Color getMouseoverColour()
	{
		return hbLightBlueMouseover;
	}
	
	public static void main(String[] args)
	{
		Environment environment;

		final ScreenFactoryDispatcher screenFactoryDispatcher = new ProspectorScreenFactoryDispatcher();

		final List<String> argList = Arrays.asList(args);
		final boolean debug = argList.contains("DEBUG");
		final boolean auto = argList.contains("AUTO");
		final boolean humanWins = argList.contains("HUMANWINS");

		ProspectorSettings settings;
		try 
		{
			settings = loadSettings(debug, auto, humanWins);
		}
		catch (IOException e) 
		{
			throw new RuntimeException("Could not load settings.");
		}		
		
		environment = new DemoEnvironment(screenFactoryDispatcher, settings);

		Button.setClickColour(hbLightBlueClicking);
		Button.setMouseoverColour(hbLightBlueMouseover);
		Button.setNormalColour(hbLightBlue);

		environment.start();
		
	}
	
	public static ProspectorSettings loadSettings(final boolean debug, final boolean auto, final boolean humanWins) throws IOException 
	{
		final ProspectorSettings settings = new ProspectorSettings();
		settings.setArgs(new CommandLineArgs());		
		settings.getArgs().setDebug(debug);
		settings.getArgs().setAuto(auto);
		settings.getArgs().setHumanWins(humanWins);
		
		settings.load();
		return settings;
	}

}
