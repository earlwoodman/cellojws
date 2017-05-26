package com.rallycallsoftware.cellojws.controls;

import java.util.function.Consumer;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.stock.OptionsPayload;
import com.rallycallsoftware.cellojws.stock.OptionsScreen;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class OptionCheckBox extends CheckBox {

	private OptionsPayload payload = new OptionsPayload();

	public OptionCheckBox(final AbsDims dims, final String text, final boolean checked_,
			final Consumer<CommandToken<OptionsPayload>> action, final OptionsScreen screen) {
		super(dims, text, null, checked_);

		payload.setOptionsScreen(screen);
		payload.setValue(checked_);
		payload.setKey(text);
		payload.setCheckBox(this);

		final CommandToken<OptionsPayload> token = new CommandToken<OptionsPayload>(action, payload);
		setToken(token);
	}

}
