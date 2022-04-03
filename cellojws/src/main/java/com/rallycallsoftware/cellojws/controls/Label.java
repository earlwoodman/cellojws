/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.controls;

import java.awt.event.MouseWheelEvent;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;


public class Label extends Control
{
    
    private boolean wrap;
    
    private String text;
    
    private boolean solidBackground = false;
                    
    private boolean drawIfBlank = true; 
    		
    public Label(final AbsDims dim)
    {
    	super(dim, null);
    	setJustification(Justification.Left);
    }
    
    public Label(final AbsDims dim, final String text_, final CommandToken<?> token)
    {
    	super(dim, token);
    	setText(text_);
    	setJustification(Justification.Left);
    }
    
	public Label(final AbsDims dim, String text_)
    {
        super(dim, null);
    	setText(text_);
        setJustification(Justification.Left);
    }
    
    public Label(final AbsDims dim, String text_, final Justification justification_)
    {
        super(dim, null);
        setText(text_);
        setJustification(justification_);
    }
    
    @Override
    public boolean doSpecialClickActions(int x, int y)
    {        
    	return true;
    }

    @Override
    public void render(final Graphics graphics, final boolean mousedown)
    {
    	if( !drawIfBlank && (getText() == null || getText().equals("")) )
    	{
    		return;
    	}
    	
    	super.render(graphics, mousedown);
        final AbsDims dims = getScreenDims();
        		
        if( solidBackground )
        {
            graphics.drawSolidRect(dims, getBackground());
        }
       
        if( !wrap )
        {
        	if( dims != null )
        	{
	        	final int centeredTop = dims.top + (dims.getAbsHeight() - WindowManager.getTextHeight()) / 2; 
	        			
			    if( getJustification() == Justification.Left )
			    {
			        graphics.drawText(text, dims.left, centeredTop, dims.getAbsWidth());
			    }
			    else
			    {
			        final int width = dims.getAbsWidth();
			        final int textWidth = graphics.getTextWidth(text, getFontInfo());
			
			        if( getJustification() == Justification.Center )
			        {
			            graphics.drawText(text, dims.left + (width - textWidth > 0 ? width - textWidth : 0) / 2, centeredTop, dims.getAbsWidth());
			        }
			        if( getJustification() == Justification.Right )
			        {
			            graphics.drawText(text, dims.right - textWidth, centeredTop, dims.getAbsWidth());
			        }
			    }
        	}
        }
        else
        {
        	
        	if( text == null || text.length() == 0 )
        	{
        		return;
        	}
        	
        	// For now, let's ignore justification and left justify.
        	// We will also ignore scroll bars and just print as much text as will fit.
        	        
        	final int gapBetweenLines = 5;
        	final int availableRows = dims.getAbsHeight() / (graphics.getTextHeight(getFontInfo()) + gapBetweenLines);
        	
        	final int width = dims.getAbsWidth();
        	final String[] words = text.split(" ");
        	int i = 0;
        	int rows = 0;

        	do
        	{
            	int remainingWidthThisLine = width;
            	int x = 0;
            	int y = (graphics.getTextHeight(getFontInfo()) + gapBetweenLines) * (rows);
            	int thisWidth = 0;
            	
        		while( remainingWidthThisLine > graphics.getTextWidth(words[i], getFontInfo()) )
	        	{
	        		graphics.drawText(words[i], dims.left + x, dims.top + y);
	        		thisWidth = graphics.getTextWidth(words[i] + " ", getFontInfo()); 
	        		x += thisWidth;
	        		remainingWidthThisLine -= thisWidth;
	        		i++;
	        		if( i == words.length )
	        		{
	        			break;
	        		}
	        		// Handle carriage return
	        		if( words[i - 1].contains("\r") )
	        		{
	        			break;
	        		}
	        	}
        		if( i == words.length )
        		{
        			break;
        		}
        		rows++;

        	}
        	while( rows < availableRows );
        			
        }
                
        //graphics.drawRect(getScreenDims(), Color.white);
    }
	
	public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setSolidBackground(boolean solidBackground)
    {
        this.solidBackground = solidBackground;
    }

    public boolean isSolidBackground()
    {
        return solidBackground;
    }

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) 
	{	
		
	}

	public boolean isWrap() 
	{
		return wrap;
	}

	public void setWrap(boolean wrap) 
	{
		this.wrap = wrap;
	}

	@Override
	public FontInfo getStandardFont()
	{
		return Environment.getLabelFontInfo();
	}

	public boolean isDrawIfBlank() 
	{
		return drawIfBlank;
	}

	public void setDrawIfBlank(boolean drawIfBlank) 
	{
		this.drawIfBlank = drawIfBlank;
	}
	
}
