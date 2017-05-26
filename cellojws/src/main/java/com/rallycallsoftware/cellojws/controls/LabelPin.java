package com.rallycallsoftware.cellojws.controls;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.Map;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.DownButtonType;
import com.rallycallsoftware.cellojws.controls.button.SmallDeleteButtonType;
import com.rallycallsoftware.cellojws.controls.button.UpButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class LabelPin extends Label implements Pin {

	private Button delete;

	private Button up;

	private Button down;

	public LabelPin(final AbsDims dims, final String text_, final Map<String, CommandToken<?>> tokens_,
			final boolean deleteable, final boolean moveable) {
		super(dims, text_);

		if (moveable) {
			up = new BasicButton(UpButtonType.getInstance(), "", tokens_ != null ? tokens_.get("up") : null,
					getUpBoxPos());
			addControl(up);

			down = new BasicButton(DownButtonType.getInstance(), "", tokens_ != null ? tokens_.get("down") : null,
					getDownBoxPos());
			addControl(down);
		}

		if (deleteable) {
			delete = new BasicButton(SmallDeleteButtonType.getInstance(), "",
					tokens_ != null ? tokens_.get("delete") : null, getDeleteBoxPos());
			addControl(delete);
		}

	}

	private Point getDeleteBoxPos() {
		final AbsDims dims = getDimensions();
		final int boxHeight = ImageFactory.getButtonSmallSize();

		return new Point(dims.getAbsWidth() - boxHeight, dims.getAbsHeight() - boxHeight);
	}

	private Point getUpBoxPos() {
		final AbsDims dims = getDimensions();
		final int boxHeight = ImageFactory.getButtonSmallSize();

		return new Point(dims.getAbsWidth() - boxHeight * 3, dims.getAbsHeight() - boxHeight);
	}

	private Point getDownBoxPos() {
		final AbsDims dims = getDimensions();
		final int boxHeight = ImageFactory.getButtonSmallSize();

		return new Point(dims.getAbsWidth() - boxHeight * 2, dims.getAbsHeight() - boxHeight);
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

	public void refreshButtons() {
		// TODO Consider if this needs to change
		if (delete != null) {
			// delete.setDimensions(getDeleteBoxDimensions());
		}

		if (up != null) {
			// up.setDimensions(getUpBoxDimensions());
		}

		if (down != null) {
			// down.setDimensions(getDownBoxDimensions());
		}
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {
		if (delete != null) {
			final AbsDims dims = getScreenDims();

			graphics.drawText(getText(), dims.left, dims.top,
					dims.getAbsWidth() - delete.getDimensions().getAbsWidth());
		} else {
			super.render(graphics, mousedown);
		}

	}

	public CommandToken<?> getDeleteToken() {
		if (delete != null) {
			return delete.getToken();
		}

		return null;
	}

	public CommandToken<?> getUpToken() {
		if (up != null) {
			return up.getToken();
		}

		return null;
	}

	public CommandToken<?> getDownToken() {
		if (down != null) {
			return down.getToken();
		}

		return null;
	}

}
