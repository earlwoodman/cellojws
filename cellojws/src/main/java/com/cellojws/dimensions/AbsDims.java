/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.dimensions;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.cellojws.general.image.Image;
import com.cellojws.logging.WorkerLog;

public class AbsDims implements Dimensions, Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 24L;
    
    public int left = 0;
    public int top = 0;
    public int right = 0;
    public int bottom = 0;

    public AbsDims(final int left_, final int top_, final int right_, final int bottom_)
    {
        left = left_;
        top = top_;
        right = right_;
        bottom = bottom_;
    }
    
    public AbsDims(final Rectangle bounds)
	{
		left = bounds.x;
		top = bounds.y;
		right = bounds.x + bounds.width;
		bottom = bounds.y + bounds.height;
	}
    
    public AbsDims()
    {
    	
    }

    @Override
	public AbsDims makeCopy() 
    {
    	return new AbsDims(left, top, right, bottom);
	}

	public String toString()
    {
        return "Left: " + left + " // Top: " + top + " // Right: " + right + " // Bottom: " + bottom;
    }

	@Override
    public int getAbsHeight()
    {
        return bottom - top;
    }
    
	@Override	
    public int getAbsWidth()
    {
        return right - left;
    }

	@Override
	public void shrink(final int i) 
	{
		left += i;
		right -= i;
		top += i;
		bottom -= i;
	}

	@Override
	public void move(final int x, final int y)
	{
		left += x;
		right += x;
		top += y;
		bottom += y;
	}
	
	@Override
	public Rectangle getRect()
	{
		final Rectangle rect = new Rectangle();
		
		rect.height = bottom - top;
		rect.width = right - left;
		rect.x = left;
		rect.y = top;
		
		return rect;
	}

	@Override
	public AbsDims absoluteify()
	{
		return this;
	}

	public AbsDims shift(final AbsDims dimensions)
	{
		final AbsDims ret = new AbsDims();
		
		ret.left = left + dimensions.left;
		ret.right = right + dimensions.left;
		ret.top = top + dimensions.top;
		ret.bottom = bottom + dimensions.top;
		
		return ret;
	}

	public AbsDims shiftBack(final AbsDims dimensions)
	{
		final AbsDims ret = new AbsDims();
		
		ret.left = left - dimensions.left;
		ret.right = right - dimensions.left;
		ret.top = top - dimensions.top;
		ret.bottom = bottom - dimensions.top;
		
		return ret;
	}

	public AbsDims getTopHalf()
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = (bottom - top) / 2 + top;
		ret.right = right;
		
		return ret;
	}

	public AbsDims getBottomHalf()
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = (bottom - top) / 2 + top;
		ret.bottom = bottom;
		ret.right = right;
		
		return ret;
		
	}

	public AbsDims getLeftHalf() 
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = bottom;
		ret.right = (right - left) / 2 + left;
		
		return ret;			
	}

	public AbsDims getRightHalf() 
	{
		final AbsDims ret = new AbsDims();

		ret.left = (right - left) / 2 + left;
		ret.top = top;
		ret.bottom = bottom;
		ret.right = right;
		
		return ret;			
	}

	public AbsDims getTopThird()
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = (bottom - top) / 3 + top;
		ret.right = right;
		
		return ret;
	}

	public AbsDims getTop40Percent()
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = Math.round((bottom - top) * 0.4F + top);
		ret.right = right;
		
		return ret;
	}


	public AbsDims getTopQuarter()
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = (bottom - top) / 4 + top;
		ret.right = right;
		
		return ret;
	}

	public AbsDims getBottomThreeQuarters()
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = (bottom - top) / 4 + top;
		ret.bottom = bottom;
		ret.right = right;
		
		return ret;
	}
	
	public AbsDims getBottomTwoThirds() 
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = (bottom - top) / 3 + top;
		ret.bottom = bottom;
		ret.right = right;
		
		return ret;
	}

	public AbsDims getBottom60Percent() 
	{
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = Math.round((bottom - top) * 0.4F + top);
		ret.bottom = bottom;
		ret.right = right;
		
		return ret;
	}

	/**
	 * Centers the given image on the given point within the current dims
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @return
	 */
	public AbsDims centerOn(final Image image, final Point point)
	{
		final AbsDims ret = new AbsDims();
		
		if( point != null && image != null )
		{
			ret.left = left + (point.x - image.getWidth() / 2);
			ret.right = ret.left + image.getWidth();
			ret.top = top + (point.y - image.getHeight() / 2);
			ret.bottom = ret.top + image.getHeight();
		}
		else
		{
			if( point == null )
			{
				WorkerLog.error("AbsDims::centerOn no point specified.");
			}
			if( image == null )
			{
				WorkerLog.error("AbsDims::centerOn no image specified.");
			}
		}
		
		return ret;
	}
	
	/**
	 * Centers the image on the dims and returns new dims.
	 * 
	 * @param image
	 * @return
	 */
	public AbsDims center(final Image image)
	{
		return center(image, true, true);
	}
	
	/**
	 * Centers the image (optional in each direction) on the 'this' dims 
	 * 
	 * @param image
	 * @param centerX
	 * @param centerY
	 * @return
	 */
	public AbsDims center(final Image image, final boolean centerX, final boolean centerY)
	{
		return center(image.getWidth(), image.getHeight(), centerX, centerY);
	}
	
	public AbsDims center(final int width, final int height, final boolean centerX, final boolean centerY)
	{
		final AbsDims ret = new AbsDims();
		
		if( centerX )
		{		
			ret.left = left + (getAbsWidth() - width) / 2;
			ret.right = left + (getAbsWidth() - width) / 2 + width;
		}
		else
		{
			ret.left = left + 0;
			ret.right = left + width;
		}
			
		if( centerY )
		{		
			ret.top = top + (getAbsHeight() - height) / 2;
			ret.bottom = top + (getAbsHeight() - height) / 2 + height;
		}
		else
		{
			ret.top = top + 0;
			ret.bottom = top + height;
		}
		
		return ret;
	}

	/**
	 * Joins this with the target, takes the overall bounding box.  
	 * @param target
	 */
	public void merge(AbsDims target)
	{
		left = left < target.left ? left : target.left;
		top = top < target.top ? top : target.top;
		right = right > target.right ? right: target.right;
		bottom = bottom > target.bottom ? bottom : target.bottom;
	}

	/**
	 * Expands the dimensions by the given amount horizontally and vertical, keeping
	 * the same center
	 * 
	 * @param i
	 * @param j
	 */
	public void expand(int i, int j)
	{
		left -= i;
		top -= j;
		right += i;
		bottom += j;
	}

	public AbsDims takeHorizontalSlice(int i, int length)
	{
		final AbsDims ret = new AbsDims();
		
		ret.left = this.left;
		ret.right = this.right;
				
		final int sliceHeight = getAbsHeight() / length;
		
		ret.top = this.top + (sliceHeight * i);
		ret.bottom = ret.top + sliceHeight;
		
		return ret;
	}

	@Override
	public boolean equals(Object obj)
	{
		if( obj == null )
		{
			return false;
		}
		
		if( !AbsDims.class.isAssignableFrom(obj.getClass()) )
		{
			return false;
		}
		
		final AbsDims other = (AbsDims)obj;
		return this.left == other.left 
			&& this.right == other.right
			&& this.top == other.top
			&& this.bottom == other.bottom;
		
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(left, right, top, bottom);
	}

	public Point getCenterPoint()
	{
		return new Point(
				(this.left + this.right) / 2,
				(this.top + this.bottom) / 2);
	}

	/** Creates an AbsDims object centered at the given point with width and 
	 * height specified.
	 * 
	 * @param point
	 * @param width
	 * @param height
	 * @return
	 */
	public AbsDims centerOn(Point point, int width, int height)
	{
		if( width < 0 || height < 0 )
		{
			throw new IllegalArgumentException("Can't create an AbsDims with negative size.");
		}
		
		return new AbsDims(
				point.x - width / 2,
				point.y - height / 2,
				point.x + width / 2,
				point.y + height / 2
				);
	}

	public List<AbsDims> splitVerticalSlices(final int count) 
	{
		final List<AbsDims> slices = new ArrayList<>();
		
		final int width = getAbsWidth() / count;
		
		for( int i = 0; i < count; i++ ) 
		{
			final AbsDims slice = new AbsDims();
			slice.top = this.top;
			slice.bottom = this.bottom;
			slice.left = i * width; 
			slice.right = slice.left + width;
			slices.add(slice);
		}
		
		return slices;
	}

	public boolean contains(int x, int y) 
	{
		return x >= left && x <= right && y >= top && y <= bottom;
	}

	public AbsDims zero() 
	{
		final AbsDims ret = new AbsDims();
		
		ret.left = 0;
		ret.top = 0;
		ret.right = getAbsWidth();
		ret.bottom = getAbsHeight();
		
		return ret;
	}	
	
}
