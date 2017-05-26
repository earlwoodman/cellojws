/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.controls;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.SerializableLock;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.special.Tooltip;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.tutorial.TutorialItem;
import com.rallycallsoftware.cellojws.windowing.DragInfo;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class Control implements ControlContainer {
	private Justification justification;

	private AbsDims dimensions;

	private AbsDims screenDimensions = null;

	private Control parent;

	private final List<Control> componentControls = new ArrayList<Control>();

	private final List<String> componentControlNames = new ArrayList<String>();

	private CommandToken<?> token;

	private CommandToken<DragDropBean> dropToken = null;

	private boolean beingProcessed = false;

	protected final SerializableLock controlsLock = new SerializableLock();

	private Tooltip tooltip;

	private FontInfo fontInfo = null;

	private FontInfo disabledFontInfo = null;

	private TutorialItem tutorialItem;

	private Image image = null;

	private float xRatio;

	private float yRatio;

	private Collection<PolygonControl> polygons = new ArrayList<PolygonControl>();

	private Color background = Color.WHITE;

	private boolean mouseOver = false;

	private AffineTransform at;

	private boolean rotation = false;

	private boolean draggable = false;

	private boolean drawBorder = false;

	private boolean enabled = true;

	private static final Color controlBorderColour = new Color(40, 30, 20);

	protected static Environment environment;

	public static void setEnvironment(Environment environment) {
		Control.environment = environment;
	}

	public static Environment getEnvironment() {
		return environment;
	}

	public Control() {
		initialize();
	}

	public Control(final AbsDims dim) {
		if (dim != null) {
			dimensions = dim.absoluteify();
		}

		token = null;

		initialize();
	}

	public Control(final AbsDims dim, final CommandToken<?> token_) {
		if (dim != null) {
			dimensions = dim.absoluteify();
		}

		token = token_;

		initialize();
	}

	private void initialize() {
		fontInfo = getStandardFont();

		initAffineTransform();
	}

	private void initAffineTransform() {
		at = new AffineTransform();
	}

	private boolean containsControl(final Control control) {
		synchronized (controlsLock) {
			return componentControls.contains(control);
		}
	}

	public AbsDims getScreenDims() {
		if (parent != null) {
			if (screenDimensions == null) {
				final AbsDims parentScreenDims = parent.getScreenDims();
				screenDimensions = dimensions.absoluteify().shift(parentScreenDims);
			}

			return screenDimensions;
		}

		return dimensions.absoluteify();
	}

	public AbsDims getDimensions() {
		return dimensions;
	}

	public void render(final Graphics graphics, final boolean mousedown) {
		graphics.setFontInfo(getFontToUse());
		if (drawBorder) {
			final AbsDims expandedDims = getScreenDims().makeCopy();
			expandedDims.expand(getStdGap(), getStdGap());
			graphics.drawRect(expandedDims, controlBorderColour);
		}
		if (image != null) {
			setxRatio((float) getScreenDims().getAbsWidth() / image.getWidth());
			setyRatio((float) getScreenDims().getAbsHeight() / image.getHeight());

			final AbsDims drawDimensions = determineImageDimsToUse();
			graphics.drawImage(image.getBufferedImage(), drawDimensions, rotation ? at : null);
		}

	}

	private FontInfo getFontToUse() {
		if (!enabled) {
			if (disabledFontInfo == null) {
				disabledFontInfo = fontInfo != null ? fontInfo.makeCopy() : getStandardFont().makeCopy();
				disabledFontInfo.setTextColor(Color.DARK_GRAY);
			}
			return disabledFontInfo;
		}

		if (fontInfo != null) {
			return fontInfo;
		}

		return getStandardFont();
	}

	/**
	 * This method and doSpecialMoveActions() are for behaviours that ALL
	 * objects of the type should express. The command system is for behaviours
	 * that a particular object of the type should express.
	 */
	public boolean doSpecialClickActions(final int x, final int y) {
		return true;
	}

	public void processMousePress(final int x, final int y) {
		setBeingProcessed(true);
	}

	public void setParent(final Control control) {
		parent = control;
	}

	public Control getParent() {
		return parent;
	}

	public CommandToken<?> processKeyPress(final KeyEvent keyEvent) {
		return null;
	}

	public CommandToken<?> getToken() {
		return token;
	}

	public void setToken(final CommandToken<?> token) {
		this.token = token;
	}

	public final void setDimensions(final AbsDims dims) {
		dimensions = dims;
		screenDimensions = null;
		dimensionsChanged();
	}

	public void dimensionsChanged() {

	}

	public void setBeingProcessed(final boolean beingProcessed_) {
		beingProcessed = beingProcessed_;
	}

	public boolean isBeingProcessed() {
		return beingProcessed;
	}

	public void addControl(final String key, final Control control_) {
		synchronized (controlsLock) {
			if (containsControl(control_) == false) {
				control_.setParent(this);
				componentControls.add(control_);
				componentControlNames.add(key);
			}
		}

	}

	public void addControl(final Control control_) {
		if (control_ != null) {
			addControl(control_.toString(), control_);
		}
	}

	public void insertControlZOrderBottom(final Control control_) {
		insertControlZOrderBottom(null, control_);
	}

	public void insertControlZOrderBottom(final String key, final Control control_) {
		String keyToUse;
		if (key == null) {
			keyToUse = control_ != null ? control_.toString() : null;
		} else {
			keyToUse = key;
		}

		if (keyToUse != null) {
			synchronized (controlsLock) {
				if (containsControl(control_) == false) {
					control_.setParent(this);
					componentControls.add(0, control_);
					componentControlNames.add(keyToUse);
				}
			}
		}
	}

	public void addControls(final Collection<Control> controls_) {
		for (Control control : controls_) {
			addControl(control);
		}

	}

	public Collection<Control> getControls() {
		final List<Control> allControls = new ArrayList<Control>();
		synchronized (controlsLock) {
			for (Control control : componentControls) {
				allControls.add(control);
			}
		}

		return allControls;
	}

	public Collection<Control> getControlsRecursive() {
		final List<Control> allControls = new ArrayList<Control>();
		synchronized (controlsLock) {
			for (Control control : componentControls) {
				allControls.addAll(control.getControlsRecursive());
				allControls.add(control);
			}
		}

		return allControls;
	}

	public List<Control> getControlsReversed() {
		final List<Control> reversed = new ArrayList<Control>();

		synchronized (controlsLock) {
			// Need to go through the controls in reverse.
			for (Control control : getControls()) {
				reversed.add(0, control);
			}
		}

		return reversed;
	}

	public void removeControl(final Control control) {
		synchronized (controlsLock) {
			if (control != null) {
				final int index = componentControls.indexOf(control);
				if (index != -1) {
					componentControls.remove(index);
					componentControlNames.remove(index);
				}
			}
		}
	}

	public void processMouseWheel(final MouseWheelEvent wheelEvent) {

	}

	public void clear() {
		componentControls.clear();
		componentControlNames.clear();
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}

	public Control getControl(final String name) {
		final int index = componentControlNames.indexOf(name);
		if (index != -1) {
			return componentControls.get(index);
		}

		return null;
	}

	public int getRightCentered(final int width) {
		return (getDimensions().getAbsWidth() - width) / 2 + width;
	}

	public int getTopCentered(final int height) {
		return (getDimensions().getAbsHeight() - height) / 2;
	}

	public int getLeftCentered(final int width) {
		return (getDimensions().getAbsWidth() - width) / 2;
	}

	public static int getStdGap() {
		// A gap of roughly 1/20 of an inch.
		return (int) (environment.getWindowManager().getScreenDpi() * 0.05F);
	}

	public static int getHalfGap() {
		return getStdGap() / 2;
	}

	public static int getDblGap() {
		return getStdGap() * 2;
	}

	/**
	 * Returns the window that contains this control
	 * 
	 * @return
	 */
	public ControlContainer getWindow() {
		if (this instanceof Window) {
			return (ControlContainer) this;
		} else {
			if (getParent() == null) {
				return null;
			} else {
				return getParent().getWindow();
			}
		}
	}

	/**
	 * Brings the given control to the front of the z order
	 * 
	 * @param list
	 */
	public void bringToFront(final Control control) {
		synchronized (controlsLock) {
			if (componentControls.contains(control)) {
				final String key = getKey(control);
				removeControl(control);
				addControl(key, control);
			}
		}
	}

	public String getKey(final Control control) {
		return componentControlNames.get(componentControls.indexOf(control));
	}

	public void setTutorialItem(final TutorialItem tutorialItem_) {
		tutorialItem = tutorialItem_;
	}

	public TutorialItem getTutorialItem() {
		return tutorialItem;
	}

	public void addPolygon(final PolygonControl polygon) {
		polygons.add(polygon);
		addControl(polygon);
	}

	public float getxRatio() {
		return xRatio;
	}

	private void setxRatio(float xRatio) {
		this.xRatio = xRatio;
	}

	public float getyRatio() {
		return yRatio;
	}

	private void setyRatio(float yRatio) {
		this.yRatio = yRatio;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void clearImage() {
		setImage(null);
	}

	protected AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		if (alpha < 0) {
			WorkerLog.error("Alpha below zero!");
			return null;
		} else if (alpha > 1) {
			WorkerLog.error("Alpha above one!");
			alpha = 1;
		}
		return (AlphaComposite.getInstance(type, alpha));
	}

	/**
	 * This method and doSpecialClickActions() are for behaviours that ALL
	 * objects of the type should express. The command system is for behaviours
	 * that a particular object of the type should express.
	 */
	public boolean doSpecialMoveActions(int x, int y) {
		mouseOver = true;
		return true;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public WindowManager getWindowManager() {
		return environment.getWindowManager();
	}

	public int getScreenHeight() {
		return environment.getWindowManager().getScreenHeight();
	}

	public int getScreenWidth() {
		return environment.getWindowManager().getScreenWidth();
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void clearMouseOver() {
		this.mouseOver = false;
	}

	public Collection<Control> findAllParents() {
		return findAllParents(new ArrayList<Control>());
	}

	private Collection<Control> findAllParents(final Collection<Control> all) {
		all.add(this);

		if (getParent() != null) {
			return getParent().findAllParents(all);
		}

		return all;
	}

	public void rotateImage() {
		if (xRatio == 0F || yRatio == 0F) {
			return;
		}

		final AbsDims dimsToUse = determineImageDimsToUse();

		if (rotation == false) {
			at.translate(dimsToUse.left, dimsToUse.top);
			at.scale(xRatio, yRatio);
		}

		rotation = true;

		// Rotation doesn't take scaling into account.
		// Dividing by 2 rotates around center point.
		at.rotate(0.4, image.getWidth() / 2, image.getHeight() / 2);

		// image.setBufferedImage(ato.filter(image.getBufferedImage(), null));
	}

	public void stopRotation() {
		rotation = false;
		initAffineTransform();
	}

	private AbsDims determineImageDimsToUse() {
		if (image != null) {
			AbsDims drawDimensions = image.getDrawDimensions();
			if (drawDimensions != null) {
				drawDimensions = drawDimensions.makeCopy();
				drawDimensions.move(getScreenDims().left, getScreenDims().top);
				return drawDimensions;
			}

			return getScreenDims();
		} else {
			return null;
		}
	}

	public float getAspectRatio() {
		return (float) getDimensions().getAbsWidth() / getDimensions().getAbsHeight();
	}

	public CommandToken<?> createCloseToken() {
		return new CommandToken<Object>(ControlController::close);
	}

	public void move(int x, int y) {
		dimensions.move(x, y);

		// Force screen dims to recalculate
		screenDimensions = null;
	}

	public boolean isDropTarget() {
		return dropToken != null;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public CommandToken<DragDropBean> getDropToken() {
		return dropToken;
	}

	public void setDropToken(CommandToken<DragDropBean> dropToken) {
		this.dropToken = dropToken;
	}

	public void drop(Control dragging) {
		if (isDropTarget()) {
			DragDropBean bean = dropToken.getPayload();
			if (bean == null) {
				bean = new DragDropBean();
			}
			bean.setDragging(dragging);
			bean.setTarget(this);
			dropToken.setPayload(bean);
			dropToken.execute();
		} else {
			WorkerLog.error("This control does not accept dragged controls." + toString());
		}
	}

	public void setScreenDims(AbsDims dims) {
		screenDimensions = dims;
	}

	public void dragRender(Graphics graphics, boolean mousedown, final AbsDims dims, DragInfo dragInfo) {
		render(graphics, mousedown);
	}

	public void renderingTooltip() {

	}

	public void update() {

	}

	public boolean isDrawBorder() {
		return drawBorder;
	}

	public void setDrawBorder(boolean drawBorder) {
		this.drawBorder = drawBorder;
	}

	public Justification getJustification() {
		return justification;
	}

	public void setJustification(Justification justification) {
		this.justification = justification;
	}

	public FontInfo getStandardFont() {
		return environment.getLabelFontInfo();
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) {
		this.fontInfo = fontInfo;
		this.disabledFontInfo = null;
	}

	public void disable() {
		enabled = false;
	}

	public void enable() {
		enabled = true;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
