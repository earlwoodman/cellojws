/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.controls;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.Justification;
import com.cellojws.general.SerializableLock;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.Image;
import com.cellojws.logging.WorkerLog;
import com.cellojws.special.Tooltip;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.DragInfo;
import com.cellojws.windowing.Window;
import com.cellojws.windowing.WindowManager;


public class Control implements ControlContainer
{
    private static final String TOOLTIP_KEY = "tooltip";

	private Justification justification;

    private AbsDims dimensions;
    
    private AbsDims screenDimensions = null;
    
    private Control parent;
    
    private final List<Control> componentControls = new ArrayList<Control>();
    
    private final List<String> componentControlNames = new ArrayList<String>();
    
    private CommandToken<?> token;
    
    private CommandToken<DragDropBean<Control, Control>> dropToken = null;
    
    private boolean beingProcessed = false;

	protected final SerializableLock controlsLock = new SerializableLock();
		
	private FontInfo fontInfo = null;

	private FontInfo disabledFontInfo = null;
    	
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

	private AbsDims borderDims = null;

	private boolean drawDropShadow;

	private static final Color controlBorderColour = new Color(50, 40, 30);

	//protected 
	private static Environment environment;    

    public static void setEnvironment(Environment environment) 
    {
		Control.environment = environment;
	}
    
	public static Environment getEnvironment()
	{
		return environment;
	}

	public Control()
	{
		initialize();
	}
	
	public Control(final AbsDims dim)
	{
		if( dim != null )
		{
			dimensions = dim.absoluteify();
		}
		
		token = null;
		
		initialize();
	}
	
	public Control(final AbsDims dim, final CommandToken<?> token_)
    {
		if( dim != null )
		{
			dimensions = dim.absoluteify();
		}
        
        token = token_;
                
        initialize();
    }
    
    private void initialize() 
    {
    	fontInfo = getStandardFont();
    	
    	initAffineTransform();
	}

	private void initAffineTransform() 
    {
    	at = new AffineTransform();
	}

	private boolean containsControl(final Control control) 
    {
        synchronized(controlsLock)
        {
            return componentControls.contains(control);
        }
    }
    
    public AbsDims getScreenDims() 
    {
        if( parent != null )
        {
        	if( screenDimensions == null )
        	{
        		final AbsDims parentScreenDims = parent.getScreenDims();
        		if( dimensions != null )
        		{
        			screenDimensions = dimensions.absoluteify().shift(parentScreenDims);
        			borderDims = screenDimensions.makeCopy();
        			borderDims.expand(1, 1);
        		}
        	}
        	
        	return screenDimensions;
        }
        
        return dimensions.absoluteify();
    }
    
	public AbsDims getDimensions()
    {
        return dimensions;
    }
    
    public void render(final Graphics graphics, final boolean mousedown)
    {    	
    	graphics.setFontInfo(getFontToUse());
    	if( drawBorder )
    	{    		
    		graphics.drawRect(borderDims, getControlBorderColour());
    	}
    	if( image != null )
    	{
            setxRatio((float)getScreenDims().getAbsWidth() / image.getWidth());
            setyRatio((float)getScreenDims().getAbsHeight() / image.getHeight());
			
			final AbsDims drawDimensions = determineImageDimsToUse();
    			
    		graphics.drawImage(image.getBufferedImage(), drawDimensions, rotation ? at : null);
    	}

    	if( drawDropShadow )
    	{
	    	final Image dropShadow = Environment.getDropShadowImage();
	    	
	    	final AbsDims dropShadowDims = getScreenDims().makeCopy();
	    	dropShadowDims.expand(Environment.tinyGap(), Environment.tinyGap());
	    	
	    	graphics.drawImage(dropShadow, dropShadowDims);
    	}

    }

    private FontInfo getFontToUse()
	{
    	if( !enabled )
    	{
    		if( disabledFontInfo == null )
    		{
    			disabledFontInfo = fontInfo != null ? fontInfo.makeCopy() : getStandardFont().makeCopy();    		
    			disabledFontInfo.setTextColor(Color.DARK_GRAY);
    		}
    		return disabledFontInfo;
    	}
    	
    	if( fontInfo != null )
    	{
    		return fontInfo;
    	}
    	
		return getStandardFont();
	}

	/**
     * This method and doSpecialMoveActions() are for behaviours that ALL objects of the 
     * type should express. The command system is for behaviours that a particular object
     * of the type should express. 
     */
    public boolean doSpecialClickActions(final int x, final int y)
    {
    	return true;
    }
    
    public void processMousePress(final int x, final int y)
    {
		setBeingProcessed(true);
    }
    
