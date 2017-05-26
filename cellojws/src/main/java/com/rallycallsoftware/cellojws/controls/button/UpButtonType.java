package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class UpButtonType extends FixedSizeButtonType {

	private UpButtonType() {
	}

	private static final UpButtonType Instance = new UpButtonType();

	public static UpButtonType getInstance() {
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
		return ImageFactory.getUpButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return getNormal();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getUpButtonPressed();
	}

}
