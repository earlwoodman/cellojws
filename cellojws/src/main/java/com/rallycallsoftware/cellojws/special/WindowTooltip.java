package com.rallycallsoftware.cellojws.special;

import java.awt.event.MouseEvent;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public abstract class WindowTooltip extends Tooltip {

	private AbsDims windowDims;

	private int width;

	private int height;

	public WindowTooltip(final Control owner, final int width, final int height) {
		super(owner);

		this.width = width;
		this.height = height;

		determineWindowDims(Environment.getEnvironment().getWindowManager());
	}

	@Override
	public void renderTooltip(final WindowManager windowManager) {
		setTooltipDisplayTime(System.currentTimeMillis());

		determineWindowDims(windowManager);

		windowManager.getGraphics().drawImage(Environment.getEnvironment().getSmallPopupImage(), windowDims);

	}

	private void determineWindowDims(final WindowManager windowManager) {
		final MouseEvent lastMouseMoveEvent = windowManager.getLastMouseMoveEvent();

		windowDims = calculateDimensions(windowManager, lastMouseMoveEvent.getX(), lastMouseMoveEvent.getY(), width,
				height);
	}

	public AbsDims getWindowDims() {
		return windowDims;
	}

	private static final long WINDOW_TOOLTIP_DISPLAY_LENGTH = 8000;

	@Override
	public long getTooltipDisplayLength() {
		return WINDOW_TOOLTIP_DISPLAY_LENGTH;
	}

}