    public void setParent(final Control control)
    {
        parent = control;
    }
    
    public Control getParent() 
    { 
        return parent; 
    }

    public CommandToken<?> processKeyPress(final KeyEvent keyEvent)
    {
    	return null;
    }

    public CommandToken<?> getToken()
    {
        return token;
    }

    public void setToken(final CommandToken<?> token)
    {
        this.token = token;
    }

    public final void setDimensions(final AbsDims dims)
    {
        dimensions = dims;
        dimensionsChanged();
    }

    public void dimensionsChanged()
    {
    	resetScreenDims(this);
    }
    
    public static void resetScreenDims(final Control control)
    {
    	control.setScreenDims(null);
    	for( final Control child : control.getControls()) 
    	{
    		resetScreenDims(child);
    	}
    }
    
    public void setBeingProcessed(final boolean beingProcessed_)
    {
        beingProcessed = beingProcessed_;
    }

    public boolean isBeingProcessed()
    {
        return beingProcessed;
    }    

    public void addControl(final String key, final Control control_)
    {
        synchronized(controlsLock)
        {
        	if( control_ != null ) 
        	{
	            if( okToAddControl(control_) ) 
	            {
	                control_.setParent(this);
	                componentControls.add(control_);
	                if( control_ instanceof Tooltip )
	                {
	                	componentControlNames.add(TOOLTIP_KEY);
	                }
	                else
	                {
	                	componentControlNames.add(key);
	                }
	            }
        	}
        }        
    	
    }
    
    private boolean okToAddControl(final Control control)
	{
    	if( componentControls.contains(control) )
    	{
    		return false;
    	}
    	
    	if( control instanceof Tooltip )
    	{
    		if( componentControls.size() > 0 && getTooltip() != null )
    		{
    			WorkerLog.error("Can't add this tooltip. Tooltip would have tooltip siblings!");
    			return false;
    		}
    	}
    	
    	return true;
	}

	public void addControl(final Control control_)
    {
    	if( control_ != null )
    	{
    		addControl(control_.toString(), control_);
    	}
    }
    
    public void insertControlZOrderBottom(final Control control_)
    {
    	insertControlZOrderBottom(null, control_);
    }
    
    public void insertControlZOrderBottom(final String key, final Control control_)
    {
    	String keyToUse;
    	if( key == null )
    	{
    		keyToUse = control_ != null ? control_.toString() : null;
    	}
    	else
    	{
    		keyToUse = key;
    	}
    	
    	if( keyToUse != null )
    	{
	    	synchronized(controlsLock)
	    	{
	            if( containsControl(control_) == false ) 
	            {
	                control_.setParent(this);
	                componentControls.add(0, control_);
	                componentControlNames.add(keyToUse);
	            }
	    	}
    	}
    }
    
    public void addControls(final Collection<Control> controls_)
    {        
    	for( Control control : controls_ )
    	{
            addControl(control);
    	}
    	
    }
    
    public List<Control> getControls()
    {
    	final List<Control> allControls = new ArrayList<Control>();
    	synchronized(controlsLock)
    	{
    		for( Control control : componentControls )
    		{
    			allControls.add(control);
    		}
    	}
    			
    	return allControls;
    }

    public Collection<Control> getControlsRecursive()
    {
    	final List<Control> allControls = new ArrayList<Control>();
    	synchronized(controlsLock)
    	{
    		for( Control control : componentControls )
    		{
    			allControls.addAll(control.getControlsRecursive());
    			allControls.add(control);
    		}
    	}
    			
    	return allControls;
    }

    public List<Control> getControlsReversed()
    {
        final List<Control> reversed = new ArrayList<Control>();

    	synchronized(controlsLock)
    	{
	        // Need to go through the controls in reverse. 
	        for( Control control : getControls() )
	        {
	            reversed.add(0, control);
	        }
    	}
	        
        return reversed;
    }
        

	public void removeControl(final Control control)
    {
        synchronized(controlsLock)
        {
        	if( control != null )
        	{
        		int index = componentControls.indexOf(control);
        		if( index != -1 )
        		{
        			componentControls.remove(index);
        			componentControlNames.remove(index);
        		}
        	}
        }
    }
	
	public void processMouseWheel(final MouseWheelEvent wheelEvent)
	{
		
	}

	/**
	 * Clears all controls except tooltips
	 * 
	 */
    public void clear()
    {
    	final Tooltip tooltip = getTooltip();    	
        componentControls.clear();
        componentControlNames.clear();
        if( tooltip != null )
    	{
        	addControl(TOOLTIP_KEY, tooltip);
    	}
    }
	
