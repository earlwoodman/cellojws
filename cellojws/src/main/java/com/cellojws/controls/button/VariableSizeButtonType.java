package com.cellojws.controls.button;

import com.cellojws.general.FontInfo;

public abstract class VariableSizeButtonType extends ButtonType
{	
	public abstract int getWidth(String text, FontInfo fontInfo);
}
