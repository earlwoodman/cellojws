/*
 * Created on 2010-07-11
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.controls.button;

import java.awt.Point;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public abstract class Button extends Control {

	private String text;

	private boolean mousedownOverride;

	private ButtonType buttonType;

	private int textHeight;

	private int textWidth;

	private int leftDrawText;

	private int topDrawText;

	private boolean metrics;

	private static final float STD_WIDTH_INCHES = 1.3F;

	private static final float STD_HEIGHT_INCHES = 0.26F;

	private static final float LRG_WIDTH_INCHES = 1.95F;

	private static final float LRG_HEIGHT_INCHES = 0.52F;

	private static int standardButtonHeight;

	private static int standardButtonWidth;

	private static int largeButtonHeight;

	private static int largeButtonWidth;

	static {
		if (environment == null) {
			throw new RuntimeException("Environment not initialized!");
		}

		final int dpi = environment.getWindowManager().getScreenDpi();

		standardButtonWidth = (int) (dpi * STD_WIDTH_INCHES);
		standardButtonHeight = (int) (dpi * STD_HEIGHT_INCHES);
		largeButtonWidth = (int) (dpi * LRG_WIDTH_INCHES);
		largeButtonHeight = (int) (dpi * LRG_HEIGHT_INCHES);
	}

	public static int getStandardButtonHeight() {
		return standardButtonHeight;
	}

	public static int getStandardButtonWidth() {
		return standardButtonWidth;
	}

	public static int getLargeButtonHeight() {
		return largeButtonHeight;
	}

	public static int getLargeButtonWidth() {
		return largeButtonWidth;
	}

	public Button(final FixedSizeButtonType size, final String text_, final CommandToken<?> token_, final Point pos) {
		super(createDims(size, pos), token_);
		initialize(size, text_);
	}

	public Button(final AbsDims dim, String text, CommandToken<?> token, final ButtonType type) {
		super(dim, token);
		initialize(type, text);
	}

	private void initialize(final ButtonType type, final String text_) {
		text = text_;
		this.buttonType = type;
		setJustification(Justification.Center);

		invalidateMetrics();
	}

	private void invalidateMetrics() {
		metrics = false;
	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return isEnabled();
	}

	@Override
	public boolean doSpecialMoveActions(int x, int y) {
		return super.doSpecialMoveActions(x, y);
	}

	@Override
	public void render(Graphics graphics, final boolean mousedown) {
		super.render(graphics, mousedown);

		final AbsDims dims = getScreenDims();

		if (isEnabled() && (mousedown || mousedownOverride) && WindowManager.getLastControl() == this) {
			if (this.buttonType.getMousedown() != null) {
				this.buttonType.getMousedown().render(graphics, dims);
			}
		} else {
			if (this.buttonType.getNormal() != null) {
				this.buttonType.getNormal().render(graphics, dims);
			}
		}

		if (!metrics) {
			textHeight = graphics.getTextHeight(getFontInfo());
			textWidth = graphics.getTextWidth(text, getFontInfo());
			if (getJustification() == Justification.Left) {
				leftDrawText = dims.left;
			} else {
				leftDrawText = (dims.getAbsWidth() - textWidth) / 2 + dims.left;
			}
			topDrawText = (dims.getAbsHeight() - textHeight) / 2 + dims.top - (dims.getAbsHeight() % 2 == 1 ? 1 : 0);
		}

		if (isMouseOver() && !mousedown && isEnabled()) {
			if (this.buttonType.getMouseover() != null) {
				this.buttonType.getMouseover().render(graphics, dims);
			}
		}

		graphics.drawText(text, leftDrawText, topDrawText);

		if (this.buttonType.getNormal() == null) {
			graphics.drawRect(dims);
		}

	}

	public String getText() {
		return text;
	}

	public void setText(final String text_) {
		text = text_;
		invalidateMetrics();
	}

	public ButtonType getButtonType() {
		return buttonType;
	}

	public static AbsDims createDims(final FixedSizeButtonType type, final Point pos) {
		return new AbsDims(pos.x, pos.y, type.getWidth() + pos.x, type.getHeight() + pos.y);
	}

	public boolean isMousedownOverride() {
		return mousedownOverride;
	}

	public void setMousedownOverride(boolean mousedownOverride) {
		this.mousedownOverride = mousedownOverride;
	}

	@Override
	public FontInfo getStandardFont() {
		return environment.getButtonFontInfo();
	}

}
