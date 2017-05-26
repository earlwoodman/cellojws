package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class SmallDeleteButtonType extends FixedSizeButtonType {

	private SmallDeleteButtonType() {
	}

	private static final SmallDeleteButtonType Instance = new SmallDeleteButtonType();

	public static SmallDeleteButtonType getInstance() {
		return Instance;
	}

	@Override
	public int getWidth() {
		return 15;
	}

	@Override
	public int getHeight() {
		return 15;
	}

	@Override
	public Image getNormal() {
		return ImageFactory.getDeleteButtonNormalSmall();
	}

	@Override
	public Image getMouseover() {
		return getNormal();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getDeleteButtonPressedSmall();
	}

}
