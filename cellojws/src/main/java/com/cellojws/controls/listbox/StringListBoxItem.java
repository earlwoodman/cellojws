package com.cellojws.controls.listbox;

import com.cellojws.adapter.Graphics;
import com.cellojws.general.core.Environment;
import com.cellojws.windowing.WindowManager;

public class StringListBoxItem implements ListBoxItem 
{

	public static final ListBoxItem Blank = new StringListBoxItem("");
	
	private String caption;
	
	public StringListBoxItem(String caption) 
	{
		this.caption = caption;
	}

	public StringListBoxItem() 
	{

	}

	public String getCaption() 
	{
		return caption;
	}

	public void setCaption(String caption) 
	{
		this.caption = caption;
	}

	@Override
	public int getHeight() 
	{
		return WindowManager.getTextHeight(Environment.getListBoxFontInfo()); 
	}
	
	@Override
	public String toString()
	{
		return getCaption();
	}

	@Override
	public void draw(final Graphics graphics, final int leftDraw, final int top, final int widthMax) 
	{
		graphics.drawText(toString(), leftDraw, top, widthMax);	
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
		
		final StringListBoxItem target = (StringListBoxItem)listBoxItem;
		
		return target.getCaption().toUpperCase().compareTo(this.getCaption().toUpperCase());
		
	}


}
