package com.rallycall.prospector;

public class CommandLineArgs 
{
	private boolean auto;
	
	private boolean debug;
	
	private boolean humanWins;

	public boolean isAuto() 
	{
		return auto;
	}

	public void setAuto(boolean auto) 
	{
		this.auto = auto;
	}

	public boolean isDebug() 
	{
		return debug;
	}

	public void setDebug(boolean debug) 
	{
		this.debug = debug;
	}

	public boolean isHumanWins() 
	{
		return humanWins;
	}

	public void setHumanWins(boolean humanWins) 
	{
		this.humanWins = humanWins;
	}
	

}
