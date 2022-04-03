package com.rallycallsoftware.cellojws.special;

import com.rallycallsoftware.cellojws.controls.Control;


public abstract class Tooltip extends Control implements SpecialWindow
{

	public abstract long getTooltipDisplayLength();

	private int width;
	
	private int height;

	public Tooltip(final int width, final int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public Tooltip() 
	{
	
	}

	public abstract void reset();

	public int getWidth() 
	{
		return width;
	}

	public void setWidth(int width) 
	{
		this.width = width;
	}

	public int getHeight() 
	{
		return height;
	}

	public void setHeight(int height) 
	{
		this.height = height;
	}
}
