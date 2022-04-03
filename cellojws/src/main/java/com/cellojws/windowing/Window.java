/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.windowing;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.controls.Direction;
import com.cellojws.controls.DropDownList;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.Image;
import com.cellojws.token.CommandToken;


public abstract class Window extends Control
{	
    private static final Color darkBlue = Environment.getWindowingSystemDark();

	private static final Color darkerBlue = Environment.getWindowingSystemLight();

	private boolean closable;

    private String title;

    private Control focus = null;
    
    private boolean popup = false;    
    
	private AbsDims shrunkDims;

	public Window(final AbsDims dim)
    {
        super(dim, null);
    }
	
	public Window(final AbsDims dim, final Image backgrdImage)
    {
        super(dim, null);
          
        setBackgroundImage(backgrdImage);
    }

	public void setBackgroundImage(final Image backgrdImage) 
	{
		if( backgrdImage != null )
        {
        	setImage(backgrdImage);
	        
	        shrunkDims = getScreenDims().makeCopy();
	        shrunkDims.shrink(0);
        }
	}

	public boolean isClosable() 
	{ 
		return closable; 
	}
    
	public void set_closable(final boolean close) 
	{ 
		closable = close; 
	}

    public void setTitle(String title) 
    { 
    	this.title = title; 
    }
    
    public String getTitle() 
    { 
    	return title; 
    }

    public void renderWindow(final Graphics graphics, final boolean mousedown)
    {    	    
    	final AbsDims titleDims = calculateTitleBarDims();

    	graphics.drawSolidRect(getDimensions(), getBackground());

    	if( isPopup() )
    	{
	    	final Image dropShadow = Environment.getDropShadowImage();
	    	
	    	final AbsDims dropShadowDims = getScreenDims().makeCopy();
	    	dropShadowDims.merge(titleDims);
	    	dropShadowDims.expand(Environment.tinyGap(), Environment.tinyGap());
	    	
	    	graphics.drawImage(dropShadow, dropShadowDims);
    	
	    	renderTitleBar(graphics, titleDims);
    	}
    	    
        synchronized(controlsLock)
        {
        	getWindowManager().renderControl(this, graphics, mousedown);
        }
                
    }
    
    private AbsDims calculateTitleBarDims()
    {
    	final AbsDims screenDims = getScreenDims();
    	
    	// Only draw the title if not full screen
		if( screenDims.getAbsHeight() == getScreenHeight() && screenDims.getAbsWidth() == getScreenWidth() )
		{
			return null;
		}
		
		final int threeEightsInch = Math.round(Environment.mediumGap() * 0.75F);
				
		final AbsDims titleDims = new AbsDims(
				screenDims.left,
				screenDims.top - threeEightsInch - Control.getStdGap(),
				screenDims.right,
				screenDims.top - Control.getStdGap()
				); 
		
		return titleDims;
    }
    
    /**
     * Renders a title bar if appropriate
     * 
     * @param graphics
     * @return
     */
    private void renderTitleBar(final Graphics graphics, final AbsDims titleDims)
	{
		graphics.drawGradientRect(darkBlue, darkerBlue, titleDims.getTopHalf(), Direction.Downward);
		graphics.drawGradientRect(darkerBlue, darkBlue, titleDims.getBottomHalf(), Direction.Downward);
	
		final FontInfo savedFont = graphics.getFontInfo();
		
		graphics.setFontInfo(Environment.getWindowTitleFont());

		final int width = WindowManager.getTextWidth(getTitle(), Environment.getWindowTitleFont());
		final int height = WindowManager.getTextHeight(Environment.getWindowTitleFont());

		graphics.drawGaussianFont(getTitle(), 
						  titleDims.left + (titleDims.getAbsWidth() - width) / 2, 
						  titleDims.top + (titleDims.getAbsHeight() - height) / 2);
		graphics.setFontInfo(savedFont);
	}
    
    @Override
    public boolean doSpecialClickActions(final int x, final int y)
    {
    	return true;
    }

    public boolean isPopup()
    {
        return popup;
    }

    public void setPopup(boolean popup)
    {
        this.popup = popup;
    }

    public void processMousePress(final int x, final int y)
    {
        
        synchronized(controlsLock)
        {
            final List<Control> reversed = getControlsReversed();
            
            for( Control control : reversed )
            {
                final AbsDims dims = control.getScreenDims();
                if( getWindowManager().isInside( dims, x, y ) )
                {                    
                    if( control instanceof Window )
                    {
                        control.processMousePress(x, y);    
                    }
                    else
                    {
                        control.processMousePress(x - dims.left, y - dims.top);
                    }
                    
                }
            }
        }
    }

    public int getTextWidth(final String text, final FontInfo fontInfo)
    {
        return WindowManager.getTextWidth(text, fontInfo);
    }

    public int getTextHeight(final FontInfo fontInfo)
    {
        return WindowManager.getTextHeight(fontInfo);
    }

	@Override
	public void processMouseWheel(final MouseWheelEvent wheelEvent) 
	{
				
	}

	/**
	 * Tells the window that a drop down list is dropping down. 
	 * 
	 * Roll up all others, and move this list to the front
	 * 
	 * @param list
	 */
	public void childDroppingDown(final DropDownList list) 
	{
		for( final Control control : getControlsRecursive() )
		{
			if( control != list && control instanceof DropDownList )
			{
				((DropDownList)control).rollUp();
			}
		}
		
		bringToFront(list.getParent());
	}

	public abstract CommandToken<?> enterKeyPressed();

	public abstract CommandToken<?> escapeKeyPressed();
	
    public abstract CommandToken<?> processKeyPress(final KeyEvent keyEvent);

	public Control getFocus()
	{
		return focus;
	}

	public void setFocus(Control focus)
	{
		this.focus = focus;
	}

}
