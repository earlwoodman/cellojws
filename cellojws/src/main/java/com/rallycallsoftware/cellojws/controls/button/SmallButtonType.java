package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class SmallButtonType extends FixedSizeButtonType {

	private SmallButtonType() {
	}

	private static final SmallButtonType Instance = new SmallButtonType();

	public static SmallButtonType getInstance() {
		return Instance;
	}

	@Override
	public int getWidth() {
		return Button.getStandardButtonWidth();
	}

	@Override
	public int getHeight() {
		return Button.getStandardButtonHeight();
	}

	@Override
	public Image getNormal() {
		return ImageFactory.getSmallButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return ImageFactory.getSmallButtonMouseover();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getSmallButtonPressed();
	}

}
