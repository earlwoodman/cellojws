/*
 * Created on 2010-07-11
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.windowing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.ControlContainer;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.ProgressBar;
import com.rallycallsoftware.cellojws.controls.TransientControl;
import com.rallycallsoftware.cellojws.controls.listbox.ListBox;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.dimensions.PctDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.SerializableLock;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.special.Tooltip;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.tutorial.TutorialDialog;
import com.rallycallsoftware.cellojws.tutorial.TutorialItem;

public class WindowManager {

	private Set<WindowListener> listeners = new HashSet<WindowListener>();

	private static final long mouseSteadyDelay = 500;

	private final SerializableLock windowLock = new SerializableLock();

	private final List<Window> windows = new ArrayList<Window>();

	// Special windows are always drawn on top of regular windows
	// and can be tutorial windows, tool tips, etc.
	//
	private Tooltip tooltip;

	private TutorialDialog tutorialDialog;

	private ProgressBar systemProgress;

	private Label systemProgressBack;

	private Label systemProgressDropShadow;

	private Label systemError;

	private boolean showProgress = false;
	//
	// End of special windows

	private TutorialItem tutorialItem = null;

	private static Graphics graphics = null;

	private int screenWidth;

	private int screenHeight;

	private int screenDpi;

	private static Control lastFound;

	private boolean tooltipOn = false;

	private Point lastMousedown;

	private long lastMouseMovedTime;

	private Control lastMousedOverControl;

	private MouseEvent lastMouseMoveEvent;

	private final SerializableLock specialWindowLock = new SerializableLock();

	private static boolean mousedown;

	private int popupCount = 0;

	private DragInfo dragging = null;

	private enum MouseProcessType {
		Click, Move,
	}

	public WindowManager(final FontInfo errorFontInfo, final Color labelColor, final Graphics graphics_,
			final int width_, final int height_, final int dpi_) {
		graphics = graphics_;
		screenWidth = width_;
		screenHeight = height_;
		screenDpi = dpi_;

		synchronized (specialWindowLock) {
			final AbsDims dims = new AbsDims(width_ / 2 - 175, height_ / 2 - 10, width_ / 2 + 175, height_ / 2 + 10);
			systemProgress = new ProgressBar(dims, "", labelColor, Color.BLACK);
			systemProgressBack = new Label(dims, "");
			systemProgressBack.setSolidBackground(true);
			systemProgressBack.setBackground(Color.black);
			final AbsDims dropShadowDims = dims.makeCopy();
			dropShadowDims.move(2, 2);
			systemProgressDropShadow = new Label(dropShadowDims, "");
			systemProgressDropShadow.setSolidBackground(true);
			systemProgressDropShadow.setBackground(Color.gray);

			final int errorHeight = getTextHeight(errorFontInfo);
			final AbsDims systemErrorDims = new AbsDims(0, (screenHeight - errorHeight) / 2, screenWidth,
					(screenHeight - errorHeight) / 2 + errorHeight);
			systemError = new Label(systemErrorDims);
			systemError.setBackground(Color.BLACK);
			systemError.setSolidBackground(true);
			systemError.setJustification(Justification.Center);
			systemError.setFontInfo(errorFontInfo);
		}
	}

	public void setProgress(final float progress) {
		systemProgress.setPercentage(progress);
	}

	public void turnOnProgress() {
		synchronized (specialWindowLock) {
			showProgress = true;
		}

	}

	public void turnOffProgress() {
		synchronized (specialWindowLock) {
			showProgress = false;
		}
	}

	public void addWindow(final Window window) {
		synchronized (windowLock) {
			if (!windows.contains(window)) {
				if (window != null) {
					windows.add(window);
				}
			}
		}
	}

	public boolean containsWindow(final ControlContainer window) {
		synchronized (windowLock) {
			return windows.contains(window);
		}
	}

	public void render() {
		synchronized (windowLock) {
			for (Window window : windows) {
				window.render(graphics, mousedown);
			}
		}

		// Now render the tooltip, if there is one.
		synchronized (specialWindowLock) {
			renderTooltip();

			renderTutorial();

			renderSystemProgress();

			renderDraggingBox();

			renderSystemError();
		}

	}

	private void renderSystemError() {
		if (systemError.getText() != null && !systemError.getText().equals("")) {
			systemError.render(graphics, false);
		}
	}

	private void renderTutorial() {
		if (tutorialItem != null) {
			tutorialDialog = new TutorialDialog(tutorialItem);
			tutorialDialog.render(graphics, false);
		} else {
			tutorialDialog = null;
		}
	}

	private void renderSystemProgress() {
		if (showProgress) {
			systemProgressDropShadow.render(graphics, false);
			systemProgressBack.render(graphics, false);
			systemProgress.render(graphics, false);
		}
	}

	private void renderTooltip() {
		if (tooltip != null) {
			if (tooltip.getTooltipDisplayTime() - System.currentTimeMillis() > tooltip.getTooltipDisplayLength()) {
				tooltipOn = false;
				tooltip.reset();
			}

			if (tooltipOn) {
				tooltip.render(this);
			}
		}
	}

	private void renderDraggingBox() {
		if (dragging != null) {
			final Control beingDragged = dragging.getControlBeingDragged();

			final AbsDims dragDims = new AbsDims();

			dragDims.left = beingDragged.getScreenDims().left + dragging.getLastX() - dragging.getInitialX();
			dragDims.top = beingDragged.getScreenDims().top + dragging.getLastY() - dragging.getInitialY();
			dragDims.right = dragDims.left + beingDragged.getDimensions().getAbsWidth();
			dragDims.bottom = dragDims.top + beingDragged.getDimensions().getAbsHeight();

			final AbsDims savedDims = beingDragged.getScreenDims().makeCopy();
			beingDragged.setScreenDims(dragDims);
			beingDragged.dragRender(graphics, mousedown, savedDims, dragging);
			beingDragged.setScreenDims(savedDims);

			turnOffErrorMessage();
			turnOffProgress();
			turnOffTooltip();
		}
	}

	private List<Window> getWindowsReversed() {
		// Reverse the windows and go through them backwards
		final List<Window> reversed = new ArrayList<Window>();

		synchronized (windowLock) {
			for (Window window : windows) {
				reversed.add(0, window);
			}
		}

		return reversed;
	}

	public CommandToken<?> processMouseClick(final int x, final int y) {
		mousedown = true;
		lastMousedown = new Point(x, y);
		return processMouseAction(MouseProcessType.Click, x, y);
	}

	private CommandToken<?> processMouseAction(final MouseProcessType processType, final int x, final int y) {
		final Collection<Control> ignore = new ArrayList<Control>();
		Control control = null;

		do {
			control = findControlAt(ignore, x, y);
			if (control != null) {
				CommandToken<?> token = null;

				if (processType == MouseProcessType.Click) {
					token = clickThroughChain(x, y, control, control.getToken());

					removeTransients();

					if (token != null && control.isEnabled()) {
						return token;
					}
				} else if (processType == MouseProcessType.Move) {
					token = moveThroughChain(x, y, control, control.getToken());
					if (token != null) {
						return token;
					}
				}
				ignore.add(control);
			}
		} while (control != null);

		return null;
	}

	private void removeTransients() {
		final Collection<Control> controls = findAllControls();

		final Stream<Control> transients = controls.stream().filter(x -> x instanceof TransientControl);

		transients.forEach(x -> x.getParent().removeControl(x));
	}

	private CommandToken<?> clickThroughChain(final int x, final int y, final Control control,
			final CommandToken<?> token) {
		if (control != null) {
			final AbsDims dims = control.getScreenDims();
			if (control.doSpecialClickActions(x - dims.left, y - dims.top)) {
				if (control.getParent() != null) {
					// In case the return value is null, we want to ignore it
					// and return the original token
					// if the return value was not null, it should have been a
					// pointer to token anyway.
					clickThroughChain(x, y, control.getParent(), token);
				}
				if (control.getTutorialItem() != null) {
					setTutorialItem(control.getTutorialItem());
					control.getTutorialItem().setShown(true);
					control.setTutorialItem(null);
				} else {
					tutorialItem = null;
				}
			}
			return token;
		}

		return null;
	}

	private CommandToken<?> moveThroughChain(final int x, final int y, final Control control,
			final CommandToken<?> token) {
		if (control != null) {
			final AbsDims dims = control.getScreenDims();
			if (control.doSpecialMoveActions(x - dims.left, y - dims.top)) {
				if (control.getParent() != null) {
					// In case the return value is null, we want to ignore it
					// and return the original token
					// if the return value was not null, it should have been a
					// pointer to token anyway.
					moveThroughChain(x, y, control.getParent(), token);
				}
				return token;
			}
		}

		return null;
	}

	public void setGraphics2D(final Graphics2D g2D) {
		graphics.setGraphics2D(g2D);
	}

	public void removeWindow(final ControlContainer window) {
		synchronized (windowLock) {
			windows.remove(window);
		}
	}

	public void removeAllWindows() {
		synchronized (windowLock) {
			windows.clear();
		}

		// Just in case it's not set correctly.
		popupCount = 0;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getScreenDpi() {
		return screenDpi;
	}

	public static int getTextWidth(final String text, final FontInfo fontInfo) {
		return graphics.getTextWidth(text, fontInfo);
	}

	public static int getTextHeight(final FontInfo fontInfo) {
		return graphics.getTextHeight(fontInfo);
	}

	public void nullAllWindows() {
		synchronized (windowLock) {
			for (@SuppressWarnings("unused")
			ControlContainer window : windows) {
				// Is there any point in doing this?
				window = null;
			}
		}
	}

	public void processMouseWheel(MouseWheelEvent arg0) {
		final Control control = findControlAt(null, arg0.getX(), arg0.getY());

		if (control != null) {
			control.processMouseWheel(arg0);
		}
	}

	public boolean isInside(final AbsDims dims, final int x, final int y) {
		if (dims != null) {
			AbsDims dimsToUse = dims;
			if (dims instanceof PctDims) {
				dimsToUse = dims.absoluteify();
			}

			if (x > dimsToUse.left && x < dimsToUse.right && y > dimsToUse.top && y < dimsToUse.bottom) {
				return true;
			}
		}

		return false;

	}

	public Control findControlAt(final int x, final int y) {
		return findControlAt(new ArrayList<Control>(), x, y);
	}

	/**
	 * Finds and returns the control at the given screen location
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Control findControlAt(final Collection<Control> ignore, final int x, final int y) {
		final List<Window> reversed = getWindowsReversed();

		lastFound = null;

		synchronized (windowLock) {
			for (Window window : reversed) {
				final Control clicked = findControlInControl(window.getControlsReversed(), ignore,
						x - window.getDimensions().left, y - window.getDimensions().top);
				if (clicked != null) {
					return clicked;
				}
				if (window.isPopup()) {
					// This case occurs if no control on the popup is clicked.
					return null;
				}
			}
		}

		return null;
	}

	private Control findControlInControl(final Collection<Control> controls, final Collection<Control> ignore,
			final int x, final int y) {

		if (controls != null) {
			for (Control control : controls) {
				if (!(ignore != null && ignore.contains(control))) {
					if (isInside(control.getDimensions(), x, y)) {
						final Control child = findControlInControl(control.getControlsReversed(), ignore,
								x - control.getDimensions().left, y - control.getDimensions().top);
						if (lastFound != null) {
							return lastFound;
						}
						if (child == null) {
							lastFound = control;
							return lastFound;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Removes the top window from the stack
	 * 
	 */
	public void removeTopWindow() {
		popupCount--;
		for (final WindowListener listener : listeners) {
			listener.closePopup(popupCount);
		}

		synchronized (windowLock) {
			windows.remove(windows.size() - 1);
		}

	}

	/**
	 * Decide what state tooltipmode should be in based on the mouse move event
	 * and the last moused-over control
	 * 
	 * @param event
	 */
	public void processMouseMove(final MouseEvent event) {

		final Control control = findControlAt(null, event.getX(), event.getY());

		clearMouseOvers(control);

		synchronized (specialWindowLock) {
			if (control != null && control instanceof ListBox) {
				// Special case for listboxes
				((ListBox) control).setTooltipByPosition(event.getY() - control.getScreenDims().top);
			}
			if (control != null) {
				tooltipOn = true;
				lastMouseMovedTime = System.currentTimeMillis();
				lastMouseMoveEvent = event;
				lastMousedOverControl = control;
				tooltip = lastMousedOverControl.getTooltip();
			} else {
				turnOffTooltip();
			}

		}

		processMouseAction(MouseProcessType.Move, event.getX(), event.getY());
	}

	private void turnOffTooltip() {
		synchronized (specialWindowLock) {
			tooltipOn = false;
			tooltip = null;
		}
	}

	private void clearMouseOvers(final Control mouseOverControl) {
		final Collection<Control> mouseOverControls = mouseOverControl != null ? mouseOverControl.findAllParents()
				: null;

		final Collection<Control> allControls = findAllControls();

		for (final Control control : allControls) {
			if (mouseOverControls == null || !mouseOverControls.contains(control)) {
				control.clearMouseOver();
			}
		}
	}

	private Collection<Control> findAllControls() {
		final Collection<Control> ret = new ArrayList<Control>();

		synchronized (windowLock) {
			for (final Window window : windows) {
				ret.addAll(window.getControlsRecursive());
			}
		}

		return ret;
	}

	public void mouseReleased(final int x, final int y) {
		mousedown = false;
		if (dragging != null) {
			final Control dropTarget = findControlAt(x, y);
			// if( dropTarget.isDropTarget() )
			{
				dropTarget.drop(dragging.getControlBeingDragged());
			}
			dragging = null;
		}
	}

	public void mousePressed() {
		mousedown = true;
		turnOffErrorMessage();
	}

	public void turnOffErrorMessage() {
		synchronized (specialWindowLock) {
			systemError.setText("");
		}
	}

	public static Control getLastControl() {
		return lastFound;
	}

	public Control getLastMousedOverControl() {
		return lastMousedOverControl;
	}

	public long getLastMouseMovedTime() {
		return lastMouseMovedTime;
	}

	public long getMouseSteadyDelay() {
		return mouseSteadyDelay;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public MouseEvent getLastMouseMoveEvent() {
		return lastMouseMoveEvent;
	}

	public CommandToken<?> enterKeyPressed() {
		synchronized (windowLock) {
			return getWindowsReversed().get(0).enterKeyPressed();
		}

	}

	public CommandToken<?> escapeKeyPressed() {
		synchronized (windowLock) {
			return getWindowsReversed().get(0).escapeKeyPressed();
		}

	}

	public CommandToken<?> processKeyPress(final KeyEvent arg0) {
		synchronized (windowLock) {
			if (countWindows() > 0) {
				return getWindowsReversed().get(0).processKeyPress(arg0);
			}
		}

		return null;
	}

	private int countWindows() {
		synchronized (windowLock) {
			return windows.size();
		}
	}

	public TutorialItem getTutorialItem() {
		return tutorialItem;
	}

	public void setTutorialItem(TutorialItem tutorialItem) {
		this.tutorialItem = tutorialItem;
	}

	public void setProgressMessage(final String message) {
		systemProgress.setText(message);
		systemProgressBack.setText(message);

	}

	public Screen getScreen(Class<?> class1) {
		synchronized (windowLock) {
			for (ControlContainer window : windows) {
				if (window.getClass() == class1) {
					if (window instanceof Screen) {
						return (Screen) window;
					}
				}
			}
		}

		return null;
	}

	public void showPopup(final Window popup) {
		popupCount++;
		popup.setPopup(true);
		addWindow(popup);
		for (final WindowListener listener : listeners) {
			listener.showPopup(popupCount);
		}
	}

	public void closeAllPopups() {

		for (int i = 0; i < popupCount; i++) {
			removeTopWindow();
		}

		popupCount = 0;
		for (final WindowListener listener : listeners) {
			listener.closeAllPopups();
		}

	}

	public void processMouseDrag(MouseEvent arg0) {
		synchronized (specialWindowLock) {
			final Control control = findControlAt(arg0.getX(), arg0.getY());
			if (control != null && control.isDraggable() && dragging == null) {
				dragging = new DragInfo();
				dragging.setControlBeingDragged(control);
				dragging.setInitialMouseEvent(arg0);
			}

			if (dragging != null) {
				dragging.setMouseEvent(arg0);
			}
		}
	}

	public static int getTextHeight() {
		return getTextHeight(graphics.getFontInfo());
	}

	public void systemError(String message) {
		systemError.setText(message);
		WorkerLog.debug(message);
	}

	public Point getLastMouseDown() {
		return lastMousedown;
	}

	public static void adjustFont(final FontInfo fontInfo, final String message, final int boxWidth,
			final int boxHeight) {
		int renderWidth;
		int renderHeight;
		do {
			fontInfo.setFontSize(fontInfo.getFontSize() - 1);
			renderWidth = WindowManager.getTextWidth(message, fontInfo);
			renderHeight = WindowManager.getTextHeight(fontInfo);
		} while ((renderWidth > boxWidth || renderHeight > boxHeight) && fontInfo.getFontSize() > 7);
	}

	public boolean isDragging() {
		return dragging != null;
	}

	public boolean isMouseDown() {
		return mousedown;
	}

	public <T> void removeWindows(Class<T> clazz) {
		synchronized (windowLock) {
			final List<Window> windowsToRemove = windows.stream().filter(x -> x.getClass() == clazz)
					.collect(Collectors.toList());

			windows.removeAll(windowsToRemove);
		}
	}

}