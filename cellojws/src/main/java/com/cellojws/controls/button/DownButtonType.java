package com.cellojws.controls.button;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.core.Environment;

public class DownButtonType extends FixedSizeButtonType
{

	private Polygon savedPolygon;

	private Polygon savedHighlightPolygon;
	
	private AbsDims savedDims;

	private AbsDims savedHighlightDims;
	
	private static Integer size = null;
	
	private Color normalHighlightColour = null;
	
	private Color mouseoverHighlightColour = null;
	
	private Color mouseDownHighlightColour = null;
	
	private DownButtonType()
	{
		if( size == null )
		{
			size = Environment.tinyGap();
		}
	}
	
	public static DownButtonType getInstance()
	{
		return new DownButtonType();
	}
	
	@Override
	public int getWidth()
	{
		return size;
	}

	@Override
	public int getHeight()
	{
		return size;
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
		
		points.add(new Point(dims.left, dims.top));
		points.add(new Point(centerX, dims.bottom));
		points.add(new Point(dims.right, dims.top));
		
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
				
		final int leftStop = dims.left + Math.round((centerX - dims.left) * highlightPercentage);
		final int rightStop = dims.left + dims.getAbsWidth() - Math.round((centerX - dims.left) * highlightPercentage); 
		
		points.add(new Point(dims.left, dims.top));
		points.add(new Point(leftStop, Math.round(dims.getAbsHeight() * highlightPercentage) + dims.top));
		points.add(new Point(rightStop, Math.round(dims.getAbsHeight() * highlightPercentage) + dims.top));
		points.add(new Point(dims.right, dims.top));
		
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
