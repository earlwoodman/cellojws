package com.cellojws.controls.button;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.VerticalScrollBar;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.core.Environment;

public class BigDownScrollButtonType extends FixedSizeButtonType
{
	private Polygon savedPolygon;
	
	private AbsDims savedDims;

	private BigDownScrollButtonType(){}
		
	public static BigDownScrollButtonType getInstance()
	{
		return new BigDownScrollButtonType();
	}
	
	@Override
	public int getWidth()
	{
		return VerticalScrollBar.getControlBoxHeight();
	}

	@Override
	public int getHeight()
	{
		return VerticalScrollBar.getControlBoxHeight();
	}


	@Override
	public void renderMousedown(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);
		
		graphics.drawLine(dims.left,
				  dims.top, 
				  dims.right,
				  dims.top, colour);
		}

	private Polygon polygonFromDims(AbsDims dims) 
	{
		// Create a polygon of a down pointing arrowhead
		if( dims.equals(savedDims) )
		{
			return savedPolygon;
		}
		
		savedDims = dims.makeCopy();
		
		final int centerX = dims.getAbsWidth() / 2 + dims.left;
		final List<Point> points = new ArrayList<>();
		
		points.add(new Point(dims.left, dims.top + Environment.veryVerySmallGap() * 2));
		points.add(new Point(centerX, dims.bottom));
		points.add(new Point(dims.right, dims.top + Environment.veryVerySmallGap() * 2));
		
		int[] xPoints = Arrays.stream(points.stream().mapToDouble(Point::getX).toArray()).mapToInt(x -> (int)Math.round(x)).toArray();
		int[] yPoints = Arrays.stream(points.stream().mapToDouble(Point::getY).toArray()).mapToInt(x -> (int)Math.round(x)).toArray();
		
		savedPolygon = new Polygon(xPoints, yPoints, points.size());
		return savedPolygon;
		
	}

	@Override
	public void renderNormal(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);
		
		graphics.drawLine(dims.left,
						  dims.top, 
						  dims.right,
						  dims.top, colour);
	}

	@Override
	public void renderMouseover(final Graphics graphics, final AbsDims dims, final Color colour)
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);
		
		graphics.drawLine(dims.left,
				  dims.top, 
				  dims.right,
				  dims.top, colour);
	}
}
