package com.rallycallsoftware.cellojws.controls;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.DownScrollButtonType;
import com.rallycallsoftware.cellojws.controls.button.UpScrollButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class PlusMinus extends Control {

	private Button down;

	private Button up;

	private Label caption;

	private int value;

	private int topIndex;

	private int bottomIndex;

	private int step;

	private boolean formatAsDollars = false;

	public PlusMinus(final AbsDims dim, final int topIndex_, final int bottomIndex_, final int value_,
			final CommandToken<?> token, final int step_, final boolean formatAsDollars_) {
		super(dim, token);

		value = value_;
		bottomIndex = bottomIndex_;
		topIndex = topIndex_;
		step = step_;

		formatAsDollars = formatAsDollars_;

		up = new BasicButton(UpScrollButtonType.getInstance(), "",
				new CommandToken<PlusMinus>(ControlController::up, this),
				new Point(dim.getAbsWidth() - VerticalScrollBar.getControlBoxHeight(), 0));
		addControl(up);

		down = new BasicButton(DownScrollButtonType.getInstance(), "",
				new CommandToken<PlusMinus>(ControlController::down, this), new Point(0, 0));
		addControl(down);

		final AbsDims captionDims = new AbsDims(VerticalScrollBar.getControlBoxHeight(), 0,
				dim.getAbsWidth() - VerticalScrollBar.getControlBoxHeight() * 2,
				VerticalScrollBar.getControlBoxHeight());
		caption = new Label(captionDims, "");
		caption.setJustification(Justification.Center);
		redrawCaption();
		addControl(caption);

	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {

	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return true;
	}

	public void decrement() {
		if (value >= bottomIndex + step) {
			value -= step;
		} else if (value >= bottomIndex) {
			value = bottomIndex;
		}
		redrawCaption();
	}

	public void increment() {
		if (value <= topIndex - step) {
			value += step;
		} else if (value <= topIndex) {
			value = topIndex;
		}
		redrawCaption();
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value_) {
		value = value_;
		redrawCaption();

	}

	private void redrawCaption() {
		if (formatAsDollars) {
			final DecimalFormat df1 = new DecimalFormat("$#,###.##");
			caption.setText(df1.format((float) value / 1000000F) + "M");
		} else {
			caption.setText(new Integer(value).toString());
		}
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

}
