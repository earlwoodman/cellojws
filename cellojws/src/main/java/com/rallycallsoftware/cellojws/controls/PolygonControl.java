package com.rallycallsoftware.cellojws.controls;

import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class PolygonControl extends Control
{

	private Polygon polygon;
	
	public PolygonControl(final CommandToken token_)
	{
		setToken(token_);
	}
	
	public PolygonControl()
	{
		
	}

	public PolygonControl(final CommandToken token_, int[] arg1, int[] arg2, int arg3)
	{
		setToken(token_);
		setPolygon(arg1, arg2, arg3);
	}
	
	private void setPolygon(int[] arg1, int[] arg2, int arg3)
	{
		polygon = new Polygon(arg1, arg2, arg3);
	}

	@Override
	public void render(Graphics graphics, boolean mousedown)
	{
		
	}

	@Override
	public boolean doSpecialClickActions(int x, int y)
	{
		if( polygon != null )
		{
			final Polygon test = copyAndResizePolygon(polygon);
			if( test != null && test.contains(x + test.getBounds().x, y + test.getBounds().y) )
			{
				return true;
			}
		}
		
		return false;
	}

	private Polygon copyAndResizePolygon(final Polygon polygon)
	{
		final int[] xPoints = new int[polygon.npoints];
		final int[] yPoints = new int[polygon.npoints];
		
		final Control parent = getParent();
		for( int i = 0; i < polygon.npoints; i++ )
		{
			xPoints[i] = (int)(polygon.xpoints[i] * parent.getxRatio()); 
			yPoints[i] = (int)(polygon.ypoints[i] * parent.getyRatio());
		}
				
		final Polygon ret = new Polygon(xPoints, yPoints, polygon.npoints);
		setDimensions(new AbsDims(ret.getBounds()));
		return ret;
	}

	@Override
	public CommandToken processKeyPress(KeyEvent keyEvent)
	{
		return null;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent)
	{
		
	}
		
}
