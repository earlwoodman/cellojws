package com.rallycallsoftware.cellojws.controls;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class PinBoard extends Control {

	private List<Pin> pins = new ArrayList<Pin>();

	private final int gap = 10;

	public PinBoard(final AbsDims dim, final CommandToken<?> token_, final Image backgroundPic) {
		super(dim, token_);

		final AbsDims backgrdDims = new AbsDims(0, 0, dim.getAbsWidth(), dim.getAbsHeight());
		final Control background = new Control(backgrdDims, null);
		background.setImage(backgroundPic);
		addControl(background);
	}

	/**
	 * Adds the specified pin to the board.
	 * 
	 * @param pin
	 */
	public void addNewPin(final Pin pin) {
		pins.add(pin);
		addControl((Control) pin);
	}

	/**
	 * Creates a new pin and adds to the board
	 * 
	 * @param text
	 * @param token
	 */
	public void makeNewPin(final String text, final Map<String, CommandToken<?>> tokens, final boolean deleteable,
			final boolean moveable) {

		final AbsDims dims = getNextPinDims();

		final LabelPin pin = new LabelPin(dims, text, tokens, deleteable, moveable);

		pins.add(pin);

		addControl(pin);
	}

	/**
	 * Returns the dims of the next pin
	 * 
	 * @return
	 */
	public AbsDims getNextPinDims() {
		return getNextPinDims(pins.size() + 1);
	}

	private AbsDims getNextPinDims(final int count) {

		final int width = getDimensions().getAbsWidth();
		final int height = 20;

		return new AbsDims(gap, (count - 1) * height + 5, width - gap, count * height + 5);

	}

	/**
	 * Removes the first pin found that has the caption specified
	 * 
	 * @param text
	 */
	public void removePin(final Control pinToRemove) {
		removeControl(pinToRemove);
		pins.remove(pinToRemove);

		// Now go through all pins and give them new dimensions

		int count = 1;
		for (final Control control : getControls()) {
			if (!(control.getImage() == null)) {
				control.setDimensions(getNextPinDims(count));
				if (control instanceof LabelPin) {
					((LabelPin) control).refreshButtons();
				}
				count++;
			}
		}

	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {

	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return true;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

	/**
	 * Removes all pins from the board by removing from both the list here and
	 * from all child controls
	 */
	public void removeAllPins() {
		for (Pin pin : pins) {
			if (pin instanceof Control) {
				removeControl((Control) pin);
			}
		}

		pins.clear();

	}

}
