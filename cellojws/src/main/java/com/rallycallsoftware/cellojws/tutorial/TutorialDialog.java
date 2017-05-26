package com.rallycallsoftware.cellojws.tutorial;

import java.awt.event.KeyEvent;

import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;

public class TutorialDialog extends Window {

	private TutorialItem tutorialItem;

	private Label message;

	public TutorialDialog(final TutorialItem tutorialItem) {
		super(tutorialItem.getScreenDims(), ImageFactory.getTutorialBackground());

		setTutorialItem(tutorialItem);

		setPopup(true);
	}

	private void recreateMessageLabel(final AbsDims dim) {
		removeControl(message);

		final AbsDims whole = new AbsDims(19, 19, dim.getAbsWidth() - 19, dim.getAbsHeight() - 19);
		message = new Label(whole, "");
		message.setWrap(true);
		addControl(message);
	}

	@Override
	public CommandToken<?> enterKeyPressed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed() {
		// TODO Auto-generated method stub
		return null;
	}

	public TutorialItem getTutorialItem() {
		return tutorialItem;
	}

	public void setTutorialItem(TutorialItem tutorialItem) {
		this.tutorialItem = tutorialItem;

		recreateMessageLabel(tutorialItem.getScreenDims());

		message.setText(tutorialItem.getMessage());
	}

	@Override
	public CommandToken<?> processKeyPress(KeyEvent keyEvent) {
		return null;
	}

}
