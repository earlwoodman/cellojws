package com.cellojws.controls.listbox;

import com.cellojws.general.Formatter;

public class CurrencyListBoxItem extends StringListBoxItem
{
	
	private Integer value;
	
	public CurrencyListBoxItem(int value)
	{
		super();
	
		setValue(value);			
	}

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		this.value = value;
		setCaption(Formatter.dollarsMillions(value));
	}
	
	@Override
	public int compare(ListBoxItem listBoxItem) 
	{ 
		if( listBoxItem == null )
		{
			return 1;
		}
		
		final CurrencyListBoxItem target = (CurrencyListBoxItem)listBoxItem;
		
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
