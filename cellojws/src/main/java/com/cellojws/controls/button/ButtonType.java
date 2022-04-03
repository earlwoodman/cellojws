package com.cellojws.controls.button;

import java.awt.Color;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;

public abstract class ButtonType
{
	public abstract int getHeight();	

	private static final int highlightAmt = 6;
	
	private Color normalHighlightColour = null;
	
	private Color mouseoverHighlightColour = null;
	
	private Color mouseDownHighlightColour = null;
	
	public static final float highlightPercentage = 0.35F;

	public Color produceHighlightColour(Color colour) 
	{
		return new Color(
				colour.getRed() + highlightAmt, 
				colour.getGreen() + highlightAmt, 
				colour.getBlue() + highlightAmt);
	}
	
	public void renderMousedown(Graphics graphics, AbsDims dims, Color colour)
	{
		graphics.drawSolidRect(dims, colour);

		if( mouseDownHighlightColour == null )
		{
			mouseDownHighlightColour = produceHighlightColour(colour); 	
		}

		graphics.drawSolidRect(dims.left, 
							   dims.top, 
							   dims.right, 
							   dims.top + Math.round(dims.getAbsHeight() * highlightPercentage), mouseDownHighlightColour);
	}

	public void renderNormal(Graphics graphics, AbsDims dims, Color colour)
	{
		graphics.drawSolidRect(dims, colour);
		
		if( normalHighlightColour == null )
		{
			normalHighlightColour = produceHighlightColour(colour);	
		}		
		
		graphics.drawSolidRect(dims.left, 
							   dims.top, 
							   dims.right, 
							   dims.top + Math.round(dims.getAbsHeight() * highlightPercentage), normalHighlightColour);
	}

	public void renderMouseover(Graphics graphics, AbsDims dims, Color colour)
	{
		graphics.drawSolidRect(dims, colour);
				
		if( mouseoverHighlightColour == null )
		{
			mouseoverHighlightColour = produceHighlightColour(colour);	
		}		

		graphics.drawSolidRect(dims.left, 
							   dims.top, 
							   dims.right, 
							   dims.top + Math.round(dims.getAbsHeight() * highlightPercentage), mouseoverHighlightColour);
	}
	 
}
