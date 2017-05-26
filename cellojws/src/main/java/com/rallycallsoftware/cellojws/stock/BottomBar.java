/*
 * Created on 2010-10-31
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.stock;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.ProgressBar;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Event;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.Pauser;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class BottomBar extends Screen implements Serializable, Pauser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3001L;

	private ProgressBar clock;

	private Label event;

	// Corner Buttons
	//
	private Button pause;

	private Button save;

	// Info Buttons
	//
	// private Button home;

	private Button events;

	private Control rightLogo;

	private Control leftLogo;

	private Control backgroundLeft;
	private Control backgroundCenter;
	private Control backgroundRight;

	private final int thickness;

	public BottomBar(final AbsDims dims) {
		super(dims, null);

		thickness = dims.getAbsHeight();

		final Image backgroundLeftImage = ImageFactory.getImage("Background/i.png");
		backgroundLeft = new Control(new AbsDims(0, 0, 100, thickness), null);
		backgroundLeft.setImage(backgroundLeftImage);
		addControl(backgroundLeft);

		final Image backgroundCenterImage = ImageFactory.getImage("Background/j.png");
		backgroundCenter = new Control(new AbsDims(100, 0, dims.getAbsWidth() - 100, thickness), null);
		backgroundCenter.setImage(backgroundCenterImage);
		addControl(backgroundCenter);

		final Image backgroundRightImage = ImageFactory.getImage("Background/k.png");
		backgroundRight = new Control(new AbsDims(dims.getAbsWidth() - 100, 0, dims.getAbsWidth(), thickness), null);
		backgroundRight.setImage(backgroundRightImage);
		addControl(backgroundRight);

		// setTitle(caption);

		final AbsDims clockDims = new AbsDims(1, 30, 600, 50); // Left and right
																// dimensions
																// will be
																// overwritten
		// final Dimensions clockDims = new Dimensions(1, 48, 600, 68); // Left
		// and right dimensions will be overwritten
		clock = new ProgressBar(clockDims, "", Color.GREEN, Color.BLACK);
		addControl(clock);

		final AbsDims pauseDims = new AbsDims(4, 48, 129, 73);
		pause = new BasicButton(SmallButtonType.getInstance(), "Start",
				new CommandToken<Object>(ControlController::pause), new Point(4, 48));
		addControl(pause);

		save = new BasicButton(SmallButtonType.getInstance(), "Save", new CommandToken<Object>(ControlController::save),
				new Point(getDimensions().getAbsWidth() - 129, 48));
		addControl(save);

		final Image image = ImageFactory.getImage("logo/HockeyBrass_small.png");
		final AbsDims leftLogoDims = new AbsDims(6, 3, image.getWidth() + 6, 53);
		leftLogo = new Control(leftLogoDims, createShowAboutToken());
		leftLogo.setImage(image);
		addControl(leftLogo);

		final AbsDims logoDims = new AbsDims(dims.getAbsWidth() - image.getWidth() - 4, 3, dims.getAbsWidth() - 4, 53);
		rightLogo = new Control(logoDims, createShowAboutToken());
		rightLogo.setImage(image);
		addControl(rightLogo);

		// Now do the array of buttons so they are centered
		//
		final int width = getDimensions().getAbsWidth();
		final int buttons = 8;
		final int buttonWidth = 125;

		final int overallLeft = (width - buttons * buttonWidth) / 2;

		// Info
		//

		events = new BasicButton(SmallButtonType.getInstance(), "Events",
				new CommandToken<Object>(ControlController::showEvents), new Point(overallLeft + buttonWidth * 3, 4));
		addControl(events);

	}

	private CommandToken<?> createShowAboutToken() {
		return new CommandToken<Object>(ControlController::showAbout);
	}

	@Override
	public void refresh() {
		final boolean debug = environment.isDebugMode();
		/*
		 * final float pctDayDone = (float)(time.getHour() + 1) / 24F;
		 * 
		 * if( debug ) { clock.setText(time.toString()); } else {
		 * clock.setText(time.toStringFormatted()); }
		 * clock.setPercentage(pctDayDone);
		 */
		final AbsDims dims = clock.getDimensions();
		final int clockWidth = getTextWidth(clock.getText(), environment.getLabelFontInfo());
		dims.left = getLeftCentered(clockWidth);
		dims.right = getRightCentered(clockWidth);
		clock.setDimensions(dims);

		final Event importantEvent = null;// getGameManager().getGameUnit().getProLeague().getLastImportantEvent();
		if (importantEvent != null) {

			final AbsDims eventDims = new AbsDims(pause.getDimensions().right + 5, 48, save.getDimensions().left - 5,
					68);
			removeControl(this.event);
			this.event = new Label(eventDims, importantEvent.toString(), Justification.Center);
			addControl(this.event);
		}

	}

	/*
	 * public void setTitle(final String caption) { final Dimensions labelDims =
	 * new Dimensions( getLeftCentered(getTextWidth(caption,
	 * getGameManager().getLabelFontInfo())), 26,
	 * getRightCentered(getTextWidth(caption,
	 * getGameManager().getLabelFontInfo())), 46 ); title = new Label(labelDims,
	 * caption, getGameManager()); addControl(title);
	 * 
	 * }
	 */
	public void disablePause() {
		pause.disable();
	}

	public void enablePause() {
		pause.enable();
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void pause() {
		pause.setText("Resume");
	}

	@Override
	public void unpause() {
		pause.setText("Pause");
	}

	@Override
	public CommandToken<?> enterKeyPressed() {
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed() {
		return null;
	}

}
