/*
 * Created on 2011-03-20
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.controls;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.DownScrollButtonType;
import com.rallycallsoftware.cellojws.controls.button.UpScrollButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class VerticalScrollBar extends Control {

	private Button up;
	private Button down;

	private int topIndex;
	private int bottomIndex;

	private int currentTop;

	private final static int CONTROLBOX_HEIGHT = 20;

	public VerticalScrollBar(final AbsDims dim, final int topIndex_, final int bottomIndex_) {
		super(dim, null);

		topIndex = topIndex_;
		bottomIndex = bottomIndex_;
		currentTop = topIndex;

		up = new BasicButton(UpScrollButtonType.getInstance(), "",
				new CommandToken<VerticalScrollBar>(ControlController::upVSB, this), new Point(0, 0));
		addControl(up);

		down = new BasicButton(DownScrollButtonType.getInstance(), "",
				new CommandToken<VerticalScrollBar>(ControlController::downVSB, this),
				new Point(0, dim.getAbsHeight() - CONTROLBOX_HEIGHT));
		addControl(down);
	}

	public void setBounds(final int topIndex_, final int bottomIndex_) {
		topIndex = topIndex_;
		bottomIndex = bottomIndex_;
		currentTop = topIndex;
	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return true;
	}

	public void down() {
		if (currentTop < bottomIndex) {
			currentTop++;
		}
	}

	public void up() {
		if (currentTop > topIndex) {
			currentTop--;
		}
	}

	@Override
	public void render(final Graphics graphics, final boolean mousedown) {

	}

	public int getTopIndex() {
		return currentTop;
	}

	public void setTopIndex(final int currentTop) {
		this.currentTop = currentTop;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

	public void scrollUp() {
		ControlController.upVSB(new CommandToken<VerticalScrollBar>(ControlController::upVSB, this));
	}

	public void scrollDown() {
		ControlController.downVSB(new CommandToken<VerticalScrollBar>(ControlController::downVSB, this));
	}

	public static int getControlBoxHeight() {
		return CONTROLBOX_HEIGHT;
	}
}
