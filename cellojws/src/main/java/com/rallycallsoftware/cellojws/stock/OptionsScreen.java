package com.rallycallsoftware.cellojws.stock;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.OptionCheckBox;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.ButtonType;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.DateTime;
import com.rallycallsoftware.cellojws.general.Options;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class OptionsScreen extends Screen {

	private final static int gap = 25;

	private Options options;

	public OptionsScreen(final Options options_, final Consumer<CommandToken<OptionsPayload>> action) {
		super(environment.getSmallPopupDims(), environment.getSmallPopupImage());

		final AbsDims dims = environment.getSmallPopupDims();

		options = options_;

		options.load(environment.getExecutionPath());
		final List<String> optionKeys = options.getAllOptionKeys();
		final List<Boolean> optionValues = options.getAllOptionValues();

		OptionCheckBox checkBox;
		AbsDims checkBoxDims;
		int i = 0;
		for (String key : optionKeys) {
			i++;
			checkBoxDims = new AbsDims(gap, gap * i, dims.getAbsWidth() - gap * 2, gap * (i + 1));
			checkBox = new OptionCheckBox(checkBoxDims, key, optionValues.get(i - 1), action, this);
			addControl(key, checkBox);
		}

		final Point closePos = new Point(getLeftCentered(125),
				dims.getAbsHeight() - gap - Button.getStandardButtonHeight());
		addControl(new BasicButton(SmallButtonType.getInstance(), "Close",
				new CommandToken<Object>(ControlController::close), closePos));
	}

	@Override
	public CommandToken<?> enterKeyPressed() {
		return new CommandToken<Object>(ControlController::close);
	}

	@Override
	public CommandToken<?> escapeKeyPressed() {
		return enterKeyPressed();
	}

	@Override
	public void refresh() {

	}

	@Override
	public boolean validate() {
		return false;
	}

}
