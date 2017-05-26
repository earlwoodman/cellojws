package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class DeleteButtonType extends FixedSizeButtonType {

	private DeleteButtonType() {
	}

	private static final DeleteButtonType Instance = new DeleteButtonType();

	public static DeleteButtonType getInstance() {
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
		return ImageFactory.getDeleteButtonNormal();
	}

	@Override
	public Image getMouseover() {
		return getNormal();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getDeleteButtonPressed();
	}

}
