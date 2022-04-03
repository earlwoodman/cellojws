package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.FontInfo;

public abstract class VariableSizeButtonType extends ButtonType
{	
	public abstract int getWidth(String text, FontInfo fontInfo);
}
