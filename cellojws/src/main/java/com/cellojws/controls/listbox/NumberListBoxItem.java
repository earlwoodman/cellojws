package com.cellojws.controls.listbox;

import com.cellojws.adapter.Graphics;
import com.cellojws.general.core.Environment;
import com.cellojws.windowing.WindowManager;

public class NumberListBoxItem implements ListBoxItem 
{
	
	public static final ListBoxItem Blank = new NumberListBoxItem(null);
	
	private Integer value; 
	
	public NumberListBoxItem(Integer value) 
	{
		this.value = value;
	}
	
	public Integer getValue() 
	{
		return value;
	}

	public void setValue(Integer value) 
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
		if( getValue() != null )
		{
			return Integer.valueOf(getValue()).toString();
		}
		
		return "";
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
		
		final NumberListBoxItem target = (NumberListBoxItem)listBoxItem;
		
		if( target.getValue() == null && this.getValue() == null ) 
		{
			return 0;
		}
		
		if( target.getValue() == null )
		{
			return -1;
		}
		else if( this.getValue() == null )
		{
			return 1;
		}
		
		if( target.getValue() < this.getValue() ) 
		{
			return -1;
		}
		else if( target.getValue() > this.getValue() )
		{
			return 1;
		}
		
		return 0;
	
	}


}
