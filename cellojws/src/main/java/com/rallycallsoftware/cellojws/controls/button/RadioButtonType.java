package com.rallycallsoftware.cellojws.controls.button;

import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;

public class RadioButtonType extends FixedSizeButtonType {
	private boolean checked;

	public RadioButtonType(final boolean checked) {
		this.checked = checked;
	}

	public RadioButtonType() {
		this.checked = false;
	}

	@Override
	public Image getNormal() {
		return checked ? ImageFactory.getCheckboxChecked() : ImageFactory.getCheckboxUnchecked();
	}

	@Override
	public Image getMouseover() {
		return ImageFactory.getCheckboxChecked();
	}

	@Override
	public Image getMousedown() {
		return ImageFactory.getCheckboxChecked();
	}

	@Override
	public int getHeight() {
		return ImageFactory.getCheckboxChecked().getHeight();
	}

	@Override
	public int getWidth() {
		return ImageFactory.getCheckboxChecked().getWidth();
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isChecked() {
		return this.checked;
	}
}
