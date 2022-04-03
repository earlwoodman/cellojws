package com.cellojws.controls.listbox;

import com.cellojws.adapter.Graphics;

public interface ListBoxItem
{
	int getHeight();
	
	int getWidth();

	void draw(final Graphics graphics, final int leftDraw, final int top, final int widthMax);

	default int compare(ListBoxItem listBoxItem) { return 0; }

}
