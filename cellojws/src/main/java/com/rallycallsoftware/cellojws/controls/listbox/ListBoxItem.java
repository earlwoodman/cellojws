package com.rallycallsoftware.cellojws.controls.listbox;

import com.rallycallsoftware.cellojws.adapter.Graphics;

public interface ListBoxItem {
	int getHeight();

	int getWidth();

	void draw(final Graphics graphics, final int leftDraw, final int top, final int widthMax);

}
