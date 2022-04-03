package com.rallycallsoftware.cellojws.controls.listbox;

import com.rallycallsoftware.cellojws.adapter.Graphics;

public class HiddenListBoxItem<T> implements ListBoxItem
{

	private T item;
		
	public HiddenListBoxItem()
	{
		
	}
	
	public HiddenListBoxItem(T item)
	{
		setItem(item);
	}
	
	public T getItem()
	{
		return item;
	}

	public void setItem(T item)
	{
		this.item = item;
	}

	@Override
	public int getHeight()
	{
		return 0;
	}

	@Override
	public void draw(Graphics graphics, int leftDraw, int top, int widthMax)
	{

	}
	
	@Override
	public int getWidth()
	{
		return 0;
	}

	@Override
	public String toString()
	{
		if( item != null )
		{
			return item.toString();
		}
		
		return "";
	}
}
