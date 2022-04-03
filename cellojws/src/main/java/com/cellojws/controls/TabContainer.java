package com.cellojws.controls;

import com.cellojws.dimensions.AbsDims;

public class TabContainer extends Container
{

	private Tab tab;
	
	public TabContainer(final AbsDims dim, final Tab tab)
	{
		super(dim, null);
		
		setTab(tab);
	}

	public Tab getTab()
	{
		return tab;
	}

	public void setTab(Tab tab)
	{
		this.tab = tab;
	}

}
