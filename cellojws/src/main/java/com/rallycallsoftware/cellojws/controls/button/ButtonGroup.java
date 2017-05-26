package com.rallycallsoftware.cellojws.controls.button;

import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class ButtonGroup extends Control {
	private List<Button> buttons = new ArrayList<>();

	public ButtonGroup(final AbsDims dims) {
		super(dims);
	}

	public Button addButton(String text, CommandToken<?> token) {
		final Button button = new BasicButton(null, text, token, SmallButtonType.getInstance());

		buttons.add(button);
		addControl(button);
		recreateButtonDims();
		return button;
	}

	public void removeButton(Button button) {
		buttons.remove(button);
		removeControl(button);
		recreateButtonDims();
	}

	private void recreateButtonDims() {

		final AbsDims dims = getDimensions();

		final int initialLeft = (dims.getAbsWidth()
				- (BasicButton.getStandardButtonWidth() * buttons.size() + Control.getStdGap() * (buttons.size() - 1)))
				/ 2;

		for (int i = 0; i < buttons.size(); i++) {
			final Button button = buttons.get(i);

			final int left = initialLeft + BasicButton.getStandardButtonWidth() * i + (Control.getStdGap() * i - 1);
			final AbsDims buttonDims = new AbsDims(left, dims.getAbsHeight() - BasicButton.getStandardButtonHeight(),
					left + BasicButton.getStandardButtonWidth(), dims.getAbsHeight());

			button.setDimensions(buttonDims);
		}
	}

}
