package com.cellojws.general.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import com.cellojws.adapter.DisplayManager;
import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.Progress;
import com.cellojws.general.Settings;
import com.cellojws.general.core.LanguageManager.Language;
import com.cellojws.general.image.Image;
import com.cellojws.general.image.ImageFactory;
import com.cellojws.general.work.Work;
import com.cellojws.logging.WorkerLog;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.WindowManager;


public abstract class Environment extends Thread implements Progress, MouseListener, KeyListener, MouseWheelListener, MouseMotionListener
{		

	private static final String buttonFontFace = "Century Gothic Bold";

	private static final int buttonFontSize = 14;

	private static transient DisplayManager displayManager;
    
    private transient WindowManager windowManager;
        
    private static int width;
    
    private static int height;

	private static String executionPath;

    private boolean running = false;
    
    private boolean fatalError = false;
    
    private boolean readyToStart = false;
    
    private static boolean debugMode;

    private static AbsDims fullScreenDims;
    
    private static AbsDims largePopupDims;
    
   	private static AbsDims smallPopupDims;

    // Some things get scheduled by a different thread (i.e. not an active mouse press, etc.)
    // so do those things here, so they can be done synchronously
    //
    private List<Work> workItems = new ArrayList<Work>();
    private transient Object workLock = new Object();    
	
	private boolean tutorialMode;
	
	private transient AudioManager audioManager;

	private static int dpi;
	
	private ScreenFactoryDispatcher screenFactoryDispatcher;

	private static Settings settings;
 
	private static FontInfo watermarkFontInfo;

	private static FontInfo largeFontInfo;
	
	private static FontInfo superLargeFontInfo;

	private static FontInfo tooltipFontInfo;

	private static FontInfo calendarFontInfo;

	private static FontInfo buttonFontInfo;

	private static FontInfo listBoxFontInfo;

	private static FontInfo labelFontInfo;

	private static FontInfo errorFontInfo;
	
	private static FontInfo menuFontInfo;
		
	private static FontInfo tagFontInfo;
	
	private static FontInfo windowTitleFont;

	private static FontInfo listBoxHeaderFontInfo;
	
	private static FontInfo fieldNameFontInfo;
	
	private static FontInfo fieldValueFontInfo;

	private static final String defaultFontFace = "Verdana";

	private static final int defaultFontSize = 18;

	private static final String secondaryFont = "Arial";

	private static final Color listBoxTextColour = new Color(0x63E1BA);

	private static Color windowingSystemLight = new Color(42, 65, 54);
	
	private static Color windowingSystemDark = new Color(42, 71, 54);  
	
	private static Color lightGreen = new Color(150, 210, 130);

	private long lastTime;

	public Environment(final ScreenFactoryDispatcher screenFactoryDispatcher, final boolean debugMode, final Settings settings)
    {    	
		Locale.setDefault(Locale.CANADA);
		
    	Environment.debugMode = debugMode;
    
    	this.screenFactoryDispatcher = screenFactoryDispatcher;
    	
    	Control.setEnvironment(this);
    	
    	tutorialMode = false;
    	    	    	
        executionPath = System.getProperty("user.dir");
            	
    	audioManager = new AudioManager();    	

    	Environment.settings = settings;
    	
    	setupDisplay(settings);
        
        environment = this;
        
        readyToStart = true;
    }

