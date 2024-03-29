package com.cellojws.adapter;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

/**
    The ScreenManager class manages initializing and displaying
    full screen graphics modes.
*/
public class DisplayManager 
{

    private GraphicsDevice device;

    private boolean fullScreen;

    private JFrame jframe;
    
    /**
        Creates a new ScreenManager object.
    */
    public DisplayManager(final boolean fullScreen) 
    {
    	this.fullScreen = fullScreen;
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
        
    	jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jframe.setUndecorated(true);
        jframe.setIgnoreRepaint(true);
        jframe.setResizable(false);
        
        jframe.setBackground(Color.white);
        jframe.setForeground(Color.black);
    }


    /**
        Returns a list of compatible display modes for the
        default device on the system.
    */
    public DisplayMode[] getCompatibleDisplayModes() 
    {
        return device.getDisplayModes();
    }


    /**
        Returns the first compatible mode in a list of modes.
        Returns null if no modes are compatible.
    */
    public DisplayMode findFirstCompatibleMode(final DisplayMode modes[])
    {
        DisplayMode goodModes[] = device.getDisplayModes();
        for (int i = 0; i < modes.length; i++) 
        {
            for (int j = 0; j < goodModes.length; j++) 
            {
                if (displayModesMatch(modes[i], goodModes[j])) 
                {
                    return modes[i];
                }
            }

        }

        return null;
    }


    /**
        Returns the current display mode.
    */
    public DisplayMode getCurrentDisplayMode() 
    {	
        return device.getDisplayMode();
    }


    /**
        Determines if two display modes "match". Two display
        modes match if they have the same resolution, bit depth,
        and refresh rate. The bit depth is ignored if one of the
        modes has a bit depth of DisplayMode.BIT_DEPTH_MULTI.
        Likewise, the refresh rate is ignored if one of the
        modes has a refresh rate of
        DisplayMode.REFRESH_RATE_UNKNOWN.
    */
    public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2)
    {
        if (mode1.getWidth() != mode2.getWidth() ||
            mode1.getHeight() != mode2.getHeight())
        {
            return false;
        }

        if (mode1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
            mode2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI &&
            mode1.getBitDepth() != mode2.getBitDepth())
        {
            return false;
        }

        if (mode1.getRefreshRate() !=
            DisplayMode.REFRESH_RATE_UNKNOWN &&
            mode2.getRefreshRate() !=
            DisplayMode.REFRESH_RATE_UNKNOWN &&
            mode1.getRefreshRate() != mode2.getRefreshRate())
         {
             return false;
         }

         return true;
    }


    /**
        Enters full screen mode and changes the display mode.
        If the specified display mode is null or not compatible
        with this device, or if the display mode cannot be
        changed on this system, the current display mode is used.
        <p>
        The display uses a BufferStrategy with 2 buffers.
    */
    public void setFullScreen(DisplayMode displayMode) 
    { 	        
        device.setFullScreenWindow(jframe);

        if (displayMode != null &&
            device.isDisplayChangeSupported())
        {
            try 
            {
                device.setDisplayMode(displayMode);
            }
            catch (IllegalArgumentException ex) { }
            // fix for mac os x
            jframe.setSize(displayMode.getWidth(), displayMode.getHeight());
        }
        createBufferStrategy(jframe);
    }


	public void createBufferStrategy(final JFrame frame)
	{
		// avoid potential deadlock in 1.4.1_02
        try 
        {
            EventQueue.invokeAndWait(new Runnable() 
            {
                public void run() 
                {
                    frame.createBufferStrategy(2);
                }
            });
        }
        catch (InterruptedException ex) 
        {
            // ignore
        }
        catch (InvocationTargetException  ex) 
        {
            // ignore
        }
	}


	public JFrame getJFrame()
	{
		return fullScreen ? (JFrame)getFullScreenWindow() : jframe;
	}


    /**
        Gets the graphics context for the display. The
        ScreenManager uses double buffering, so applications must
        call update() to show any graphics drawn.
        <p>
        The application must dispose of the graphics object.
    */
    public Graphics2D getGraphics() 
    {
    	final JFrame window = fullScreen ? (JFrame)device.getFullScreenWindow() : jframe;
        if (window != null) 
        {
            final BufferStrategy strategy = window.getBufferStrategy();
            return (Graphics2D)strategy.getDrawGraphics();            
        }
        else 
        {
            return null;
        }
    }


    /**
        Updates the display.
    */
    public void update() 
    {
    	final JFrame window = fullScreen ? (JFrame)device.getFullScreenWindow() : jframe;
    	
        if (window != null) 
        {
            BufferStrategy strategy = window.getBufferStrategy();
            if (!strategy.contentsLost()) 
            {
                strategy.show();
            }
        }
        
        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
    }

	/**
        Returns the window currently used in full screen mode.
        Returns null if the device is not in full screen mode.
    */
    public JFrame getFullScreenWindow() 
    {
        return (JFrame)device.getFullScreenWindow();
    }

    /**
        Returns the width of the window currently used in full
        screen mode. Returns 0 if the device is not in full
        screen mode.
    */
    public int getWidth()
    {
    	JFrame window = fullScreen ? (JFrame)device.getFullScreenWindow() : jframe;
        if (window != null) 
        {
            return window.getWidth();
        }
        else 
        {
            return 0;
        }
    }

    /**
        Returns the height of the window currently used in full
        screen mode. Returns 0 if the device is not in full
        screen mode.
    */
    public int getHeight() 
    {
    	JFrame window = fullScreen ? (JFrame)device.getFullScreenWindow() : jframe;
        if (window != null) 
        {
            return window.getHeight();
        }
        else {
            return 0;
        }
    }


    /**
        Restores the screen's display mode.
    */
    public void restoreScreen() 
    {
        java.awt.Window window = device.getFullScreenWindow();
        if (window != null) 
        {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }


    /**
        Creates an image compatible with the current display.
    */
    public BufferedImage createCompatibleImage(int w, int h, int transparency)
    {
        java.awt.Window window = device.getFullScreenWindow();
        if (window != null) 
        {
            GraphicsConfiguration gc = window.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, transparency);
        }
        return null;
    }
    
}
