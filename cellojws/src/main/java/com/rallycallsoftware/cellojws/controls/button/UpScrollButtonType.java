package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.controls.VerticalScrollBar;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class UpScrollButtonType extends FixedSizeButtonType {

	private UpScrollButtonType() {
	}

	private static final UpScrollButtonType Instance = new UpScrollButtonType();

	public static UpScrollButtonType getInstance() {
		return Instance;
	}

	@Override
	public int getWidth() {
		return VerticalScrollBar.getControlBoxHeight();
	}

	@Override
	public int getHeight() {
		return VerticalScrollBar.getControlBoxHeight();
	}

	@Override
	public Image getNormal() {
		return ImageFactory.getUpScrollButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return getNormal();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getUpScrollButtonPressed();
	}

}