	private void setupDisplay(final Settings settings) 
	{
		final boolean fullScreen = !settings.getWindowed();
        displayManager = new DisplayManager(fullScreen);
        final List<DisplayMode> displayModes = Arrays.asList(displayManager.getCompatibleDisplayModes());
        final List<DisplayMode> okModes = 
        		displayModes.stream()
		        			.filter(x -> x.getHeight() >= 900)
		        			.filter(x -> x.getWidth() >= 1600)
		        			.filter(x -> x.getBitDepth() >= 32)
		        			.collect(Collectors.toList());
        settings.setDisplayModes(okModes);
        if( settings.getDisplayMode() == null || settings.getDisplayMode() == "" || settings.getDisplayMode().equals("0") )
        {
	        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        height = (int)screenSize.getHeight();
	        width = (int)screenSize.getWidth();
        }
        else
        {
        	// Compensate for fact that there's no "Default Monitor Resolution" in okModes
        	final int okModeIndex = Integer.parseInt(settings.getDisplayMode()) - 1;
        	final DisplayMode displayMode = okModes.get(okModeIndex);
        	height = displayMode.getHeight();
        	width = displayMode.getWidth();
        }
        dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        fullScreenDims = new AbsDims(0, 0, width - 1, height - 1);
        
        setupPopupDims();
    
        java.awt.Graphics2D graphics2D;
        if( fullScreen ) 
        {
	        final DisplayMode displayMode = new DisplayMode(width, height, 32, DisplayMode.REFRESH_RATE_UNKNOWN);        
	        try
	        {
	            displayManager.setFullScreen(displayMode);
	        }
	        finally
	        {
	            
	        }
	        graphics2D = displayManager.getGraphics();
        }
        else
        {
        	JFrame jframe = displayManager.getJFrame();
	        jframe.setSize(width, height);
	        jframe.setVisible(true);
	        displayManager.createBufferStrategy(jframe);
	        graphics2D = (Graphics2D) jframe.getBufferStrategy().getDrawGraphics();
        }
        // Let's go with a thin stroke value of 1/35 of an inch. Arbitrary
        final Graphics graphics = new Graphics(graphics2D, Math.round(dpi / 35));
        this.windowManager = new WindowManager(getErrorFontInfo(), getLabelFontInfo(), graphics, width, height, dpi);
	}
	
	
    private Graphics2D getGraphics()
	{
    	return (Graphics2D) displayManager.getGraphics();
	}

	
	static
	{
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
		listBoxFontInfo.setTextColorForHighlight(Color.white);
		listBoxFontInfo.setDropShadowForHighlight(new Color(0x777777));
		listBoxFontInfo.setFontFace("Arial");
		listBoxFontInfo.setFontSize(22);
		listBoxFontInfo.setItalic(false);
		listBoxFontInfo.setUpperCase(false);

		listBoxHeaderFontInfo = new FontInfo();
		listBoxHeaderFontInfo.setTextColorForHighlight(Color.white);
		listBoxHeaderFontInfo.setDropShadowForHighlight(new Color(0x777777));
		listBoxHeaderFontInfo.setFontFace("Arial");
		listBoxHeaderFontInfo.setFontSize(22);
		listBoxHeaderFontInfo.setItalic(false);
		listBoxHeaderFontInfo.setUpperCase(false);

		buttonFontInfo = new FontInfo();
		buttonFontInfo.setTextColor(Color.white);
		buttonFontInfo.setTextColorForHighlight(null);
		buttonFontInfo.setDropShadow(Color.black);
		buttonFontInfo.setDropShadowForHighlight(null);
		buttonFontInfo.setFontFace(buttonFontFace);
		buttonFontInfo.setFontSize(buttonFontSize);
		buttonFontInfo.setItalic(false);
		buttonFontInfo.setUpperCase(false);

		windowTitleFont = new FontInfo();
		windowTitleFont.setTextColor(Color.white);
		windowTitleFont.setTextColorForHighlight(null);
		windowTitleFont.setDropShadow(Color.black);
		windowTitleFont.setDropShadowForHighlight(null);
		windowTitleFont.setFontFace(buttonFontFace);
		windowTitleFont.setFontSize(18);
		windowTitleFont.setItalic(false);
		windowTitleFont.setUpperCase(false);
		windowTitleFont.setGaussianTextColour(lightGreen);

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
		
		largeFontInfo = new FontInfo();
		largeFontInfo.setTextColor(Color.WHITE);
		largeFontInfo.setTextColorForHighlight(null);
		largeFontInfo.setDropShadow(new Color(0x6a6a6a));
		largeFontInfo.setDropShadowForHighlight(null);
		largeFontInfo.setFontFace(secondaryFont);
		largeFontInfo.setFontSize(36);
		largeFontInfo.setItalic(false);
		largeFontInfo.setUpperCase(false);
		
		superLargeFontInfo = new FontInfo();
		superLargeFontInfo.setTextColor(getWindowingSystemDark());
		superLargeFontInfo.setTextColorForHighlight(null);
		superLargeFontInfo.setDropShadow(new Color(0x6a6a6a));
		superLargeFontInfo.setDropShadowForHighlight(null);
		superLargeFontInfo.setFontFace(secondaryFont);
		superLargeFontInfo.setFontSize(68);
		superLargeFontInfo.setItalic(false);
		superLargeFontInfo.setUpperCase(false);		

		watermarkFontInfo = new FontInfo();
		watermarkFontInfo.setTextColor(new Color(0xa0a0a0));
		watermarkFontInfo.setTextColorForHighlight(null);
		watermarkFontInfo.setDropShadow(new Color(0x9f9f9f));
		watermarkFontInfo.setDropShadowForHighlight(null);
		watermarkFontInfo.setFontFace(secondaryFont);
		watermarkFontInfo.setFontSize(28);
		watermarkFontInfo.setItalic(true);
		watermarkFontInfo.setUpperCase(false);
		
		errorFontInfo = new FontInfo();
		errorFontInfo.setTextColor(new Color(200, 0, 0));
		errorFontInfo.setDropShadow(Color.RED.darker());
		errorFontInfo.setFontFace("Verdana Italic");
		errorFontInfo.setFontSize(22);
		errorFontInfo.setItalic(true);
		errorFontInfo.setUpperCase(false);

		menuFontInfo = new FontInfo();
		menuFontInfo.setTextColor(listBoxTextColour);
		menuFontInfo.setDropShadow(listBoxTextColour.darker());
		menuFontInfo.setFontFace(defaultFontFace);
		menuFontInfo.setFontSize(18);
		menuFontInfo.setItalic(false);
		menuFontInfo.setUpperCase(false);
				
		tagFontInfo = new FontInfo();
		tagFontInfo.setTextColor(new Color(198, 196, 254));
		tagFontInfo.setTextColorForHighlight(null);
		tagFontInfo.setDropShadow(Color.BLACK);
		tagFontInfo.setDropShadowForHighlight(null);
		tagFontInfo.setFontFace(Environment.getDefaultFontFace());
		tagFontInfo.setFontSize(Environment.getDefaultFontSize());
		tagFontInfo.setItalic(false);
		tagFontInfo.setUpperCase(false);

		fieldNameFontInfo = buttonFontInfo.makeCopy();
		fieldNameFontInfo.setFontSize(fieldNameFontInfo.getFontSize() + 2);
		fieldNameFontInfo.setTextColor(Environment.listBoxTextColour);

		fieldValueFontInfo = buttonFontInfo.makeCopy();

	}
	
