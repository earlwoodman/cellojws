package com.rallycallsoftware.cellojws.stock;

import java.awt.Point;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Event;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class EventInfoScreen extends Screen {

	private Event event;

	private Label caption;

	private Button close;

	private Label title;

	private Control logoCtr;

	public EventInfoScreen(final AbsDims dim, final Event event_, final Image logo) {
		super(dim, environment.getSmallPopupImage());

		event = event_;

		int textHeight = WindowManager.getTextHeight(environment.getLabelFontInfo());
		final AbsDims captionDims = new AbsDims(0, (dim.getAbsHeight() - textHeight) / 2, dim.getAbsWidth(),
				(dim.getAbsHeight() - textHeight) / 2 + textHeight);
		caption = new Label(captionDims, event.toString(), Justification.Center);
		addControl(caption);

		close = new BasicButton(SmallButtonType.getInstance(), "Close",
				new CommandToken<Object>(ControlController::close),
				new Point((dim.getAbsWidth() - 125) / 2, dim.getAbsHeight() - 60));
		addControl(close);

		final AbsDims titleDims = new AbsDims(0, 50, dim.getAbsWidth(), 100);
		title = new Label(titleDims, "Notice", Justification.Center);
		final FontInfo override = environment.getLabelFontInfo();
		override.setFontSize(36);
		title.setFontInfo(override);

		addControl(title);

		if (logo != null) {
			final AbsDims logoDims = new AbsDims(25, 25, 25 + logo.getWidth(), 25 + logo.getHeight());
			logoCtr = new Control(logoDims, null);
			logoCtr.setImage(logo);
			addControl(logoCtr);
		}

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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
