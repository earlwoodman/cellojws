package com.rallycallsoftware.cellojws.special;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public abstract class Tooltip extends SpecialWindow {

	public abstract long getTooltipDisplayLength();

	private long tooltipDisplayTime;

	private Control owner;

	public Tooltip(Control owner) {
		this.owner = owner;
	}

	@Override
	public void render(final WindowManager windowManager) {
		final long elapsed = System.currentTimeMillis() - windowManager.getLastMouseMovedTime();

		if (elapsed > windowManager.getMouseSteadyDelay() && elapsed < getTooltipDisplayLength()) {
			renderTooltip(windowManager);
		}

		owner.renderingTooltip();
	}

	public abstract void renderTooltip(final WindowManager windowManager);

	public void setTooltipDisplayTime(long tooltipDisplayTime) {
		this.tooltipDisplayTime = tooltipDisplayTime;
	}

	public long getTooltipDisplayTime() {
		return tooltipDisplayTime;
	}

	public AbsDims calculateDimensions(final WindowManager windowManager, final int x, final int y, final int width,
			final int height) {
		// Roughly a third of an inch offset from the mouse pointer
		final int yOffset = Math.round(windowManager.getScreenDpi() * 0.33F);

		final AbsDims tooltipDims = new AbsDims(x - 1, y - 1 + yOffset, x + width + 1, y + height + yOffset);

		if (tooltipDims.right > windowManager.getScreenWidth()) {
			tooltipDims.left -= tooltipDims.right - windowManager.getScreenWidth();
			tooltipDims.right -= tooltipDims.right - windowManager.getScreenWidth();
		}
		if (tooltipDims.bottom > windowManager.getScreenHeight()) {
			tooltipDims.top -= tooltipDims.bottom - windowManager.getScreenHeight();
			tooltipDims.bottom -= tooltipDims.bottom - windowManager.getScreenHeight();
		}
		return tooltipDims;
	}

	public abstract void reset();

}
