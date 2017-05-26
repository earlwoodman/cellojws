package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class LargeButtonType extends FixedSizeButtonType {

	private LargeButtonType() {
	}

	private static final LargeButtonType Instance = new LargeButtonType();

	public static LargeButtonType getInstance() {
		return Instance;
	}

	@Override
	public int getWidth() {
		return Button.getLargeButtonWidth();
	}

	@Override
	public int getHeight() {
		return Button.getLargeButtonHeight();
	}

	@Override
	public Image getNormal() {
		return ImageFactory.getLargeButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return ImageFactory.getLargeButtonMouseover();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getLargeButtonPressed();
	}

}
