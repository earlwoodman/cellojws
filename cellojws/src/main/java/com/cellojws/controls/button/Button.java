/*
 * Created on 2010-07-11
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.controls.button;

import java.awt.Color;
import java.awt.Point;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.Justification;
import com.cellojws.general.core.Environment;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.WindowManager;

public abstract class Button extends Control
{

	private String text;

	private boolean mousedownOverride;
	
	private ButtonType buttonType;
	
	private int textHeight;

	private int textWidth;

	private int	leftDrawText;
	
	private int	topDrawText;

	private boolean metrics;

	// Ugly default colours
	private static Color mouseoverColour = new Color(155, 40, 20);

	private static Color normalColour = new Color(80, 50, 190);

	private static Color clickColour = new Color(9, 19, 100);

	public static int getStandardButtonHeight()
	{
		return Math.round(25 * Environment.getScaling());
	}

	public static int getStandardButtonWidth()
	{
		return Math.round(128 * Environment.getScaling());
	}

	public static int getLargeButtonHeight()
	{
		return Math.round(45 * Environment.getScaling());
	}

	public static int getLargeButtonWidth()
	{
		return Math.round(225 * Environment.getScaling());
	}

	public Button(final FixedSizeButtonType type, final String text_, final CommandToken<?> token_, final Point pos)
	{
		super(createDims(type, pos), token_);
		initialize(type, text_);
	}

	public Button(final AbsDims dim, String text, CommandToken<?> token, final ButtonType type)
	{
		super(dim, token);
		initialize(type, text);
	}

	private void initialize(final ButtonType type, final String text_)
	{
		text = text_;
		this.buttonType = type; 
		setJustification(Justification.Center);		
		
		invalidateMetrics();
	}

	private void invalidateMetrics()
	{
		metrics = false;
	}
	
	@Override
	public boolean doSpecialClickActions(int x, int y)
	{
		return isEnabled();
	}

	@Override
	public boolean doSpecialMoveActions(int x, int y)
	{
		return super.doSpecialMoveActions(x, y);
	}

	@Override
	public void render(Graphics graphics, final boolean mousedown)
	{
		super.render(graphics, mousedown);
		
		final AbsDims dims = getScreenDims();
		
		if (isEnabled() && (mousedown || mousedownOverride) && WindowManager.getLastControl() == this)
		{
			if (this.buttonType != null)
			{
				this.buttonType.renderMousedown(graphics, dims, clickColour);
			}
		}
		else
		{
			if (this.buttonType != null)
			{
				this.buttonType.renderNormal(graphics, dims, normalColour);
			}
		}

		if (!metrics)
		{
			textHeight = graphics.getTextHeight(getFontInfo());
			textWidth = graphics.getTextWidth(text, getFontInfo());
			if( getJustification() == Justification.Left )
			{
				leftDrawText = dims.left;
			}
			else
			{
				leftDrawText = (dims.getAbsWidth() - textWidth) / 2 + dims.left;
			}
			topDrawText = (dims.getAbsHeight() - textHeight) / 2 + dims.top;
		}

		if (isMouseOver() && !mousedown && isEnabled())
		{
			if (this.buttonType != null)
			{
				this.buttonType.renderMouseover(graphics, dims, mouseoverColour);
			}
		}

		graphics.drawText(text, leftDrawText, topDrawText);

	}

	public String getText()
	{
		return text;
	}

	public void setText(final String text_)
	{
		text = text_;
		invalidateMetrics();
	}

	public ButtonType getButtonType()
	{
		return buttonType;
	}

    public static AbsDims createDims(final FixedSizeButtonType type, final Point pos)
	{
    	return new AbsDims(pos.x, pos.y, type.getWidth() + pos.x, type.getHeight() + pos.y);
	}

	public boolean isMousedownOverride()
	{
		return mousedownOverride;
	}

	public void setMousedownOverride(boolean mousedownOverride)
	{
		this.mousedownOverride = mousedownOverride;
	}

	@Override
	public FontInfo getStandardFont()
	{
		return Environment.getButtonFontInfo();
	}

	public static Color getMouseoverColour() 
	{
		return mouseoverColour;
	}

	public static void setMouseoverColour(Color mouseoverColour) 
	{
		Button.mouseoverColour = mouseoverColour;
	}

	public static Color getNormalColour() 
	{
		return normalColour;
	}

	public static void setNormalColour(Color normalColour) 
	{
		Button.normalColour = normalColour;
	}

	public static Color getClickColour() 
	{
		return clickColour;
	}

	public static void setClickColour(Color clickColour) 
	{
		Button.clickColour = clickColour;
	}
	
}
