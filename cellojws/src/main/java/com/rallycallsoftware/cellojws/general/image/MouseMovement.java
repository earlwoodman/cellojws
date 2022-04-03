package com.rallycallsoftware.cellojws.general.image;

public enum MouseMovement 
{
	Normal("Normal"),
	Mouseover("Mouseover"),
	Mousedown("Mousedown");
	
	private String filename;
	
	private MouseMovement(String filename)
	{
		this.filename = filename;
	}

	public String getFilename() 
	{
		return filename;
	}	

}
