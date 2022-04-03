package com.rallycallsoftware.cellojws.general;

import java.awt.Color;

import com.rallycallsoftware.cellojws.general.core.Environment;

public class FontInfo 
{
	private Color textColor;
	
	private Color dropShadow;
	
	private Color textColorForHighlight;
	
	private Color dropShadowForHighlight;
	
	private Color gaussianTextColour;
	
	private int fontSize;
	
	private String fontFace;
	
	private boolean italic;	

	private boolean upperCase;
	
	public FontInfo()
	{
		
	}

	public int getFontSize()
	{
		return Math.round(fontSize * Environment.getScaling());
	}
	
	public int getUnscaledFontSize()
	{
		return fontSize;
	}

	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
	}

	public String getFontFace() 
	{
		return fontFace;
	}

	public void setFontFace(String fontFace) 
	{
		this.fontFace = fontFace;
	}

	public boolean isItalic() 
	{
		return italic;
	}

	public void setItalic(boolean italic) 
	{
		this.italic = italic;
	}

	public Color getTextColor() 
	{
		return textColor;
	}

	public void setTextColor(Color textColor) 
	{
		this.textColor = textColor;
	}

	public Color getDropShadow() 
	{
		return dropShadow;
	}

	public void setDropShadow(Color dropShadow) 
	{
		this.dropShadow = dropShadow;
	}
	
	public Color getTextColorForHighlight() 
	{
		return textColorForHighlight;
	}

	public void setTextColorForHighlight(Color textColorForHighlight) 
	{
		this.textColorForHighlight = textColorForHighlight;
	}

	public Color getDropShadowForHighlight() 
	{
		return dropShadowForHighlight;
	}

	public void setDropShadowForHighlight(Color dropShadowForHighlight) 
	{
		this.dropShadowForHighlight = dropShadowForHighlight;
	}

	public boolean isUpperCase() 
	{
		return upperCase;
	}

	public void setUpperCase(boolean upperCase) 
	{
		this.upperCase = upperCase;
	}

	public Color getGaussianTextColour() 
	{
		return gaussianTextColour;
	}

	public void setGaussianTextColour(Color gaussianTextColour) 
	{
		this.gaussianTextColour = gaussianTextColour;
	}

	public FontInfo makeCopy()
	{
		final FontInfo newFontInfo = new FontInfo();
		
		newFontInfo.textColor = textColor;
		newFontInfo.dropShadow = dropShadow;
		newFontInfo.textColorForHighlight = textColorForHighlight;
		newFontInfo.dropShadowForHighlight = dropShadowForHighlight;
		newFontInfo.fontSize = fontSize;
		newFontInfo.fontFace = fontFace;
		newFontInfo.italic = italic;
		newFontInfo.upperCase = upperCase;
		newFontInfo.gaussianTextColour = gaussianTextColour;
		
		return newFontInfo;
	}
	
}
