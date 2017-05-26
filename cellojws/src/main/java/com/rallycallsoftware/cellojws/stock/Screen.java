/*
 * Created on 2010-10-17
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.stock;

import java.awt.Point;
import java.awt.event.KeyEvent;

import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;

public abstract class Screen extends Window {

	public Screen(final AbsDims dims, final Image backgrdImage) {
		super(dims, backgrdImage);
	}

	abstract public void refresh();

	public abstract boolean validate();

	public Label createLabel(final AbsDims dims, final String text) {
		final Label label = new Label(dims, text);
		addControl(label);
		return label;
	}

	public Button createSmallButton(final String caption, final CommandToken<?> token, final Point pos) {
		final Button button = new BasicButton(SmallButtonType.getInstance(), caption, token, pos);
		addControl(button);
		return button;
	}

	@Override
	public CommandToken<?> processKeyPress(KeyEvent keyEvent) {
		return null;
	}

}
