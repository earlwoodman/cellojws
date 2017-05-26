package com.rallycallsoftware.cellojws.general.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.DisplayManager;
import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Progress;
import com.rallycallsoftware.cellojws.general.core.LanguageManager.Language;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.general.work.Work;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class Environment extends Thread
		implements Progress, MouseListener, KeyListener, MouseWheelListener, MouseMotionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8041653828152763972L;

	private static final String buttonFontFace = "Arial Bold";

	private static final int buttonFontSize = 14;

	private static final int DPI_CUT_OFF = 150;

	private transient DisplayManager displayManager;

	private transient WindowManager windowManager;

	private int width;

	private int height;

	private String executionPath;

	private boolean running = false;

	private boolean readyToStart = false;

	private boolean debugMode;

	private AbsDims fullScreenDims;

	private AbsDims largePopupDims;

	private AbsDims smallPopupDims;

	// Some things get scheduled by a different thread (i.e. not an active mouse
	// press, etc.)
	// so do those things here, so they can be done synchronously
	//
	private List<Work> workItems = new ArrayList<Work>();
	private transient Object workLock = new Object();

	private boolean tutorialMode;

	private transient AudioManager audioManager;

	private int dpi;

	private ScreenFactoryDispatcher screenFactoryDispatcher;

	private static FontInfo watermarkFontInfo;

	private static FontInfo largeItalicFontInfo;

	private static FontInfo tooltipFontInfo;

	private static FontInfo calendarFontInfo;

	private static FontInfo buttonFontInfo;

	private static FontInfo listBoxFontInfo;

	private static FontInfo labelFontInfo;

	private static FontInfo errorFontInfo;

	private static FontInfo menuFontInfo;

	private static FontInfo windowTitleFontInfo;

	private static FontInfo tagFontInfo;

	private static String defaultFontFace = "Verdana";

	private static String windowTitleFontFace = "Verdana";

	private static int defaultFontSize = 18;

	public Environment(final ScreenFactoryDispatcher screenFactoryDispatcher, final String debugMode_) {
		debugMode = "DEBUG".equals(debugMode_);

		this.screenFactoryDispatcher = screenFactoryDispatcher;

		Control.setEnvironment(this);

		tutorialMode = false;

		executionPath = System.getProperty("user.dir");

		audioManager = new AudioManager();

		displayManager = new DisplayManager();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		height = (int) screenSize.getHeight();
		width = (int) screenSize.getWidth();

		fullScreenDims = new AbsDims(0, 0, width - 1, height - 1);

		setupPopupDims();

		final DisplayMode displayMode = new DisplayMode(width, height, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
		try {
			displayManager.setFullScreen(displayMode);
		} finally {

		}

		final Graphics graphics = new Graphics(displayManager.getGraphics());

		this.windowManager = new WindowManager(this.getErrorFontInfo(), this.getLabelFontInfo().getTextColor(), graphics, width,
				height, dpi);

		environment = this;

		readyToStart = true;
	}

	static {
		labelFontInfo = new FontInfo();
		labelFontInfo.setTextColor(Color.WHITE);
		labelFontInfo.setTextColorForHighlight(null);
		labelFontInfo.setDropShadow(new Color(0x777777));
		labelFontInfo.setDropShadowForHighlight(new Color(0x777777));
		labelFontInfo.setFontFace(defaultFontFace);
		labelFontInfo.setFontSize(defaultFontSize);
		labelFontInfo.setItalic(false);
		labelFontInfo.setUpperCase(false);

		listBoxFontInfo = new FontInfo();
		listBoxFontInfo.setTextColor(new Color(0x63BAE1));
		listBoxFontInfo.setTextColorForHighlight(Color.YELLOW);
		listBoxFontInfo.setDropShadow(new Color(0x135091));
		listBoxFontInfo.setDropShadowForHighlight(new Color(0x777777));
		listBoxFontInfo.setFontFace(defaultFontFace);
		listBoxFontInfo.setFontSize(22);
		listBoxFontInfo.setItalic(false);
		listBoxFontInfo.setUpperCase(false);

		buttonFontInfo = new FontInfo();
		buttonFontInfo.setTextColor(new Color(228, 199, 20));
		buttonFontInfo.setTextColorForHighlight(null);
		buttonFontInfo.setDropShadow(Color.BLACK);
		buttonFontInfo.setDropShadowForHighlight(null);
		buttonFontInfo.setFontFace(buttonFontFace);
		buttonFontInfo.setFontSize(buttonFontSize);
		buttonFontInfo.setItalic(false);
		buttonFontInfo.setUpperCase(true);

		calendarFontInfo = new FontInfo();
		calendarFontInfo.setTextColor(Color.BLACK);
		calendarFontInfo.setTextColorForHighlight(null);
		calendarFontInfo.setDropShadow(Color.DARK_GRAY.brighter());
		calendarFontInfo.setDropShadowForHighlight(new Color(0x777777));
		calendarFontInfo.setFontFace(defaultFontFace);
		calendarFontInfo.setFontSize(defaultFontSize);
		calendarFontInfo.setItalic(false);
		calendarFontInfo.setUpperCase(false);

		tooltipFontInfo = new FontInfo();
		tooltipFontInfo.setTextColor(Color.WHITE);
		tooltipFontInfo.setTextColorForHighlight(null);
		tooltipFontInfo.setDropShadow(new Color(0x6a6a6a));
		tooltipFontInfo.setDropShadowForHighlight(null);
		tooltipFontInfo.setFontFace(defaultFontFace);
		tooltipFontInfo.setFontSize(defaultFontSize);
		tooltipFontInfo.setItalic(true);
		tooltipFontInfo.setUpperCase(false);

		largeItalicFontInfo = new FontInfo();
		largeItalicFontInfo.setTextColor(Color.WHITE);
		largeItalicFontInfo.setTextColorForHighlight(null);
		largeItalicFontInfo.setDropShadow(new Color(0x6a6a6a));
		largeItalicFontInfo.setDropShadowForHighlight(null);
		largeItalicFontInfo.setFontFace("Arial");
		largeItalicFontInfo.setFontSize(36);
		largeItalicFontInfo.setItalic(true);
		largeItalicFontInfo.setUpperCase(false);

		watermarkFontInfo = new FontInfo();
		watermarkFontInfo.setTextColor(new Color(0xa0a0a0));
		watermarkFontInfo.setTextColorForHighlight(null);
		watermarkFontInfo.setDropShadow(new Color(0x9f9f9f));
		watermarkFontInfo.setDropShadowForHighlight(null);
		watermarkFontInfo.setFontFace("Arial");
		watermarkFontInfo.setFontSize(28);
		watermarkFontInfo.setItalic(true);
		watermarkFontInfo.setUpperCase(false);

		errorFontInfo = new FontInfo();
		errorFontInfo.setTextColor(Color.RED.brighter().brighter());
		errorFontInfo.setDropShadow(Color.RED);
		errorFontInfo.setFontFace("Arial");
		errorFontInfo.setFontSize(22);
		errorFontInfo.setItalic(false);
		errorFontInfo.setUpperCase(false);

		menuFontInfo = new FontInfo();
		menuFontInfo.setTextColor(Color.WHITE.darker());
		menuFontInfo.setDropShadow(Color.WHITE.darker().darker().darker());
		menuFontInfo.setFontFace(defaultFontFace);
		menuFontInfo.setFontSize(20);
		menuFontInfo.setItalic(false);
		menuFontInfo.setUpperCase(false);

		windowTitleFontInfo = new FontInfo();
		windowTitleFontInfo.setTextColor(getLightBlue());
		windowTitleFontInfo.setDropShadow(Color.darkGray);
		windowTitleFontInfo.setFontFace(windowTitleFontFace);
		windowTitleFontInfo.setFontSize(22);
		windowTitleFontInfo.setItalic(false);
		windowTitleFontInfo.setUpperCase(false);

		tagFontInfo = new FontInfo();
		tagFontInfo.setTextColor(new Color(198, 196, 254));
		tagFontInfo.setTextColorForHighlight(null);
		tagFontInfo.setDropShadow(Color.BLACK);
		tagFontInfo.setDropShadowForHighlight(null);
		tagFontInfo.setFontFace(Environment.getDefaultFontFace());
		tagFontInfo.setFontSize(Environment.getDefaultFontSize());
		tagFontInfo.setItalic(false);
		tagFontInfo.setUpperCase(true);
	}

	private void setupPopupDims() {

		int largeWidth;
		int largeHeight;
		int smallWidth;
		int smallHeight;

		if (isHighDPI()) {
			largeWidth = 2440;
			largeHeight = 1440;
			smallWidth = 1640;
			smallHeight = 940;
		} else {
			largeWidth = 1220;
			largeHeight = 720;
			smallWidth = 820;
			smallHeight = 470;
		}

		largePopupDims = createPopupDimensions(largeWidth, largeHeight);
		smallPopupDims = createPopupDimensions(smallWidth, smallHeight);

	}

	private AbsDims createPopupDimensions(final int popupWidth, final int popupHeight) {
		return new AbsDims((width - popupWidth) / 2, (height - popupHeight) / 2, (width - popupWidth) / 2 + popupWidth,
				(height - popupHeight) / 2 + popupHeight);
	}

	private static Environment environment;

	public static Environment getEnvironment() {
		return environment;
	}

	public void run() {
		do {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}
		} while (!readyToStart);

		final Window window = displayManager.getFullScreenWindow();
		window.addMouseListener(this);
		window.addMouseWheelListener(this);
		window.addMouseMotionListener(this);
		window.addKeyListener(this);

		screenFactoryDispatcher.startOver();

		synchronized (this) {
			running = true;
		}

		while (true) {
			runLoop();
			refresh();
			doWork();

			synchronized (this) {
				if (!running) {
					System.exit(0);
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}

		}

	}

	public void runLoop() {
		final Graphics2D g = displayManager.getGraphics();

		// draw background
		g.setColor(Color.BLACK);

		g.fillRect(0, 0, displayManager.getWidth(), displayManager.getHeight());

		// draw messages
		g.setColor(Color.WHITE);

		windowManager.setGraphics2D(g);
		windowManager.render();

		g.dispose();

		displayManager.update();
	}

	private void doWork() {
		synchronized (workLock) {
			if (workItems.size() > 0) {
				workItems.get(0).doWork();
				workItems.remove(0);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		windowManager.mousePressed();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (!windowManager.isDragging()) {
			final CommandToken<?> token = windowManager.processMouseClick(arg0.getX(), arg0.getY());
			if (token != null) {
				screenFactoryDispatcher.dispatch(token);
			}
		}

		windowManager.mouseReleased(arg0.getX(), arg0.getY());
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyChar() == '\n') {
			final CommandToken<?> token = windowManager.enterKeyPressed();
			if (token != null) {
				screenFactoryDispatcher.dispatch(token);
			}
		} else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			final CommandToken<?> token = windowManager.escapeKeyPressed();
			if (token != null) {
				screenFactoryDispatcher.dispatch(token);
			}
		} else {
			final CommandToken<?> token = windowManager.processKeyPress(arg0);
			if (token != null) {
				screenFactoryDispatcher.dispatch(token);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public void refresh() {
		screenFactoryDispatcher.refresh();
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public String getExecutionPath() {
		return executionPath;
	}

	public synchronized boolean isReadyToStart() {
		return readyToStart;
	}

	public synchronized void setReadyToStart(boolean readyToStart) {
		this.readyToStart = readyToStart;
	}

	public FontInfo getListBoxFontInfo() {
		final FontInfo fieldNameFont = listBoxFontInfo.makeCopy();
		fieldNameFont.setFontFace("Arial");
		fieldNameFont.setFontSize(22);
		final Color paleBlue = getPaleBlue();
		fieldNameFont.setTextColor(paleBlue);
		fieldNameFont.setDropShadow(paleBlue.darker().darker().darker());

		return fieldNameFont;
	}

	public FontInfo getButtonFontInfo() {
		return buttonFontInfo;
	}

	public FontInfo getCalendarFontInfo() {
		return calendarFontInfo;
	}

	public FontInfo getLabelFontInfo() {
		return labelFontInfo;
	}

	public FontInfo getToolTipFontInfo() {
		return tooltipFontInfo;
	}

	public FontInfo getLargeItalicFont() {
		return largeItalicFontInfo;
	}

	public FontInfo getWatermarkFontInfo() {
		return watermarkFontInfo;
	}

	public FontInfo getRadioButtonFontInfo() {
		return getListBoxFontInfo().makeCopy();
	}

	public void hourglass() {
		displayManager.hourglass();
	}

	public void pointer() {
		displayManager.pointer();
	}

	public void stopRunning() {
		synchronized (this) {
			running = false;
		}

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		windowManager.processMouseWheel(arg0);
	}

	public void exit() {
		hourglass();

		screenFactoryDispatcher.startOver();

		pointer();
	}

	public void addWork(final Work work_) {
		// Something needs to be done, so schedule it.
		synchronized (workLock) {
			workItems.add(work_);
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		windowManager.processMouseDrag(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		windowManager.processMouseMove(arg0);
	}

	public boolean isTutorialMode() {
		return tutorialMode;
	}

	public void setTutorialMode(boolean tutorialMode) {
		this.tutorialMode = tutorialMode;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public void playMusic() {
		audioManager.playAudio(executionPath + "\\Audio\\soft.wav", true);
	}

	public void stopMusic() {
		audioManager.stop();
	}

	@Override
	public void setProgress(final float value) {
		windowManager.turnOnProgress();
		windowManager.setProgress(value);
	}

	@Override
	public void turnOffProgress() {
		windowManager.turnOffProgress();
	}

	@Override
	public void setProgressMessage(final String message) {
		windowManager.setProgressMessage(message);
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}

	public AbsDims getStartScreenDims() {
		return getFullScreenDims();
	}

	public AbsDims getFullScreenDims() {
		return fullScreenDims;
	}

	public AbsDims getLargePopupDims() {
		return largePopupDims;
	}

	public AbsDims getSmallPopupDims() {
		return smallPopupDims;
	}

	public Image getSmallPopupImage() {
		if (isHighDPI()) {
			return ImageFactory.getSmallPopupHighDpi();
		} else {
			return ImageFactory.getSmallPopupLowDpi();
		}

	}

	public Image getLargePopupImage() {
		if (isHighDPI()) {
			return ImageFactory.getLargePopupHighDpi();
		} else {
			return ImageFactory.getLargePopupLowDpi();
		}
	}

	public String getText(final LangKey key) {
		return LanguageManager.get(Language.English, key);
	}

	public float getAspectRatio() {
		return (float) fullScreenDims.getAbsWidth() / fullScreenDims.getAbsHeight();
	}

	public int getDpi() {
		return dpi;
	}

	public void establishWorkLock() {
		workLock = new Object();
	}

	public ScreenFactoryDispatcher getScreenFactoryDispatcher() {
		return screenFactoryDispatcher;
	}

	public void setScreenFactoryDispatcher(ScreenFactoryDispatcher screenFactoryDispatcher) {
		this.screenFactoryDispatcher = screenFactoryDispatcher;
	}

	public int getPixelsPerInch(float inches) {
		return (int) (inches * dpi);
	}

	public boolean isHighDPI() {
		return dpi > DPI_CUT_OFF;
	}

	public int getPopupImageBorder() {
		if (isHighDPI()) {
			return 20;
		} else {
			return 10;
		}
	}

	public FontInfo getErrorFontInfo() {
		return errorFontInfo;
	}

	public FontInfo getMenuFontInfo() {
		return menuFontInfo;
	}

	public static String getDefaultFontFace() {
		return defaultFontFace;
	}

	public static int getDefaultFontSize() {
		return defaultFontSize;
	}

	public int halfInch() {
		return Math.round(0.5F * environment.getDpi());
	}

	public int quarterInch() {
		return Math.round(0.25F * environment.getDpi());
	}

	public int inch() {
		return environment.getDpi();
	}

	public static String getArialItalic() {
		return "Arial Italic";
	}

	public static int getStandardTinyFontSize() {
		return 11;
	}

	public int tenthInch() {
		return Math.round(0.1F * environment.getDpi());
	}

	public Image getDropShadowImage() {
		if (isHighDPI()) {
			return ImageFactory.getDropShadowHighDpi();
		}

		return ImageFactory.getDropShadowLowDpi();
	}

	public FontInfo getWindowTitleFont() {
		return windowTitleFontInfo;
	}

	public FontInfo getTagFont() {
		return tagFontInfo;
	}

	public static Color getDarkBlue() {
		return new Color(42, 54, 71);
	}

	public static Color getLightBlue() {
		return new Color(71, 163, 241);
	}

	public static Color getPaleBlue() {
		return new Color(150, 170, 221);
	}

	public DisplayManager getDisplayManager() {
		return displayManager;
	}

	private static final Color offWhite = new Color(212, 204, 219);

	public static Color getOffWhite() {
		return offWhite;
	}

}
