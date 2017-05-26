package com.rallycallsoftware.cellojwsdemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.rallycallsoftware.cellojws.adapter.DisplayManager;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.core.ScreenFactoryDispatcher;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class Application implements MouseListener, MouseWheelListener, MouseMotionListener, KeyListener {

	private boolean running = false;

	private final DisplayManager displayManager = new DisplayManager();

	private WindowManager windowManager;
	
	public static void main(String[] args)
	{
		new Application().run();
	}
	
	public void run()
	{
		final ScreenFactoryDispatcher dispatcher = new ScreenFactoryDispatcher() {
			
			public void completed() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void refresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Screen getScreen(Class<?> class1) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		final Environment environment = new Environment(dispatcher, "");

		windowManager = environment.getWindowManager();
                
        final Window window = displayManager.getFullScreenWindow();
        window.addMouseListener(this);
        window.addMouseWheelListener(this);
        window.addMouseMotionListener(this);
        window.addKeyListener(this);
        
        synchronized( this )
        {
            running = true;
        }
     
        while ( true )
        {    
            runLoop();
    		
    		synchronized( this )
    		{
    		    if( !running )
    		    {
    		        System.exit(0);
    		    }
    		}
    		try
    		{
    		    Thread.sleep(20);
    		}
    		catch(InterruptedException e)
    		{
       
    		}
        }
	}
	
	private void runLoop() 
	{
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

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
    	if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) 
    	{
    		synchronized(this)
    		{
    			running = false;	
    		}
    	}

	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
}
