/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.windowing;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.DropDownList;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.Gradient.Direction;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.token.CommandToken;

public abstract class Window<T extends WindowBean> extends Control {
	private static final Color darkBlue = Environment.getDarkBlue();

	private static final Color darkerBlue = new Color(42, 54, 65);

	private boolean closable;

	private String title;

	private Control focus = null;

	private boolean popup = false;

	private AbsDims shrunkDims;

	private WindowBean windowBean;
	
	public Window(final AbsDims dim, final Image backgrdImage, final T bean) {
		super(dim, null);

		windowBean = bean;
		
		if (backgrdImage != null) {
			setImage(backgrdImage);

			shrunkDims = getScreenDims().makeCopy();
			shrunkDims.shrink(0);
		}
	}

	public boolean is_closable() {
		return closable;
	}

	public void set_closable(final boolean close) {
		closable = close;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void render(final Graphics graphics, final boolean mousedown) {
		graphics.drawSolidRect(getScreenDims(), getBackground());
		
		final AbsDims titleDims = calculateTitleBarDims();

		final Image dropShadow = environment.getDropShadowImage();

		final AbsDims dropShadowDims = getScreenDims().makeCopy();
		dropShadowDims.merge(titleDims);
		dropShadowDims.expand(environment.tenthInch(), environment.tenthInch());

		graphics.drawImage(dropShadow, dropShadowDims);

		renderTitleBar(graphics, titleDims);

		super.render(graphics, mousedown);

		synchronized (controlsLock) {
			for (Control control : getControls()) {
				renderControl(control, graphics, mousedown);
			}
		}

	}

	private AbsDims calculateTitleBarDims() {
		final AbsDims screenDims = getScreenDims();

		// Only draw the title if not full screen
		if (screenDims.getAbsHeight() == getScreenHeight() && screenDims.getAbsWidth() == getScreenWidth()) {
			return null;
		}

		final int threeEightsInch = Math.round(environment.halfInch() * 0.75F);

		final AbsDims titleDims = new AbsDims(screenDims.left, screenDims.top - threeEightsInch - Control.getStdGap(),
				screenDims.right, screenDims.top - Control.getStdGap());

		return titleDims;
	}

	/**
	 * Renders a title bar if appropriate
	 * 
	 * @param graphics
	 * @return
	 */
	private void renderTitleBar(final Graphics graphics, final AbsDims titleDims) {
		if (titleDims == null) {
			return;
		}
		
		graphics.drawGradientRect(darkBlue, darkerBlue, titleDims.getTopHalf(), Direction.Downward);
		graphics.drawGradientRect(darkerBlue, darkBlue, titleDims.getBottomHalf(), Direction.Downward);

		final FontInfo savedFont = graphics.getFontInfo();

		graphics.setFontInfo(environment.getWindowTitleFont());

		final int width = WindowManager.getTextWidth(getTitle(), environment.getWindowTitleFont());
		final int height = WindowManager.getTextHeight(environment.getWindowTitleFont());

		graphics.drawGaussianFont(getTitle(), titleDims.left + (titleDims.getAbsWidth() - width) / 2,
				titleDims.top + (titleDims.getAbsHeight() - height) / 2);
		graphics.setFontInfo(savedFont);
	}

	/**
	 * renderControl method -- recursive!
	 * 
	 * @param control
	 * @param graphics
	 * @param mousedown
	 */
	private void renderControl(final Control control, final Graphics graphics, final boolean mousedown) {
		synchronized (controlsLock) {
			control.render(graphics, mousedown);
			for (Control child : control.getControls()) {
				renderControl(child, graphics, mousedown);
			}
		}
	}

	@Override
	public boolean doSpecialClickActions(final int x, final int y) {
		return true;
	}

	public boolean isPopup() {
		return popup;
	}

	public void setPopup(boolean popup) {
		this.popup = popup;
	}

	public void processMousePress(final int x, final int y) {

		synchronized (controlsLock) {
			final List<Control> reversed = getControlsReversed();

			for (Control control : reversed) {
				final AbsDims dims = control.getScreenDims();
				if (getWindowManager().isInside(dims, x, y)) {
					if (control instanceof Window) {
						control.processMousePress(x, y);
					} else {
						control.processMousePress(x - dims.left, y - dims.top);
					}

				}
			}
		}
	}

	public int getTextWidth(final String text, final FontInfo fontInfo) {
		return WindowManager.getTextWidth(text, fontInfo);
	}

	public int getTextHeight(final FontInfo fontInfo) {
		return WindowManager.getTextHeight(fontInfo);
	}

	@Override
	public void processMouseWheel(final MouseWheelEvent wheelEvent) {

	}

	/**
	 * Tells the window that a drop down list is dropping down.
	 * 
	 * Roll up all others, and move this list to the front
	 * 
	 * @param list
	 */
	public void childDroppingDown(final DropDownList list) {
		for (final Control control : getControlsRecursive()) {
			if (control != list && control instanceof DropDownList) {
				((DropDownList) control).rollUp();
			}
		}

		bringToFront(list.getParent());
	}

	public abstract CommandToken<?> enterKeyPressed();

	public abstract CommandToken<?> escapeKeyPressed();

	public abstract CommandToken<?> processKeyPress(final KeyEvent keyEvent);

	public Control getFocus() {
		return focus;
	}

	public void setFocus(Control focus) {
		this.focus = focus;
	}

	public WindowBean getWindowBean() {
		return windowBean;
	}

	public Label createLabel(final AbsDims dims, final String text) {
		final Label label = new Label(dims, text);
		addControl(label);
		return label;
	}

	public Button createSmallButton(final String caption, final CommandToken<?> token, final Point pos) {
		final Button button = new BasicButton(SmallButtonType.getInstance(), caption, token, pos);
		addControl(button);
		return button;
	}

	public abstract void refresh();

	public abstract boolean validate();

}
