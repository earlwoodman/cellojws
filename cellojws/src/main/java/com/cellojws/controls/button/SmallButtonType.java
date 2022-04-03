package com.cellojws.controls.button;

public class SmallButtonType extends FixedSizeButtonType
{
	
	private SmallButtonType(){}
	
	private static final SmallButtonType Instance = new SmallButtonType();
	
	public static SmallButtonType getInstance()
	{
		return Instance;
	}
	
	@Override
	public int getWidth()
	{
		return Button.getStandardButtonWidth();
	}

	@Override
	public int getHeight()
	{
		return Button.getStandardButtonHeight();
	}

}
