/*
 * Created on 2011-04-03
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.controls;

import java.awt.event.MouseWheelEvent;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class CheckBox extends Button {

	private boolean checked;

	public CheckBox(final AbsDims dims, final String text, final CommandToken<?> token, final boolean checked_) {
		super(dims, text, token, null);
		checked = checked_;
	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		checked = !checked;
		return true;
	}

	@Override
	public void render(final Graphics graphics, final boolean mousedown) {
		super.render(graphics, mousedown);

		final AbsDims dims = getScreenDims();

		graphics.setFontInfo(getEnvironment().getLabelFontInfo());
		graphics.drawText((checked ? "■" : "□"), dims.left,
				dims.bottom - WindowManager.getTextHeight(graphics.getFontInfo()));
		graphics.drawText(getText(), dims.left + 20, dims.bottom - WindowManager.getTextHeight(graphics.getFontInfo()));
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

}
