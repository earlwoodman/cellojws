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

public class BigUpScrollButtonType extends FixedSizeButtonType
{
	private AbsDims savedDims;
	
	private Polygon savedPolygon;

	private BigUpScrollButtonType(){}
		
	public static BigUpScrollButtonType getInstance()
	{
		return new BigUpScrollButtonType();
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
				  dims.bottom, 
				  dims.right,
				  dims.bottom, colour);
	}

	private Polygon polygonFromDims(AbsDims dims) 
	{
		// Create a polygon of an up pointing arrowhead
		if( dims.equals(savedDims) )
		{
			return savedPolygon;
		}

		savedDims = dims.makeCopy();

		final int centerX = dims.getAbsWidth() / 2 + dims.left;
		final List<Point> points = new ArrayList<>();
		
		points.add(new Point(dims.left, dims.bottom - Environment.veryVerySmallGap() * 2));
		points.add(new Point(centerX, dims.top));
		points.add(new Point(dims.right, dims.bottom - Environment.veryVerySmallGap() * 2));
		
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
				  dims.bottom, 
				  dims.right,
				  dims.bottom, colour);
	}

	@Override
	public void renderMouseover(final Graphics graphics, final AbsDims dims, final Color colour)
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);
		
		graphics.drawLine(dims.left,
				  dims.bottom, 
				  dims.right,
				  dims.bottom, colour);
	}

}
