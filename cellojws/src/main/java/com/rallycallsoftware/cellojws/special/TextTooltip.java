package com.rallycallsoftware.cellojws.special;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class TextTooltip extends Tooltip {
	private static final long TOOLTIP_DISPLAY_WINDOW = 4000;

	private String text;

	private Environment environment;

	public TextTooltip(final Control owner) {
		super(owner);
		environment = Environment.getEnvironment();
	}

	@Override
	public long getTooltipDisplayLength() {
		return TOOLTIP_DISPLAY_WINDOW;
	}

	@Override
	public void renderTooltip(final WindowManager windowManager) {
		final Graphics graphics = windowManager.getGraphics();

		if (text != null && text.length() > 0) {
			setTooltipDisplayTime(System.currentTimeMillis());

			final MouseEvent lastMouseMoveEvent = windowManager.getLastMouseMoveEvent();

			final int width = graphics.getTextWidth(text, environment.getToolTipFontInfo());
			final int height = graphics.getTextHeight(environment.getToolTipFontInfo());
			final int x = lastMouseMoveEvent.getX();
			final int y = lastMouseMoveEvent.getY();

			final AbsDims tooltipDims = calculateDimensions(windowManager, x, y, width, height);

			graphics.drawSolidRect(tooltipDims, Color.BLACK);
			graphics.drawRect(tooltipDims);
			graphics.setFontInfo(environment.getToolTipFontInfo());
			graphics.drawText(text, tooltipDims.left, tooltipDims.top);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void reset() {
		setText("");
	}

}
