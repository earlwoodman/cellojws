package com.rallycallsoftware.cellojws.windowing;

import java.awt.event.MouseEvent;

import com.rallycallsoftware.cellojws.controls.Control;

public class DragInfo 
{
	private Control controlBeingDragged;
	
	private int initialX;
	
	private int initialY;

	private int lastX;
	
	private int lastY;
	
	public Control getControlBeingDragged() 
	{
		return controlBeingDragged;
	}

	public void setControlBeingDragged(Control controlBeingDragged) 
	{
		this.controlBeingDragged = controlBeingDragged;
	}

	public int getInitialX() 
	{
		return initialX;
	}

	public int getInitialY() 
	{
		return initialY;
	}

	public int getLastX() 
	{
		return lastX;
	}

	public int getLastY() 
	{
		return lastY;
	}

	public void setMouseEvent(MouseEvent arg0)
	{
		lastX = arg0.getX();
		lastY = arg0.getY();
	}

	public void setInitialMouseEvent(MouseEvent arg0) 
	{
		initialX = arg0.getX();
		initialY = arg0.getY();	
	}
	
}
