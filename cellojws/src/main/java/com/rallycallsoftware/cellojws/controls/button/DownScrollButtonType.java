package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.controls.VerticalScrollBar;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class DownScrollButtonType extends FixedSizeButtonType {

	private DownScrollButtonType() {
	}

	private static final DownScrollButtonType Instance = new DownScrollButtonType();

	public static DownScrollButtonType getInstance() {
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
		return ImageFactory.getDownScrollButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return getNormal();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getDownScrollButtonPressed();
	}

}
