package com.rallycallsoftware.cellojws.controls.button;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.VerticalScrollBar;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;

public class UpScrollButtonType extends FixedSizeButtonType
{
	private Polygon savedPolygon;

	private Polygon savedHighlightPolygon;
	
	private AbsDims savedDims;

	private AbsDims savedHighlightDims;

	private Color normalHighlightColour = null;
	
	private Color mouseoverHighlightColour = null;
	
	private Color mouseDownHighlightColour = null;

	private UpScrollButtonType(){}
		
	public static UpScrollButtonType getInstance()
	{
		return new UpScrollButtonType();
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
		
		points.add(new Point(dims.left, dims.bottom));
		points.add(new Point(centerX, dims.top));
		points.add(new Point(dims.right, dims.bottom));
		
		int[] xPoints = Arrays.stream(points.stream().mapToDouble(Point::getX).toArray()).mapToInt(x -> (int)Math.round(x)).toArray();
		int[] yPoints = Arrays.stream(points.stream().mapToDouble(Point::getY).toArray()).mapToInt(x -> (int)Math.round(x)).toArray();
		
		savedPolygon = new Polygon(xPoints, yPoints, points.size());
		return savedPolygon;		
	}

	private Polygon highlightPolygonFromDims(AbsDims dims) 
	{
		// Create a polygon of a down pointing arrowhead
		if( dims.equals(savedHighlightDims) )
		{
			return savedHighlightPolygon;
		}

		savedHighlightDims = dims.makeCopy();

		final int centerX = dims.getAbsWidth() / 2 + dims.left;
		final List<Point> points = new ArrayList<>();
		
		final int halfWidth = centerX - dims.left;
		final int leftStop = dims.left + Math.round(halfWidth * (1 - highlightPercentage));
		final int rightStop = dims.right - Math.round(halfWidth * (1 - highlightPercentage));
		
		points.add(new Point(centerX, dims.top));
		points.add(new Point(leftStop, dims.top + Math.round(dims.getAbsHeight() * highlightPercentage)));
		points.add(new Point(rightStop, dims.top + Math.round(dims.getAbsHeight() * highlightPercentage)));
				
		int[] xPoints = Arrays.stream(points.stream().mapToDouble(Point::getX).toArray()).mapToInt(x -> (int)Math.round(x)).toArray();
		int[] yPoints = Arrays.stream(points.stream().mapToDouble(Point::getY).toArray()).mapToInt(x -> (int)Math.round(x)).toArray();
		
		savedHighlightPolygon = new Polygon(xPoints, yPoints, points.size());
		return savedHighlightPolygon;		
	}


	@Override
	public void renderMousedown(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);
		
		if( mouseDownHighlightColour == null )
		{
			mouseDownHighlightColour = produceHighlightColour(colour);
		}
		
		graphics.drawPolygon(highlightPolygonFromDims(dims), mouseDownHighlightColour);
	}

	@Override
	public void renderNormal(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);
		
		if( normalHighlightColour == null )
		{
			normalHighlightColour = produceHighlightColour(colour);
		}
		
		graphics.drawPolygon(highlightPolygonFromDims(dims), normalHighlightColour);

	}

	@Override
	public void renderMouseover(final Graphics graphics, final AbsDims dims, final Color colour)
	{
		graphics.drawPolygon(polygonFromDims(dims), colour);

		if( mouseoverHighlightColour == null )
		{
			mouseoverHighlightColour = produceHighlightColour(colour);
		}
		
		graphics.drawPolygon(highlightPolygonFromDims(dims), mouseoverHighlightColour);

	}
}
