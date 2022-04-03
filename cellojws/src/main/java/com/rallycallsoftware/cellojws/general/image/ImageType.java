package com.rallycallsoftware.cellojws.general.image;

public enum ImageType 
{
	PlayButton("PlayButton");
	
	private String filename;
	
	private ImageType(String filename)
	{
		this.filename = filename;
	}

	public String getFilename() 
	{
		return filename;
	}
	
}
