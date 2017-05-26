package com.rallycallsoftware.cellojws.controls.button;

import java.awt.Point;

import com.rallycallsoftware.cellojws.controls.Pin;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class ButtonPin extends BasicButton implements Pin {

	public ButtonPin(final String text, final CommandToken<?> token, final Point pos) {
		super(SmallButtonType.getInstance(), text, token, pos);
	}

}