	private void setupPopupDims()
 	{
				
		int largeWidth;
		int largeHeight;
		int smallWidth;
		int smallHeight;
		
		largeWidth = Math.round(1220 * getScaling());
		largeHeight = Math.round(720 * getScaling());
		smallWidth = Math.round(820 * getScaling());
		smallHeight = Math.round(470 * getScaling());
 		
        largePopupDims = createPopupDimensions(largeWidth, largeHeight);
        smallPopupDims = createPopupDimensions(smallWidth, smallHeight);
	
	}

	public static float getScaling() 
	{
		if( width <= 1920 )
		{
			return 1.0F;
		}
		else if( width <= 2560 )
		{
			return 1.25F;
		}
		else if( width <= 3840 )
		{
			return 1.5F;
		}
		
		return 1.75F;
	}

	private AbsDims createPopupDimensions(final int popupWidth, final int popupHeight) 
	{
		return new AbsDims((width - popupWidth) / 2, (height - popupHeight) / 2, (width - popupWidth) / 2 + popupWidth, (height - popupHeight) / 2 + popupHeight);
	}

	private static Environment environment;
    
    public static Environment getEnvironment()
    {
    	return environment;
    }

	public void run()
    {
    	do
    	{
    		try
    		{
    			Thread.sleep(20);
    		}
    		catch(InterruptedException e)
    		{
    			
    		}
    	}
    	while( !readyToStart );    		        
                
        final JFrame jframe = displayManager.getJFrame();
        
    	jframe.addMouseListener(this);
        jframe.addMouseWheelListener(this);
        jframe.addMouseMotionListener(this);
        jframe.addKeyListener(this);
        
        screenFactoryDispatcher.startOver();
        
        synchronized( this )
        {
            running = true;
        }
            
        final int desiredFPS = 12;
        final int idealSleep = 1000 / desiredFPS;
        while ( true )
        {
        	long start = System.currentTimeMillis();
            runLoop();
    		doWork();
    		
    		synchronized( this )
    		{
    		    if( !running )
    		    {
    		        System.exit(0);
    		    }
    		    if( fatalError )
    		    {
    		    	break;
    		    }
    		}
    		
    		/*int delay = (int) (System.currentTimeMillis() - start);
    		int actualSleep = idealSleep - delay;
    		if( actualSleep > 0 )
    		{
    			try    		
	    		{
	    		    Thread.sleep(actualSleep);
	    		}
	    		catch(InterruptedException e)
	    		{
	       
	    		}
    		}*/
        }
        
    }

