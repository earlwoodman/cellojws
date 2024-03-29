package com.cellojws.controls;

import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.image.Image;
import com.cellojws.token.CommandToken;


public abstract class CalendarObject extends Control 
{

	private int day;
	
	private Control image;
	
	public CalendarObject(final AbsDims dim, final CommandToken<?> token, final Image image_, final int day_) 
	{		
		super(dim, token);
		image = new Control(dim.makeCopy());
		image.setImage(image_);
		day = day_;
		addControl(image);
	}

	public int getDay() 
	{
		return day;
	}

	public void setDay(int day) 
	{
		this.day = day;
	}

	public void setImageDimensions(AbsDims imageDims) 
	{
		image.setDimensions(imageDims);
	}

	public Control getImageControl() 
	{
		return image;
	}

}
