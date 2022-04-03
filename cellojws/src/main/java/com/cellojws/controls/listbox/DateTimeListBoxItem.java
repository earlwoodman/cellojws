package com.cellojws.controls.listbox;

import com.cellojws.adapter.Graphics;
import com.cellojws.general.DateTime;
import com.cellojws.general.core.Environment;
import com.cellojws.windowing.WindowManager;

public class DateTimeListBoxItem implements ListBoxItem 
{

	public static final ListBoxItem Blank = new DateTimeListBoxItem(null);
	
	private DateTime value;
	
	public DateTimeListBoxItem(DateTime value) 
	{
		this.value = value;
	}

	public DateTime getValue() 
	{
		return value;
	}

	public void setValue(DateTime value) 
	{	
		this.value = value;
	}

	@Override
	public int getHeight() 
	{
		return WindowManager.getTextHeight(Environment.getListBoxFontInfo());
	}

	@Override
	public String toString()
	{
		return getValue() != null ? getValue().toString() : "";
	}

	@Override
	public void draw(Graphics graphics, int leftDraw, int top, int widthMax) 
	{
		graphics.drawText(toString(), leftDraw, top);
	}
	
	@Override
	public int getWidth()
	{
		return ListBox.PADDING + WindowManager.getTextWidth(toString(), Environment.getListBoxFontInfo());
	}

	@Override
	public int compare(ListBoxItem listBoxItem) 
	{ 
		if( listBoxItem == null )
		{
			return 1;
		}
		
		final DateTimeListBoxItem target = (DateTimeListBoxItem)listBoxItem;
		
		if( target.getValue() == null && this.getValue() == null )
		{
			return 0;
		}
		if( this.getValue() == null )
		{
			return -1;
		}
		if( target.getValue() == null )
		{
			return 1;
		}
		
		if( target.getValue().isEarlier(this.getValue()) )
		{
			return -1;
		}
		
		if( this.getValue().isEarlier(target.getValue()) )
		{
			return 1;
		}
		
		return 0;
	}

}
