/*
 * Created on 2010-06-24
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.logging;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.cellojws.general.core.Environment;

public enum WorkerLog
{

	Logger;
	
    private boolean enabled = false;
        
    private Path file;
    
    private WorkerLog()
    {
        try
        {
        	String path = Environment.getExecutionPath();
        	if( path == null || path.equals("") )
        	{
        		path = System.getProperty("user.dir");
        	}
        	file = Paths.get(path + "/Logs/log.txt");
        	List<String> lines = new ArrayList<String>();
			lines.add("Beginning log file.");
        	Files.write(file, lines, Charset.forName("UTF-8"));
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void write(final String string2Log)
    {
    	if( enabled )
    	{
    		try
	        {    		
    			List<String> lines = new ArrayList<String>();
    			lines.add(string2Log);
    		    Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
	        }
	        catch( Exception e )
	        {
	            System.err.println("Could not write to log with string " + string2Log);
	        }
    	}
    }        
   
    public static void debug(final String string2Log)
    {
        System.out.println("DEBUG: " + string2Log);
        Logger.write(string2Log);
    }
    
    public static void info(final String string2Log)
    {
        System.out.println("INFO: " + string2Log);
        Logger.write(string2Log);
    }
 
    public static void error(final String string2Log)
    {
        System.out.println("ERROR: " + string2Log);
        Logger.write(string2Log);
    }
        
	public void disable() 
	{
		enabled = false;
	}
	
	public void enable()
	{
		enabled = true;
	}
}