	public Control getControl(final String name) 
	{
		final int index = componentControlNames.indexOf(name);
		if( index != -1 )
		{
			return componentControls.get(index);
		}
		
		return null;
	}


    public int getRightCentered(final int width)
    {
        return (getDimensions().getAbsWidth() - width) / 2 + width;
    }

    public int getTopCentered(final int height)
    {
        return (getDimensions().getAbsHeight() - height) / 2;
    }

    public int getLeftCentered(final int width)
    {
        return (getDimensions().getAbsWidth() - width) / 2;
    } 
	
    /*
     * Use Environment.smallGap(), bigGap(), tinyGap(), etc. instead of these methods
     * 
     */    
    @Deprecated
	public static int getStdGap()
	{
		// A gap of roughly 1/20 of an inch.
		return (int)(environment.getWindowManager().getScreenDpi() * 0.05F); 
	}
	
	@Deprecated
	public static int getHalfGap()
	{
		return getStdGap() / 2;
	}
	
	@Deprecated
	public static int getDblGap()
	{
		return getStdGap() * 2; 
	}

	/**
	 * Returns the window that contains this control
	 * 
	 * @return
	 */
	public ControlContainer getWindow() 
	{
		if( this instanceof Window )
		{
			return (ControlContainer)this;
		}
		else
		{
			if( getParent() == null )
			{			
				return null;
			}
			else
			{
				return getParent().getWindow();
			}
		}				
	}

	/**
	 * Brings the given control to the front of the z order
	 * 
	 * @param list
	 */
	public void bringToFront(final Control control) 
	{
		synchronized(controlsLock)
		{
			if( componentControls.contains(control) )
			{
				final String key = getKey(control);
				removeControl(control);
				addControl(key, control);
			}
		}
	}

	public String getKey(final Control control) 
	{
		return componentControlNames.get(componentControls.indexOf(control));
	}

	public void addPolygon(final PolygonControl polygon)
	{
		polygons.add(polygon);
		addControl(polygon);
	}

	public float getxRatio()
	{
		return xRatio;
	}

	private void setxRatio(float xRatio)
	{
		this.xRatio = xRatio;
	}

	public float getyRatio()
	{
		return yRatio;
	}

	private void setyRatio(float yRatio)
	{
		this.yRatio = yRatio;
	}

	public Image getImage()
	{
		return image;
	}
	
	public void setImage(Image image)
	{
		this.image = image;
	}

	public void clearImage()
	{
		setImage(null);
	}
	
	protected AlphaComposite makeComposite(float alpha)
	{
		 int type = AlphaComposite.SRC_OVER;
		 if( alpha < 0 )
		 {
			 WorkerLog.error("Alpha below zero!");
			 return null;
		 }
		 else if( alpha > 1 )
		 {
			 WorkerLog.error("Alpha above one!");
			 alpha = 1;
		 }
		 return(AlphaComposite.getInstance(type, alpha));
	}

    /**
     * This method and doSpecialClickActions() are for behaviours that ALL objects of the 
     * type should express. The command system is for behaviours that a particular object
     * of the type should express. 
     */
	public boolean doSpecialMoveActions(int x, int y) 
	{	
		mouseOver = true;
		return true;
	}	 

	public Color getBackground()
	{
		return background;
	}

	public void setBackground(Color background)
	{
		this.background = background;
	}
	
	public WindowManager getWindowManager()
	{
		return environment.getWindowManager();
	}

	public int getScreenHeight()
	{
		return environment.getWindowManager().getScreenHeight();
	}
	
	public int getScreenWidth()
	{
		return environment.getWindowManager().getScreenWidth();
	}

	public boolean isMouseOver() 
	{
		return mouseOver;
	}

	public void clearMouseOver() 
	{
		this.mouseOver = false;
	}

	public Collection<Control> findAllParents() 
	{
		return findAllParents(new ArrayList<Control>());
	}
	
	private Collection<Control> findAllParents(final Collection<Control> all)
	{
		all.add(this);

		if( getParent() != null )
		{
			return getParent().findAllParents(all);
		}
		
		return all;
	}

	public void rotateImage() 
	{
		if( xRatio == 0F || yRatio == 0F )
		{
			return;
		}
		
		final AbsDims dimsToUse = determineImageDimsToUse();

		if( rotation == false )
		{			
			at.translate(dimsToUse.left, dimsToUse.top);
			at.scale(xRatio, yRatio);
		}

		rotation = true;

		// Rotation doesn't take scaling into account. 
		// Dividing by 2 rotates around center point.
		at.rotate(0.4, image.getWidth() / 2, image.getHeight() / 2);
		
		//image.setBufferedImage(ato.filter(image.getBufferedImage(), null));
	}

