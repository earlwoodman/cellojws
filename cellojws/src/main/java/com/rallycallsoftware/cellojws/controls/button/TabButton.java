package com.rallycallsoftware.cellojws.controls.button;

import java.awt.Color;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Tab;
import com.rallycallsoftware.cellojws.controls.TabContainer;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class TabButton extends BasicButton
{
	private Tab tab;
	
	private TabContainer tabContainer;
	
	public TabButton(final AbsDims dim, final String text, final CommandToken<?> token, final Tab tab, final TabContainer container)
	{
		super(dim, text, token, TabButtonType.getInstance());
		
		this.tab = tab;
		
		this.tabContainer = container;
	}

	public Tab getTab()
	{
		return tab;
	}

	public void setTab(Tab tab)
	{
		this.tab = tab;
	}

	public TabContainer getTabContainer()
	{
		return tabContainer;
	}

	public void setTabContainer(TabContainer tabContainer)
	{
		this.tabContainer = tabContainer;
	}

	@Override
	public void render(Graphics graphics, boolean mousedown)
	{
		super.render(graphics, mousedown);
		
		final AbsDims dims = getScreenDims();
		
		graphics.drawLine(dims.left, dims.bottom, dims.left, dims.top, Color.darkGray);
		graphics.drawLine(dims.left, dims.top, dims.right, dims.top, Color.darkGray);
		graphics.drawLine(dims.right, dims.top, dims.right, dims.bottom, Color.darkGray);
	}

}
