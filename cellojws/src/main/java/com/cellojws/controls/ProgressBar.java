package com.cellojws.controls;

import java.awt.Color;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.Justification;


public class ProgressBar extends Label 
{

	private float percentage;
	
	private Color progressColour;
	
	private Color backgroundColour;
	
	private boolean orientationVertical;
	
	public ProgressBar(final AbsDims dim, final String text_, final Color progressColour_, final Color backgroundColour_) 
	{
		super(dim, text_);
		progressColour = progressColour_;
		backgroundColour = backgroundColour_;		
	}
	
	public ProgressBar(final AbsDims dim, final Color progressColour_) 
	{
		super(dim, "");
		progressColour = progressColour_;
		backgroundColour = progressColour_.darker().darker().darker();		
	}

    public ProgressBar(final AbsDims dim, String text_, final Justification justification_)
    {
    	super(dim, text_, justification_);
    }

    public ProgressBar(final AbsDims dim)
    {
    	super(dim, "", Justification.Left);
    }

	public boolean isOrientationVertical()
	{
		return orientationVertical;
	}

	public void setOrientationVertical(boolean orientationVertical)
	{
		this.orientationVertical = orientationVertical;
	}

	public float getPercentage() 
	{
		return percentage;
	}

	public void setPercentage(final float percentage) 
	{
		this.percentage = percentage;
	}

	@Override
	public void render(final Graphics graphics, final boolean mousedown) 
	{	
		
		final AbsDims dims = getScreenDims();
		
		AbsDims pctDims;
		if( !orientationVertical )
		{
			pctDims = new AbsDims(
					dims.left,
					dims.top,
					dims.left + (int)(percentage * dims.getAbsWidth()),
					dims.bottom
					);
		}
		else
		{
			pctDims = new AbsDims(
					dims.left,
					dims.top + (int)((1F - percentage) * dims.getAbsHeight()),
					dims.right,
					dims.bottom 
					);
		}
		
		graphics.drawSolidRect(dims, backgroundColour);
		graphics.drawSolidRect(pctDims, progressColour);
		
		super.render(graphics, mousedown);
	}
    
	
    
}
