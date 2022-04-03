package com.cellojws.controls;

public class DragDropBean<T extends Control, U extends Control> 
{
	private U target;
	
	private T dragging;

	public U getTarget() 
	{
		return target;
	}

	public void setTarget(U target) 
	{
		this.target = target;
	}

	public T getDragging() 
	{
		return dragging;
	}

	public void setDragging(T dragging) 
	{
		this.dragging = dragging;
	}	
	
}