	public void stopRotation()
	{
		rotation = false;
		initAffineTransform();
	}
	
	private AbsDims determineImageDimsToUse() 
	{
		if( image != null )
		{
			AbsDims drawDimensions = image.getDrawDimensions();
			if( drawDimensions != null )
			{
				drawDimensions = drawDimensions.makeCopy();
				drawDimensions.move(getScreenDims().left, getScreenDims().top);
				return drawDimensions;
			}
			
			return getScreenDims();
		}
		else
		{
			return null;
		}
	}

	public float getAspectRatio() 
	{
		return (float)getDimensions().getAbsWidth() / getDimensions().getAbsHeight();
	}

	public CommandToken<?> createCloseToken()
	{
		return new CommandToken<Object>(ControlController::close);
	}

	public void move(int x, int y)
	{
		dimensions.move(x, y);

		// Force screen dims to recalculate
		screenDimensions = null;
	}
	
    public boolean isDropTarget() 
    {
		return dropToken != null;
	}

	public boolean isDraggable() 
	{
		return draggable;
	}

	public void setDraggable(boolean draggable) 
	{
		this.draggable = draggable;
	}

	public CommandToken<DragDropBean<Control, Control>> getDropToken() 
	{
		return dropToken;
	}

	public void setDropToken(CommandToken<DragDropBean<Control, Control>> dropToken) 
	{	
		this.dropToken = dropToken;
	}

	public void drop(Control dragging) 
	{
		if( isDropTarget() )
		{
			DragDropBean<Control, Control> bean = dropToken.getPayload();
			if( bean == null )
			{
				bean = new DragDropBean<Control, Control>();
			}
			bean.setDragging(dragging);
			bean.setTarget(this);
			dropToken.setPayload(bean);
			dropToken.execute();
		}
		else
		{
			WorkerLog.error("This control does not accept dragged controls." + toString());
		}
	}

	public void setScreenDims(AbsDims dims) 
	{
		screenDimensions = dims;
	}

	public void dragRender(Graphics graphics, boolean mousedown, final AbsDims dims, DragInfo dragInfo)
	{
		render(graphics, mousedown);
	}

	public void update()
	{
		
	}

	public boolean isDrawBorder()
	{
		return drawBorder;
	}

	public void setDrawBorder(boolean drawBorder)
	{
		this.drawBorder = drawBorder;
	}

	public Justification getJustification() 
	{
		return justification;
	}

	public void setJustification(Justification justification) 
	{
		this.justification = justification;
	}

	public FontInfo getStandardFont()
	{
		return Environment.getLabelFontInfo();
	}

	public FontInfo getFontInfo() 
	{
		return fontInfo;
	}

	public void setFontInfo(FontInfo fontInfo) 
	{
		this.fontInfo = fontInfo;
		this.disabledFontInfo = null;
	}

	public void disable()
	{
		enabled = false;
	}

	public void enable()
	{
		enabled = true;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Searches the control's children for the named control
	 * @param name
	 * @return
	 */
	public Optional<Control> findControl(String name)
	{
		final Control control = getControl(name);
		
		if( control != null ) 
		{
			return Optional.of(control);			
		}
		
		for( final Control componentControl : componentControls )
		{
			final Optional<Control> componentControlChild = componentControl.findControl(name);
			if( componentControlChild.isPresent() )
			{
				return componentControlChild;
			}
		}
		
		return Optional.empty();
	}

	public static Color getControlBorderColour()
	{
		return controlBorderColour;
	}

	public Tooltip getTooltip()
	{
		final Optional<Control> tooltip = componentControls.stream().filter(x -> x instanceof Tooltip).findFirst();
		
		return tooltip.isPresent() ? (Tooltip)tooltip.get() : null;
	}
	
	public void setTooltip(Tooltip tooltip) 
	{ 
		removeTooltip();
		addControl(tooltip);
	}
	
	/**
	 * Removes the tooltip from the component controls if there is one. There
	 * should never be more than one anyway.
	 */
	public void removeTooltip()
	{
		final Optional<Control> tooltip = componentControls.stream().filter(x -> x instanceof Tooltip).findFirst();
		
		if( tooltip.isPresent() )
		{
			removeControl(tooltip.get());
		}
	}

	public void removeControls() 
	{
		componentControls.clear();
		componentControlNames.clear();
		polygons.clear();
		
	}

	public boolean isDrawDropShadow() 
	{
		return drawDropShadow;
	}

	public void setDrawDropShadow(boolean drawDropShadow) 
	{
		this.drawDropShadow = drawDropShadow;
	}

	
}
