package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class TabButtonType extends VariableSizeButtonType {

	private TabButtonType() {
	}

	private static final TabButtonType Instance = new TabButtonType();

	public static TabButtonType getInstance() {
		return Instance;
	}

	@Override
	public int getWidth(String text, FontInfo fontInfo) {
		// Return the fixed amount plus the variable amount
		return Environment.getEnvironment().quarterInch() + WindowManager.getTextWidth(text, fontInfo);
	}

	@Override
	public Image getNormal() {
		return SmallButtonType.getInstance().getNormal();
	}

	@Override
	public Image getMouseover() {
		return SmallButtonType.getInstance().getMouseover();
	}

	@Override
	public Image getMousedown() {
		return SmallButtonType.getInstance().getMousedown();
	}

	@Override
	public int getHeight() {
		return SmallButtonType.getInstance().getHeight();
	}
}
