package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class DownButtonType extends FixedSizeButtonType {

	private DownButtonType() {
	}

	private static final DownButtonType Instance = new DownButtonType();

	public static DownButtonType getInstance() {
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
		return ImageFactory.getDownButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return getNormal();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getDownButtonPressed();
	}

}