	public void runLoop() 
	{
		final Graphics2D g = getGraphics();
		
		// draw background
		g.setColor(Color.BLACK);
		
		g.fillRect(0, 0, displayManager.getWidth(), displayManager.getHeight());
   
		// draw messages
		g.setColor(Color.WHITE);
		
		windowManager.setGraphics2D(g);
		windowManager.render();
				
		long elapsed = System.currentTimeMillis() - lastTime;
		
		lastTime = System.currentTimeMillis();
				
		g.drawString(Float.toString(Math.round(1000F / elapsed)), 0, 100);
		g.dispose();
		
		displayManager.update();
	}

	private void doWork() 
    {	
    	synchronized(workLock)
    	{
    		if( workItems.size() > 0 )
    		{        		
    			workItems.get(0).doWork();
    			workItems.remove(0);
    		}
    	}
	}

	@Override
    public void mouseClicked(MouseEvent arg0)
    {

    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
         
    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {

    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
    	windowManager.mousePressed();
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
    	try
    	{
	    	if( !windowManager.isDragging() )
	    	{
		        final CommandToken<?> token = windowManager.processMouseClick(arg0.getX(), arg0.getY());
		        if( token != null )
		        {
		        	screenFactoryDispatcher.dispatch(token);
		        }
	    	}
	    	
	    	windowManager.mouseReleased(arg0.getX(), arg0.getY());
    	}
    	catch(Exception e)
    	{
    		showErrorAndExit(e);
    	}
    }    
    
    @Override
    public void keyPressed(KeyEvent arg0)
    {
    	try
    	{
	    	if( arg0.getKeyChar() == '\n' )
	    	{
	    		final CommandToken<?> token = windowManager.enterKeyPressed();
	    		if( token != null )
	    		{
	    			screenFactoryDispatcher.dispatch(token);
	    		}
	    	}
	    	else if( arg0.getKeyCode() == KeyEvent.VK_ESCAPE )
	    	{
	    		final CommandToken<?> token = windowManager.escapeKeyPressed();
	    		if( token != null )
	    		{
	    			screenFactoryDispatcher.dispatch(token);
	    		}
	    	}
	    	else
	    	{
	    		final CommandToken<?> token = windowManager.processKeyPress(arg0);
	    		if( token != null )
	    		{
	    			screenFactoryDispatcher.dispatch(token);
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		showErrorAndExit(e);
    	}
    }

    /**
     * Something went really wrong and we can't handle it.
     * 
     * @param e
     */
    private void showErrorAndExit(final Exception e) 
    {
		WorkerLog.error("FATAL ERROR");
		for( StackTraceElement ste : e.getStackTrace() )
		{
			WorkerLog.error(ste.toString());
		}
		WorkerLog.error(e.getMessage());
		synchronized( this )
		{
			fatalError = true;
		}
		
		displayManager.restoreScreen();		
		SwingAlert.infoBox("Executive Hockey has encountered a fatal error and cannot continue: " + e.toString());
		System.exit(1);	
	}

	@Override
    public void keyReleased(KeyEvent arg0)
    {
        
    }

    @Override
    public void keyTyped(KeyEvent arg0)
    {
        
    }
    
    public static boolean isDebugMode()
    {
        return debugMode;
    }
    
	public static String getExecutionPath() 
	{	
		return executionPath;
	}

	public synchronized boolean isReadyToStart() 
    {
		return readyToStart;
	}

	public synchronized void setReadyToStart(boolean readyToStart)
	{
		this.readyToStart = readyToStart;
	}
	
	public static FontInfo getListBoxFontInfo() 
	{
		final Color listBoxColour = Environment.getEnvironment().getMouseoverColour();
		
		listBoxFontInfo.setTextColor(listBoxColour);
		listBoxFontInfo.setDropShadow(listBoxColour.darker().darker().darker());

		return listBoxFontInfo;
	}
	
	public static FontInfo getButtonFontInfo() 
	{
		return buttonFontInfo;
	}

	public static FontInfo getCalendarFontInfo() 
	{
		return calendarFontInfo;
	}

	public static FontInfo getLabelFontInfo() 
	{
		return buttonFontInfo;
	}

	public static FontInfo getToolTipFontInfo() 
	{
		return tooltipFontInfo;	
	}

	public static FontInfo getLargeFont() 
	{
		return largeFontInfo;
	}

	public static FontInfo getWatermarkFontInfo() 
	{
		return watermarkFontInfo;
	}
	
	public static FontInfo getListBoxHeaderFontInfo() 
	{
		final Color listBoxColour = Environment.getEnvironment().getMouseoverColour();
		
		listBoxHeaderFontInfo.setTextColor(listBoxColour.darker().darker());
		listBoxHeaderFontInfo.setDropShadow(listBoxColour.darker().darker().darker().darker());
		
		return listBoxHeaderFontInfo;
	}
	
	public static FontInfo getRadioButtonFontInfo()
	{
		return getListBoxFontInfo().makeCopy();
	}

	public void stopRunning()
	{
		synchronized( this )
        {
            running = false;
        }
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) 
	{
		windowManager.processMouseWheel(arg0);
	}
    
	public void exit()
	{
	    screenFactoryDispatcher.startOver();	    
	}
	    
    public void addWork(final Work work_)
    {
    	// Something needs to be done, so schedule it.
    	synchronized(workLock)
    	{
    		workItems.add(work_);
    	}
    }

	@Override
	public void mouseDragged(MouseEvent arg0) 
	{	
		windowManager.processMouseDrag(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		windowManager.processMouseMove(arg0);
	}		

    public boolean isTutorialMode() 
    {
		return tutorialMode;
	}

	public void setTutorialMode(boolean tutorialMode) 
	{
		this.tutorialMode = tutorialMode;
	}

	public AudioManager getAudioManager()
	{
		return audioManager;
	}

	public void playMusic()
	{
		audioManager.playAudio(executionPath + "\\Audio\\soft.wav", true);	
	}

	public void stopMusic()
	{	
		audioManager.stop();
	}

	@Override
	public void setProgress(final float value)
	{
		windowManager.turnOnProgress();
		windowManager.setProgress(value);
	}
	
	@Override
	public void turnOffProgress()
	{
		windowManager.turnOffProgress();
	}

	@Override
	public void setProgressMessage(final String message) 
	{
		windowManager.setProgressMessage(message);		
	}

	public WindowManager getWindowManager()
	{
		return windowManager;
	}

	public AbsDims getStartScreenDims()
	{
		return getFullScreenDims(); 
	}

	public static AbsDims getFullScreenDims()
	{
		return fullScreenDims;
	}

	public static AbsDims getLargePopupDims()
	{
		return largePopupDims;
	}

	public static AbsDims getSmallPopupDims()
	{
		return smallPopupDims;
	}

	public static Image getSmallPopupImage()
	{
		return ImageFactory.getSmallPopup();	
	}

	public static Image getLargePopupImage()
	{
		return ImageFactory.getLargePopup();	
	}

	public String getText(final LangKey key)
	{
		return LanguageManager.get(Language.English, key);
	}

	public float getAspectRatio() 
	{
		return (float)fullScreenDims.getAbsWidth() / fullScreenDims.getAbsHeight();
	}
	
	public void establishWorkLock()
	{
		workLock = new Object();
	}

    public ScreenFactoryDispatcher getScreenFactoryDispatcher()
	{
		return screenFactoryDispatcher;
	}

	public void setScreenFactoryDispatcher(
			ScreenFactoryDispatcher screenFactoryDispatcher)
	{
		this.screenFactoryDispatcher = screenFactoryDispatcher;
	}

	public static int getPopupImageBorder() 
	{
		return Math.round(10 * getScaling());
	}

	public static FontInfo getErrorFontInfo() 
	{
		return errorFontInfo;
	}

	public static FontInfo getMenuFontInfo() 
	{
		return menuFontInfo;
	}

	public static String getDefaultFontFace() 
	{
		return defaultFontFace;
	}

	public static int getDefaultFontSize() 
	{
		return defaultFontSize;
	}

	public static int mediumGap()
	{
		return Math.round(0.5F * bigGap());
	}
	
	public static int smallGap()
	{
		return Math.round(0.25F * bigGap());
	}
	
	public static int tinyGap()
	{
		return Math.round(0.1F * bigGap());
	}

	public static int verySmallGap()
	{
		return bigGap() / 32;
	}

	public static int veryVerySmallGap()
	{
		return bigGap() / 48;
	}

	public static int bigGap()
	{
		return Math.round(100 * getScaling());
	}

	public static String getArialItalic()
	{
		return "Arial Italic";
	}

	public static int getStandardTinyFontSize()
	{
		return 11;
	}

	public static Image getDropShadowImage()
	{
		return ImageFactory.getDropShadow();	
	}

	public static FontInfo getWindowTitleFont()
	{
		return windowTitleFont;
	}

	public static FontInfo getTagFont()
	{
		return tagFontInfo;
	}

	public abstract Color getNormalColour();
	
	public abstract Color getClickingColour();
	
	public abstract Color getMouseoverColour();
	
	public DisplayManager getDisplayManager()
	{
		return displayManager;
	}

	private static final Color offWhite = new Color(212, 204, 219);

	public static Color getOffWhite()
	{
		return offWhite;
	}

	public void pointer() 
	{
		getWindowManager().pointer();
	}

	public void hourglass() 
	{
		getWindowManager().hourglass();
	}

	public static Color getWindowingSystemLight() 
	{
		return windowingSystemLight;
	}

	public static Color getWindowingSystemDark() 
	{
		return windowingSystemDark;
	}

	public static String getMainFontFace()
	{
		return buttonFontFace;
	}

	public static Settings getSettings() 
	{
		return settings;
	}

	public static JFrame getFrame()
	{
		return displayManager.getJFrame();
	}

	public static String getScalingString() 
	{
		if( getScaling() == 1.0F )
		{
			return "10";
		}
		else if( getScaling() == 1.25F )
		{
			return "125";
		}
		else if( getScaling() == 1.5F )
		{
			return "15";
		}
		else if( getScaling() == 1.75F )
		{
			return "175";
		}
		
		return "10";
	}
    
    public static int getAppScreenWidth() 
    {
		return width;
	}

	public static int getAppScreenHeight() 
	{
		return height;
	}

	public static FontInfo getSuperLargeFont() 
	{
		return superLargeFontInfo;
	}

	public static FontInfo getFieldNameFont()
	{
		return fieldNameFontInfo;
	}
	
	public static FontInfo getFieldValueFont()
	{
		return fieldValueFontInfo;
	}

}
