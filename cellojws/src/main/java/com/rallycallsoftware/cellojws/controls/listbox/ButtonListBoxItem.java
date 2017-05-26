package com.rallycallsoftware.cellojws.controls.listbox;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.FixedSizeButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class ButtonListBoxItem extends BasicButton implements ListBoxItem {

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return false;
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {
		// Purposely not render, because we are going to draw with the draw
		// method instead
	}

	public ButtonListBoxItem(String text, CommandToken<?> token, FixedSizeButtonType size) {
		super(null, text, token, size);
	}

	@Override
	public int getHeight() {
		return getButtonType().getHeight();
	}

	@Override
	public void draw(Graphics graphics, int leftDraw, int top, int widthMax) {
		final FontInfo savedFontInfo = graphics.getFontInfo();

		final AbsDims parentDims = getParent().getScreenDims();

		graphics.setFontInfo(Environment.getEnvironment().getButtonFontInfo());
		final AbsDims realDims = new AbsDims(leftDraw - parentDims.left, top - getParent().getScreenDims().top,
				leftDraw + getWidth() - parentDims.left, top - getParent().getScreenDims().top + getHeight());

		setDimensions(realDims);
		super.render(graphics, getWindowManager().isMouseDown());

		graphics.setFontInfo(savedFontInfo);
	}

	@Override
	public int getWidth() {
		if (getButtonType() instanceof FixedSizeButtonType) {
			return ((FixedSizeButtonType) getButtonType()).getWidth();
		} else {
			throw new IncorrectButtonTypeException();
		}
	}

	@Override
	public String toString() {
		return getText();
	}
}
