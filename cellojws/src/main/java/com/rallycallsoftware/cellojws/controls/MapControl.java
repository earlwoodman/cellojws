package com.rallycallsoftware.cellojws.controls;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.LatLong;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;


public class MapControl extends Control 
{

	private LatLong topLeft;
	
	private LatLong bottomRight;
	
	public MapControl(AbsDims dim, CommandToken<?> token, Image image_, final LatLong topLeft_, final LatLong bottomRight_) 
	{
		super(dim, token);
		setImage(image_);
		
		topLeft = topLeft_;
		bottomRight = bottomRight_;
	}

	public LatLong getTopLeft() 
	{
		return topLeft;
	}

	public void setTopLeft(LatLong topLeft) 
	{
		this.topLeft = topLeft;
	}

	public LatLong getBottomRight() 
	{
		return bottomRight;
	}

	public void setBottomRight(LatLong bottomRight) 
	{
		this.bottomRight = bottomRight;
	}

	
}
