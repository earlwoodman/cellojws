package com.rallycallsoftware.cellojws.controls;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class PopupMenu<T> extends Control implements TransientControl
{

	private List<PopupMenuItem<T>> items = new ArrayList<>();

	private PopupMenuItem<T> lastMousedOver;
	
	private FontInfo fontInfo;
	
	private FontInfo mousedOverFont;
	
	public PopupMenu()
	{
		fontInfo = Environment.getMenuFontInfo();
		mousedOverFont = fontInfo.makeCopy();
		
		mousedOverFont.setTextColor(Color.white);
		mousedOverFont.setDropShadow(Color.white.darker());
	}
	
	public void addItem(final PopupMenuItem<T> item)
	{
		items.add(item);
	}
	
	public void reset()
	{
		items = new ArrayList<>();
	}
	
	@Override
	public void render(Graphics graphics, boolean mousedown)
	{		
		super.render(graphics, mousedown);
				
		final int itemHeight = getItemHeight();
		final int height = itemHeight * items.size();
		
		final AbsDims dims = getScreenDims().makeCopy();
		dims.bottom = dims.top + height;
		graphics.drawGradientRect(Environment.getWindowingSystemLight(), Environment.getWindowingSystemDark(), dims, Direction.Downward);
		
		int i = 0;
		for( final PopupMenuItem<T> item : items )
		{
			final AbsDims itemDims = new AbsDims();
			itemDims.left = dims.left;
			itemDims.right = dims.right;
			itemDims.top = dims.top + (i++ * itemHeight); 
			render(graphics, item, itemDims);
		}
		
	}

	private int getItemHeight() 
	{
		return Control.getStdGap() + WindowManager.getTextHeight(fontInfo) + Control.getStdGap();		
	}

	private void render(final Graphics graphics, final PopupMenuItem<T> item, final AbsDims dims) 
	{
		graphics.setFontInfo(fontInfo);
		if( lastMousedOver == item )
		{
			graphics.setFontInfo(mousedOverFont);
		}
		
		graphics.drawText(" " + item.getMessage() + " ", dims.left + Control.getStdGap(), dims.top + Control.getStdGap());
	}

	@Override
	public boolean doSpecialMoveActions(int x, int y) 
	{
		boolean ret = super.doSpecialMoveActions(x, y);
		
		int item = y / getItemHeight();
		if( item >= 0 && item < items.size() )
		{
			lastMousedOver = items.get(item);
		}
		
		return ret;
	}

	public String getLongestMessage(final FontInfo fontInfo) 
	{
		final Comparator<PopupMenuItem<T>> comp = (p1, p2) -> Integer.compare( 
				WindowManager.getTextWidth(p1.getMessage(), fontInfo), 
				WindowManager.getTextWidth(p2.getMessage(), fontInfo));
		
		return items.stream().max(comp).get().getMessage(); 
	}
	
	@Override
	public CommandToken<?> getToken() 
	{
		if( lastMousedOver != null )
		{
			return lastMousedOver.getToken();
		}
		
		return null;
	}

	public void configDims(final Control parent) 
	{
		final AbsDims parentDims = parent.getScreenDims();
						
		final Point mousedown = Environment.getEnvironment().getWindowManager().getLastMouseDown();
		final String message = " " + getLongestMessage(Environment.getMenuFontInfo()) + " ";
		final int buffer = WindowManager.getTextWidth("  ", Environment.getMenuFontInfo());
		setDimensions(
				new AbsDims(
						mousedown.x - parentDims.left, 
						mousedown.y - parentDims.top, 
						mousedown.x - parentDims.left + buffer + Math.round(WindowManager.getTextWidth(message, Environment.getMenuFontInfo())), 
						mousedown.y - parentDims.top + items.size() * getItemHeight()));
	}

	public void configDimsBelow(final Control parent) 
	{
		final AbsDims parentDims = parent.getScreenDims();
						
		final String message = " " + getLongestMessage(Environment.getMenuFontInfo()) + " ";
		final int buffer = WindowManager.getTextWidth("  ", Environment.getMenuFontInfo());
		setDimensions(
				new AbsDims(
						parentDims.left, 
						parentDims.bottom, 
						parentDims.left + buffer + Math.round(WindowManager.getTextWidth(message, Environment.getMenuFontInfo())), 
						parentDims.bottom + items.size() * getItemHeight()));
	}

	public List<PopupMenuItem<T>> getItems() 
	{
		return items;
	}

	public void createMenuItem(String message, final Consumer<CommandToken<T>> action, T payload)
	{
		final PopupMenuItem<T> item = new PopupMenuItem<>();
		item.setMessage(message);
		final CommandToken<T> token = new CommandToken<T>(action, payload);
		item.setToken(token);
		items.add(item);
	}

	@Override
	public void hidden()
	{
		
	}

}
