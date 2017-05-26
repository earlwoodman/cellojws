package com.rallycallsoftware.cellojws.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.RadioButtonInfoBean;
import com.rallycallsoftware.cellojws.controls.button.RadioButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class RadioButtons extends Control {

	private List<Button> buttons = new ArrayList<>();

	public RadioButtons(final AbsDims dims, final RadioButtonInfoBean... beans) {
		super(dims);

		buttons = new ArrayList<>();

		final RadioButtonType radioButtonType = new RadioButtonType();
		for (int i = 0; i < beans.length; i++) {
			final AbsDims sliceDims = new AbsDims(0, i * radioButtonType.getHeight(), radioButtonType.getWidth(),
					(i + 1) * radioButtonType.getHeight());
			final CommandToken<RadioButtonInfoBean> token = new CommandToken<>(ControlController::turnOnRadioButton);
			final Button button = new BasicButton(sliceDims, beans[i].getCaption(), token, new RadioButtonType(0 == i));

			button.setFontInfo(environment.getRadioButtonFontInfo());
			button.setJustification(Justification.Left);
			beans[i].setButtons(buttons);
			beans[i].setClickedButton(button);
			token.setPayload(beans[i]);
			addControl(button);
			buttons.add(button);
		}
	}

	public int getChecked() {
		final Optional<Button> button = buttons.stream().filter(x -> ((RadioButtonType) x.getButtonType()).isChecked())
				.findFirst();

		if (button.isPresent()) {
			return buttons.indexOf(button.get());
		}

		return -1;
	}

}
