package com.rallycallsoftware.cellojws.controls.listbox;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class NumberListBoxItem implements ListBoxItem {

	private int value;

	public NumberListBoxItem(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int getHeight() {
		return WindowManager.getTextHeight(Environment.getEnvironment().getListBoxFontInfo());
	}

	@Override
	public String toString() {
		return Integer.valueOf(getValue()).toString();
	}

	@Override
	public void draw(Graphics graphics, int leftDraw, int top, int widthMax) {
		graphics.drawText(toString(), leftDraw, top);
	}

	@Override
	public int getWidth() {
		return ListBox.PADDING
				+ WindowManager.getTextWidth(toString(), Environment.getEnvironment().getListBoxFontInfo());
	}

}
