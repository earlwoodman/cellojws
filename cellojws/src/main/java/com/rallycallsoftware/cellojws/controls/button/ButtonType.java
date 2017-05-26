package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;

public abstract class ButtonType {
	public abstract Image getNormal();

	public abstract Image getMouseover();

	public abstract Image getMousedown();

	public abstract int getHeight();
}