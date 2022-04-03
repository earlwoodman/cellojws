package com.earljw.demo;

import java.util.ArrayList;
import java.util.List;

import com.cellojws.general.Settings;

public class ProspectorSettings extends Settings
{
	private CommandLineArgs args;

	private String gameSpeed; 
	
	public String getGameSpeed() 
	{
		return gameSpeed;
	}

	public void setGameSpeed(String gameSpeed) 
	{
		this.gameSpeed = gameSpeed;
	}

	public CommandLineArgs getArgs() 
	{
		return args;
	}

	public void setArgs(CommandLineArgs args) 
	{
		this.args = args;
	}

	@Override
	public List<String> findSaveableFieldsAndValues() 
	{
		final List<String> fields = new ArrayList<>();
		
		fields.addAll(super.findSaveableFieldsAndValues());
		fields.add("GameSpeed=" + getGameSpeed());		
		
		return fields;
		
	}

	public List<String> gameSpeedLabels() 
	{
		final List<String> ret = new ArrayList<>();
		
		ret.add("Slow");
		ret.add("Medium");
		ret.add("Fast");
		ret.add("Connor McDavid with a tailwind");
		
		return ret;
	}

}
