package com.rallycallsoftware.cellojws.general;

import java.awt.DisplayMode;
import java.beans.Statement;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.rallycallsoftware.cellojws.logging.WorkerLog;

public abstract class Settings 
{

	private boolean windowed;

	private String displayMode;

	private List<DisplayMode> displayModes;	
			
	private void populateField(final String item)  
	{
		final String[] chunks = item.split("=");
		final String key = chunks[0].trim();
		final String value = chunks[1].trim();
		final Object[] values = new Object[1];
		if( value.equals("true") )
		{
			values[0] = true;
		}
		else if( value.equals("false"))
		{
			values[0] = false;
		}
		else
		{
			values[0] = value;
		}
		
		try
		{
			final Statement statement = new Statement(this, "set" + key, values);			
			statement.execute();
		}
		catch(Exception e)
		{
			WorkerLog.error(e.getMessage());
		}
	}

	public boolean getWindowed() 
	{
		return windowed;
	}

	public void setWindowed(boolean windowed) 
	{
		this.windowed = windowed;
	}
	
	public String getDisplayMode() 
	{
		return displayMode;
	}

	public void setDisplayMode(String displayMode) 
	{
		this.displayMode = displayMode;
	}

	
	public void save() throws IOException 
	{
		final String dir = System.getProperty("user.dir");
		final List<String> values = findSaveableFieldsAndValues();
		
		Path path = Paths.get(dir + "/settings.ini");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) 
		{
			for( final String value : values )
			{
				writer.write(value);
				writer.write("\r\n");
			}
		}
	}

	public List<String> findSaveableFieldsAndValues() 
	{
		final List<String> fields = new ArrayList<>();
		
		fields.add("Windowed=" + getWindowed());
		fields.add("DisplayMode=" + getDisplayMode());
		
		return fields;
		
	}

	public void load() throws IOException 
	{
		final String dir = System.getProperty("user.dir");
		try (Stream<String> stream = Files.lines(Paths.get(dir + "/settings.ini"))) 
		{
			stream.forEach(x -> populateField(x));
		}
		catch (IOException e) 
		{
			WorkerLog.error(e.getMessage());
			throw e;
		}
	}

	public void setDisplayModes(List<DisplayMode> okModes) 
	{
		this.displayModes = okModes;
	}

	public List<String> displayModeLabels()
	{
		final List<String> displayModeLabels = new ArrayList<>();
		
		displayModeLabels.add("Monitor Default Resolution");
		displayModes.stream()
				    .forEach(x -> displayModeLabels.add(x.getWidth() + " x " + x.getHeight() + " at " + x.getRefreshRate() + "-Hz"));
		
		return displayModeLabels;
	}
		
}
