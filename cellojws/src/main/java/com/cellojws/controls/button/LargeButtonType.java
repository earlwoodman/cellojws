package com.cellojws.controls.button;

public class LargeButtonType extends FixedSizeButtonType
{

	private LargeButtonType(){}
	
	private static final LargeButtonType Instance = new LargeButtonType();
	
	public static LargeButtonType getInstance()
	{
		return Instance;
	}
	
	@Override
	public int getWidth()
	{
		return Button.getLargeButtonWidth();
	}

	@Override
	public int getHeight()
	{
		return Button.getLargeButtonHeight();
	}

}
