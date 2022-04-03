package com.rallycallsoftware.cellojws.special;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;

public abstract class WindowTooltip extends Tooltip 
{
	private static final long WINDOW_TOOLTIP_DISPLAY_LENGTH = 8000;  
	
	public WindowTooltip(final int width, final int height) 
	{		
		super(width, height);
	}
	
	@Override
	public void render(Graphics graphics, boolean mousedown) 
	{
		super.render(graphics, mousedown);
	
		final AbsDims dims = getWindowManager().calculateTooltipDims(this);
		setDimensions(dims);
		
		final AbsDims screenDims = getScreenDims();
		if( screenDims.right > getScreenWidth() )
		{
			screenDims.left -= screenDims.right - getScreenWidth(); 
			screenDims.right -= screenDims.right - getScreenWidth();
		}
		if( screenDims.bottom > getScreenHeight() )
		{
			screenDims.top -= screenDims.bottom - getScreenHeight();
			screenDims.bottom -= screenDims.bottom - getScreenHeight();
		}

		graphics.drawImage(Environment.getSmallPopupImage(), screenDims);
	}
	
	@Override
	public long getTooltipDisplayLength() 
	{
		return WINDOW_TOOLTIP_DISPLAY_LENGTH;
	}

}
