package com.cellojws.controls.listbox;

public class ClockListBoxItem extends StringListBoxItem 
{

	public static final ListBoxItem Blank = new ClockListBoxItem("");
		
	public ClockListBoxItem(String caption) 
	{
		setCaption(caption);
	}

	public ClockListBoxItem() 
	{

	}

	@Override
	public int compare(ListBoxItem listBoxItem) 
	{ 
		if( listBoxItem == null )
		{
			return 1;
		}
		
		final ClockListBoxItem target = (ClockListBoxItem)listBoxItem;
		
		if( target.getCaption() == "" && this.getCaption() == "" )
		{
			return 0;
		}
		
		if( target.getCaption() == "" )
		{
			return -1;
		}
		
		if( this.getCaption() == "" )
		{
			return 1;
		}
		
		final String[] targetParts = target.getCaption().split(":");
		final int targetMinutes = Integer.parseInt(targetParts[0]);
		final int targetSeconds = Integer.parseInt(targetParts[1]);
		final String[] thisParts = this.getCaption().split(":");
		final int thisMinutes = Integer.parseInt(thisParts[0]);
		final int thisSeconds = Integer.parseInt(thisParts[1]);
		
		if( targetMinutes < thisMinutes )
		{
			return -1;
		}
		if( targetMinutes > thisMinutes )
		{
			return 1;
		}
		
		if( targetSeconds < thisSeconds )
		{
			return -1;
		}
		if( targetSeconds > thisSeconds )
		{
			return 1;
		}
		
		return 0;
	}


}
