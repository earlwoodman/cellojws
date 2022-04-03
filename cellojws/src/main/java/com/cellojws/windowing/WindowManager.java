/*
 * Created on 2010-07-11
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.windowing;

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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.controls.ControlContainer;
import com.cellojws.controls.Label;
import com.cellojws.controls.PopupMenu;
import com.cellojws.controls.ProgressBar;
import com.cellojws.controls.TransientControl;
import com.cellojws.controls.listbox.ListBox;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.dimensions.PctDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.Justification;
import com.cellojws.general.SerializableLock;
import com.cellojws.general.core.Environment;
import com.cellojws.logging.WorkerLog;
import com.cellojws.special.Tooltip;
import com.cellojws.stock.Screen;
import com.cellojws.token.CommandToken;


public class WindowManager
{
    

	private Set<WindowListener> listeners = new HashSet<WindowListener>();
	
    private static final long mouseSteadyDelay = 500;

	private static final long ACHIEVEMENT_TIMEOUT = 5000;

	private final SerializableLock windowLock = new SerializableLock();
    
    private final List<Window> windows = new ArrayList<Window>();
    
    // Special windows are always drawn on top of regular windows
    // and can be tutorial windows, tool tips, etc.
    //
    private Tooltip tooltip;
        
    private ProgressBar systemProgress;
    
    private Label systemProgressBack;
    
    private Label systemProgressDropShadow;
    
    private Label systemError;
    		
    private boolean showProgress = false;
    //
    // End of special windows 
    
	private static Graphics graphics = null;
    
    private int screenWidth;
    
    private int screenHeight;

	private int screenDpi;

    private static Control lastFound;
        
    private Point lastMousedown;
    
    private long lastMouseMovedTime;
    
	private Control lastMousedOverControl;
	
	private MouseEvent lastMouseMoveEvent;
    
	private final SerializableLock specialWindowLock = new SerializableLock(); 
	
	private static boolean mousedown;
	
	private int popupCount = 0;

	private DragInfo dragging = null;

	private Hourglass hourglass;
		
	private List<PopupMenu<?>> popupMenus = new ArrayList<>();

	private enum MouseProcessType
	{
		Click,
		Move,
	}
	
    public WindowManager(final FontInfo errorFontInfo, final FontInfo labelFontInfo, final Graphics graphics_, final int width_, final int height_, final int dpi_)
    {
        graphics = graphics_;
        screenWidth = width_;
        screenHeight = height_;
        screenDpi = dpi_;
        
        synchronized(specialWindowLock)
        {
        	final AbsDims dims = new AbsDims(
        			screenWidth / 2 - 175,
        			screenHeight / 2 - 10,
        			screenWidth / 2 + 175,
        			screenHeight / 2 + 10);
        	systemProgress = new ProgressBar(dims, "", labelFontInfo.getTextColor(), Color.BLACK);
        	systemProgressBack = new Label(dims, "");
        	systemProgressBack.setSolidBackground(true);
        	systemProgressBack.setBackground(Color.black);
        	final AbsDims dropShadowDims = dims.makeCopy();
        	dropShadowDims.move(2, 2);
        	systemProgressDropShadow = new Label(dropShadowDims, "");
        	systemProgressDropShadow.setSolidBackground(true);
        	systemProgressDropShadow.setBackground(Color.gray);
        	
        	final int errorHeight = getTextHeight(errorFontInfo) * 3;
        	final AbsDims systemErrorDims = new AbsDims(
        			0,
        			(screenHeight - errorHeight) / 2,
        			screenWidth,
        			(screenHeight - errorHeight) / 2 + errorHeight
        			);
        	systemError = new Label(systemErrorDims);
        	systemError.setBackground(Environment.getWindowingSystemDark());
        	systemError.setSolidBackground(true);
        	systemError.setJustification(Justification.Center);
        	systemError.setFontInfo(errorFontInfo);
        }
    }
    
    public void addListener(WindowListener windowListener)
    {
    	listeners.add(windowListener);
    }
    
    public void setProgress(final float progress)
    {
    	systemProgress.setPercentage(progress);
    }
    
    public void turnOnProgress()
    {
    	synchronized (specialWindowLock) 
    	{
        	showProgress = true;
        }

    }
    
    public void turnOffProgress()
    {
    	synchronized (specialWindowLock) 
    	{
        	showProgress = false;
        }
    }
    
    
    public void addWindow(final Window window) 
    {
        synchronized(windowLock)
        {
            if( !windows.contains(window))
            {
                if( window != null )
                {
                    windows.add(window);
                }
            }
        }
    }
    
    public boolean containsWindow(final ControlContainer window) 
    {
        synchronized(windowLock)
        {
            return windows.contains(window);
        }
    }
    
    public void render()
    {
        synchronized(windowLock)
        {
        	popupMenus.clear();
            for( Window window : windows )
            {
                window.renderWindow(graphics, mousedown);
            }
            popupMenus.forEach(x -> renderControl(x, graphics, mousedown));
        }
        
        // Now render the tooltip, if there is one.
        synchronized(specialWindowLock)
        {
        	renderTooltip();
        	    		
    		renderSystemProgress();
    		
    		renderDraggingBox();
    		
    		renderSystemError();
    		    		
    		renderHourglass();
    	    		
        }
        
    }
	
	private void renderHourglass() 
	{
		if( hourglass != null )
		{
			hourglass.render();
		}
	}

	private void renderSystemError() 
	{
		if( systemError.getText() != null && !systemError.getText().equals("") )
		{
			systemError.render(graphics, false);
		}	
	}

	private void renderSystemProgress() 
	{
		if( showProgress )
		{
			systemProgressDropShadow.render(graphics, false);
			systemProgressBack.render(graphics, false);
			systemProgress.render(graphics, false);
		}
	}

	public void renderTooltip() 
	{
		if( lastMousedOverControl == null )
		{
			tooltip = null;
			return;	
		}
		
		final Tooltip tooltip = lastMousedOverControl.getTooltip();
		if( this.tooltip != tooltip || tooltip == null )
		{
			this.tooltip = null;
			return;
		}
		this.tooltip = tooltip;
		
		final long elapsed = System.currentTimeMillis() - getLastMouseMovedTime();
		if( elapsed > getMouseSteadyDelay() && elapsed < tooltip.getTooltipDisplayLength() )
		{
			renderControl(tooltip, graphics, mousedown);
		}
	}
	
	/**
     * renderControl method -- recursive!
     * 
     * @param control
     * @param graphics
     * @param mousedown
     */
    public void renderControl(final Control control, final Graphics graphics, final boolean mousedown) 
    {
        control.render(graphics, mousedown);
        for( Control child : control.getControls() )
        {
        	if(!(child instanceof Tooltip) && !(child instanceof PopupMenu)) 
        	{
        		renderControl(child, graphics, mousedown);
        	}
        	if( child instanceof PopupMenu )
        	{
        		popupMenus.add((PopupMenu<?>)child);
        	}
        }
	}
    
    private void renderDraggingBox() 
    {
		if( dragging != null )
		{			
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
			
			turnOffProgress();
			tooltip = null;
		}
	}

	private List<Window> getWindowsReversed()
    {
        // Reverse the windows and go through them backwards
        final List<Window> reversed = new ArrayList<Window>();            
        
        synchronized(windowLock)
        {
            for( Window window : windows )
            {
                reversed.add(0, window);
            }
        }
        
        return reversed;
    }
    
    public CommandToken<?> processMouseClick(final int x, final int y)
    {
    	mousedown = true;
    	lastMousedown = new Point(x, y);
    	return processMouseAction(MouseProcessType.Click, x, y);
    }

	private CommandToken<?> processMouseAction(final MouseProcessType processType, final int x, final int y) 
	{
		final Collection<Control> ignore = new ArrayList<Control>();
    	Control control = null;
    	
    	do
    	{
	    	control = findControlAt(ignore, x, y);
	    	if( control != null )
	    	{
	    		CommandToken<?> token = null;
	    	
	    		if( processType == MouseProcessType.Click )
	    		{
	    			token = clickThroughChain(x, y, control, control.getToken());
	    			
	    			removeUnclickedTransients(control);
	    			
	    			if( token != null && control.isEnabled() )
	    			{
	    				return token;
	    			}
	    		}
	    		else if( processType == MouseProcessType.Move )
	    		{
	    			token = moveThroughChain(x, y, control, control.getToken());
	    			if( token != null )
	    			{
	    				return token;
	    			}
	    		}
	    		ignore.add(control);
	    	}	    	
    	}
    	while(control != null);
    	
    	return null;
	}

	private void removeUnclickedTransients(final Control exception) 
	{
		final Collection<Control> controls = findAllControls();
		
		final Stream<Control> transients = controls.stream().filter(x -> x instanceof TransientControl);
		
		transients.forEach(x -> {
			if( x != exception )
			{
				x.getParent().removeControl(x);
				((TransientControl)x).hidden();
			}
		});
	}

	private CommandToken<?> clickThroughChain(final int x, final int y, final Control control, final CommandToken<?> token)
	{		
		if( control != null )
		{
			final AbsDims dims = control.getScreenDims();
		    if( control.doSpecialClickActions(x - dims.left, y - dims.top) )
		    {
		    	if( control.getParent() != null )
		    	{
		    		// In case the return value is null, we want to ignore it and return the original token
		    		// if the return value was not null, it should have been a pointer to token anyway.
		    		clickThroughChain(x, y, control.getParent(), token);
		    	}
		    }
	    	return token;
		}
		
		return null;
	}
    
	public void pointer() 
	{
		hourglass = null;
	}

	public void hourglass() 
	{
		hourglass = new Hourglass(graphics, Environment.bigGap() * 2, screenWidth, screenHeight);
	}

	private CommandToken<?> moveThroughChain(final int x, final int y, final Control control, final CommandToken<?> token)
	{
		if( control != null )
		{
			final AbsDims dims = control.getScreenDims();
		    if( control.doSpecialMoveActions(x - dims.left, y - dims.top) )
		    {
		    	if( control.getParent() != null )
		    	{
		    		// In case the return value is null, we want to ignore it and return the original token
		    		// if the return value was not null, it should have been a pointer to token anyway.
		    		moveThroughChain(x, y, control.getParent(), token);
		    	}
		    	return token;
		    }
		}
		
		return null;
	}
    
    public void setGraphics2D(final Graphics2D g2D)
    {
        graphics.setGraphics2D(g2D);
    }

    public void removeWindow(final ControlContainer window)
    {
        synchronized(windowLock)
        {
            windows.remove(window);
        }        
    }

    
    public void removeAllWindows()
    {
        synchronized(windowLock)
        {
            windows.clear();
        }

        // Just in case it's not set correctly.
		popupCount = 0;
    }

    public int getScreenWidth()
    {
        return screenWidth;
    }

    public int getScreenHeight()
    {
        return screenHeight;
    }

	public int getScreenDpi() 
	{
		return screenDpi;
	}

    public static int getTextWidth(final String text, final FontInfo fontInfo)
    {        
        return graphics.getTextWidth(text, fontInfo);
    }

    public static int getTextHeight(final FontInfo fontInfo)
    {
        return graphics.getTextHeight(fontInfo);
    }
    
    public void nullAllWindows()
    {
    	synchronized(windowLock)
    	{
	        for( @SuppressWarnings("unused") ControlContainer window : windows )
	        {
	        	// Is there any point in doing this?
	            window = null;
	        }
    	}
    }

	public void processMouseWheel(MouseWheelEvent arg0) 
	{
		final Control control = findControlAt(null, arg0.getX(), arg0.getY());
		
		if( control != null )
		{
			control.processMouseWheel(arg0);
		}
	}

    public boolean isInside( final AbsDims dims, final int x, final int y )
    {
    	if( dims != null )
    	{
	    	AbsDims dimsToUse = dims;
	    	if( dims instanceof PctDims )
	    	{
	    		dimsToUse = dims.absoluteify();
	    	}
    	
    		if( x > dimsToUse.left && x < dimsToUse.right && y > dimsToUse.top && y < dimsToUse.bottom )
    		{
    			return true;
    		}
    	}

        return false;

    }

	public Control findControlAt(final int x, final int y)
	{
		return findControlAt(new ArrayList<Control>(), x, y);
	}
	
    /**
     * Finds and returns the control at the given screen location
     * 
     * @param x
     * @param y
     * @return
     */
	public Control findControlAt(final Collection<Control> ignore, final int x, final int y)
	{
		final List<Window> reversed = getWindowsReversed();
		
		lastFound = null;
		
        synchronized(windowLock)
        {
        	// Check popup menus first
        	//
        	for( final PopupMenu<?> popupMenu : popupMenus )
        	{
        		if( popupMenu.getDimensions().contains(x, y) )
        		{
        			return popupMenu;
        		}
        	}        
        	
        	// No popup menus found? Check all windows
        	//
            for( Window window : reversed )
            {               	
            	final Control clicked = findControlInControl(window.getControlsReversed(), ignore, x - window.getDimensions().left, y - window.getDimensions().top);
            	if( clicked != null )
            	{
            		return clicked;
            	}
            	if( window.isPopup() )
            	{
            		// This case occurs if no control on the popup is clicked.
            		return null;
            	}
            }
        }
        
        return null;
	}

	private Control findControlInControl(final Collection<Control> controls, final Collection<Control> ignore, final int x, final int y) 
	{
		
		if( controls != null )
		{
	    	for( Control control : controls ) 
	    	{
	    		if( !(ignore != null && ignore.contains(control)) )
	    		{
	    			if( isInside(control.getDimensions(), x, y) )
		    		{
		    			final Control child = findControlInControl(control.getControlsReversed(), ignore, x - control.getDimensions().left, y - control.getDimensions().top);
		    			if( lastFound != null )
		    			{
		    				return lastFound;
		    			}
		    			if( child == null )
		    			{
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
	public void removeTopWindow() 
	{
        popupCount--;
        for(final WindowListener listener : listeners)
        {
        	listener.closePopup(popupCount);
        }
        
		synchronized(windowLock)
		{
			windows.remove(windows.size() - 1);
		}
		
	}

	/**
	 * Decide what state tooltipmode should be in based on the mouse move event
	 * and the last moused-over control
	 * 
	 * @param event
	 */
	public void processMouseMove(final MouseEvent event) 
	{

		final Control control = findControlAt(null, event.getX(), event.getY());

		clearMouseOvers(control);
		
        synchronized(specialWindowLock)
        {
			if( control != null && control instanceof ListBox )
			{
				// Special case for listboxes
				((ListBox)control).setTooltipByPosition(event.getY() - control.getScreenDims().top);
			}
			if( control != null )
			{
				lastMouseMovedTime = System.currentTimeMillis();
				lastMouseMoveEvent = event;
				lastMousedOverControl = control;
				tooltip = lastMousedOverControl.getTooltip();
			}
			else
			{
				tooltip = null;
			}
			
        }

        processMouseAction(MouseProcessType.Move, event.getX(), event.getY());
	}

	private void clearMouseOvers(final Control mouseOverControl) 
	{
		final Collection<Control> mouseOverControls = mouseOverControl != null ? mouseOverControl.findAllParents() : null;
		
		final Collection<Control> allControls = findAllControls();
		
		for( final Control control : allControls )
		{
			if( mouseOverControls == null || !mouseOverControls.contains(control) )
			{
				control.clearMouseOver();
			}
		}
	}

	private Collection<Control> findAllControls() 
	{
		final Collection<Control> ret = new ArrayList<Control>();

		synchronized(windowLock)
		{
			for( final Window window : windows )
			{
				ret.addAll(window.getControlsRecursive());
			}
		}
		
		return ret;
	}

	public void mouseReleased(final int x, final int y) 
	{
		final String originalError = systemError.getText();
						
		mousedown = false;
		if( dragging != null )
		{
			final Control dropTarget = findControlAt(x, y);
			if( dropTarget != null && dragging != null )
			{
				dropTarget.drop(dragging.getControlBeingDragged());
			}
			dragging = null;
		}
		
		if( (originalError != null && !originalError.equals("")) && (systemError.getText() != null || !systemError.getText().equals("")) && !originalError.equals(systemError.getText()) )
		{
			turnOffErrorMessage();
		}
	}

	public void mousePressed() 
	{
		mousedown = true;
		
		turnOffErrorMessage();
	}

	public void systemError(String message)
	{
		systemError.setText(message);
		WorkerLog.debug(message);
	}

	public void turnOffErrorMessage() 
	{
		synchronized(specialWindowLock)
		{
			systemError.setText("");
		}
	}
	
	public static Control getLastControl()
	{
		return lastFound;
	}

	public Control getLastMousedOverControl() 
	{	
		return lastMousedOverControl;
	}

	public long getLastMouseMovedTime() 
	{
		return lastMouseMovedTime;
	}
	
	public long getMouseSteadyDelay()
	{
		return mouseSteadyDelay;
	}

	public Graphics getGraphics() 
	{
		return graphics;
	}

	public MouseEvent getLastMouseMoveEvent() 
	{
		return lastMouseMoveEvent;
	}

	public CommandToken<?> enterKeyPressed()
	{
		synchronized(windowLock)
        {
            return getWindowsReversed().get(0).enterKeyPressed();
        }

	}

	public CommandToken<?> escapeKeyPressed()
	{
		synchronized(windowLock)
        {
            return getWindowsReversed().get(0).escapeKeyPressed();
        }
        		
	}
	
	public CommandToken<?> processKeyPress(final KeyEvent arg0)
	{
		
        synchronized(windowLock)
        {
        	if( countWindows() > 0 )
        	{
        		return getWindowsReversed().get(0).processKeyPress(arg0);
        	}        
        }

    	return null;
	}

	private int countWindows()
	{
		synchronized(windowLock)
		{
			return windows.size();
		}
	}

	public void setProgressMessage(final String message) 
	{
		systemProgress.setText(message);
		systemProgressBack.setText(message);
		
	}

	public Screen getScreen(Class<?> class1)
	{
		synchronized(windowLock)
		{
			for( ControlContainer window : windows )
			{
				if( window.getClass() == class1 || class1.isAssignableFrom(window.getClass()) )
				{
					if( window instanceof Screen )
					{
						return (Screen)window;
					}
				}
			}
		}
		
		return null;
	}
	
    public void showPopup(final Window popup)
    {
    	lastMousedOverControl = null;
    	popupCount++;
        popup.setPopup(true);
        addWindow(popup);
        for(final WindowListener listener : listeners)
        {
        	listener.showPopup(popupCount);
        }
    }

	public void closeAllPopups()
	{
		
       	for( int i = 0; i < popupCount; i++ )
       	{
       		removeTopWindow();	
       	}
        
       	popupCount = 0;
        for(final WindowListener listener : listeners)
        {
        	listener.closeAllPopups();
        }
        
	}

	public void processMouseDrag(MouseEvent arg0) 
	{
		synchronized(specialWindowLock)
		{
			final Control control = findControlAt(arg0.getX(), arg0.getY());
			if( control != null && control.isDraggable() && dragging == null )
			{
				dragging = new DragInfo();
				dragging.setControlBeingDragged(control);
				dragging.setInitialMouseEvent(arg0);
			}
			
			if( dragging != null )
			{
				dragging.setMouseEvent(arg0);
			}
		}
	}

	public static int getTextHeight() 
	{
		return getTextHeight(graphics.getFontInfo());
	}

	public Point getLastMouseDown() 
	{
		return lastMousedown;
	}

	public static void adjustFont(final FontInfo fontInfo, final String message, final int boxWidth, final int boxHeight)
	{
		int renderWidth = Integer.MAX_VALUE;
		int renderHeight = Integer.MAX_VALUE;
		
		int fontSize = fontInfo.getFontSize();
		
		while(renderWidth > boxWidth * 0.98F || renderHeight > boxHeight * 0.98F)
		{
			FontInfo temp = fontInfo.makeCopy();
			temp.setFontSize(fontSize);
			renderWidth = WindowManager.getTextWidth(message, temp);
			renderHeight = WindowManager.getTextHeight(temp);
			if( fontSize <= 7 )
			{
				WorkerLog.debug("Font size is " + fontSize + " for message " + message);
				break;
			}
			fontSize--;
		}
		fontInfo.setFontSize(fontSize);
	}

	public boolean isDragging()
	{
		return dragging != null;
	}

	public boolean isMouseDown()
	{
		return mousedown;
	}
	
	public <T> void removeWindows(Class<T> clazz)
	{
		synchronized(windowLock)
		{
			final List<Window> windowsToRemove = windows.stream().filter(x -> x.getClass() == clazz)
				.collect(Collectors.toList());
			
			windows.removeAll(windowsToRemove);
		}
	}

	/**
	 * Returns the topmost window on the stack that matches the
	 * type given
	 * 
	 * @param windowClazz
	 * @return
	 */
	public Optional<Window> findTopWindowByType(Class<?> windowClazz)
	{
		for( final Window window : getWindowsReversed() )
		{
			if( window.getClass() == windowClazz )
			{
				return Optional.of(window);
			}
		}
		
		return Optional.empty();
	}

	public AbsDims calculateTooltipDims(Tooltip tooltip) 
	{
		if( lastMouseMoveEvent == null )
		{
			return null;
		}
		
		final int x = lastMouseMoveEvent.getX();
		final int y = lastMouseMoveEvent.getY();
		
		// Roughly a third of an inch offset from the mouse pointer
		final int yOffset = Math.round(getScreenDpi() * 0.33F);
		
		final AbsDims parentDims = tooltip.getParent().getScreenDims();
		final AbsDims tooltipDims = new AbsDims(
				x - 1 - parentDims.left,
				y - 1 + yOffset - parentDims.top,
				x + tooltip.getWidth() + 1 - parentDims.left,
				y + tooltip.getHeight() + yOffset - parentDims.top
				);
	
		return tooltipDims;
	}

	/**
	 * For times when we need to override the last mouse down position
	 * 
	 * @param x
	 * @param y
	 */
	public void setLastMousedown(int x, int y) 
	{
		lastMousedown = new Point(x, y);
	}

	public void removePopupMenus() 
	{
		final Collection<Control> controls = findAllControls();
		
		final Stream<Control> popups = controls.stream().filter(x -> x instanceof PopupMenu<?>);
		
		popups.forEach(x -> {
			x.getParent().removeControl(x);
		});
	}
}