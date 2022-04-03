package com.cellojws.controls.button;

import java.awt.Color;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.core.Environment;
import com.cellojws.windowing.WindowManager;

public class TabButtonType extends VariableSizeButtonType
{

	private TabButtonType(){}
	
	private static final TabButtonType Instance = new TabButtonType();
	
	public static TabButtonType getInstance()
	{
		return Instance;
	}

	@Override
	public int getWidth(String text, FontInfo fontInfo)
	{
		// Return the fixed amount plus the variable amount
		return Environment.getEnvironment().smallGap()  
			+ WindowManager.getTextWidth(text, fontInfo);
	}

	@Override
	public int getHeight()
	{
		return SmallButtonType.getInstance().getHeight();
	}

	@Override
	public void renderMousedown(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		
	}

	@Override
	public void renderNormal(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		
	}

	@Override
	public void renderMouseover(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		
	}
}
