package com.rallycallsoftware.cellojws.special;

import java.awt.Color;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;

public class TextTooltip extends Tooltip 
{
	private static final long TOOLTIP_DISPLAY_WINDOW = 4000;

	private String text;

	private Label label;
	
	public TextTooltip()
	{
		super();
		initialize();
	}

	public TextTooltip(final String message)
	{
		initialize();
		setText(message);
	}

	private void initialize()
	{
		label = new Label(null);
		label.setDrawIfBlank(false);
		label.setBackground(Color.black);
		label.setSolidBackground(true);
		label.setFontInfo(Environment.getToolTipFontInfo());
		addControl(label);
	}
	
	@Override
	public long getTooltipDisplayLength() 
	{
		return TOOLTIP_DISPLAY_WINDOW;
	}

	@Override
	public void render(final Graphics graphics, final boolean mousedown) 
	{
		if( text != null && text.length() > 0 )
		{
			setHeight(graphics.getTextHeight(Environment.getToolTipFontInfo()));
			setWidth(graphics.getTextWidth(text, Environment.getToolTipFontInfo()));
    		final AbsDims tooltipDims = getWindowManager().calculateTooltipDims(this);
    		
    		setDimensions(tooltipDims);
    		label.setDimensions(new AbsDims(0, 0, tooltipDims.getAbsWidth(), tooltipDims.getAbsHeight()));
		}
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
		label.setText(text);
	}

	@Override
	public void reset()
	{
		setText("");
	}

}
