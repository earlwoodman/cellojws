package com.rallycallsoftware.cellojws.controls.listbox;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class ImageListBoxItem implements ListBoxItem {

	private Image image;

	public ImageListBoxItem(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public int getWidth() {
		return ListBox.PADDING + getImage().getWidth();
	}

	@Override
	public String toString() {
		return image.toString();
	}

	@Override
	public void draw(Graphics graphics, int leftDraw, int top, int widthMax) {
		// TODO: Sucks making a new dimensions object every time here.
		graphics.drawImage(getImage().getBufferedImage(),
				new AbsDims(leftDraw, top, leftDraw + getImage().getWidth(), top + getHeight()));

	}

}
