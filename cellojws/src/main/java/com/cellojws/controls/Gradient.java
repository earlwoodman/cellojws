package com.cellojws.controls;

import java.awt.Color;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.Random;
import com.cellojws.token.CommandToken;

public class Gradient extends Control
{

	private Color light;
	
	private Color dark;

	private long lastTick;
	
	private static final int frameRate = 36;
	private static final int msBetweenFrames = (int)(400F / frameRate); 
			
	private int topOrLeft;
	
	private static final int thicknessDefault = 280;
	
	private static final int offset = 400;
	
	private AbsDims screenDims;
	
	private AbsDims topHalfDims;
	
	private AbsDims bottomHalfDims;
	
	private AbsDims leftHalfDims;
	
	private AbsDims rightHalfDims;
	
	private int thickness = thicknessDefault;
	
	public int getThickness() {
		return thickness;
	}

	public void setThickness(int thickness) {
		this.thickness = thickness;
	}

	private Direction direction = Direction.Downward;
		
	public Gradient(final AbsDims dim, final Color dark, final Color light, final Control parent)
	{
		super(dim);
		
		init(dark, light, parent);
	}
	
	public Gradient(final AbsDims dim, final CommandToken<?> token_, final Color dark, final Color light, final Control parent)
    {
        super(dim, token_);
        
        init(dark, light, parent);
    }
    
	private void init(final Color dark, final Color light, final Control parent)
	{
		lastTick = System.currentTimeMillis();
		
		topOrLeft = parent.getScreenDims().top;
		
		screenDims = parent.getScreenDims();
		topHalfDims = screenDims.getTopHalf();
		bottomHalfDims = screenDims.getBottomHalf();
		leftHalfDims = screenDims.getLeftHalf();
		rightHalfDims = screenDims.getRightHalf();

		this.dark = dark;
		this.light = light;
	}
	
	@Override
	public void render(final Graphics graphics, final boolean mousedown)
	{
		final long latestTick = System.currentTimeMillis();
		
		if( latestTick - lastTick > msBetweenFrames )
		{
			if( direction == Direction.Downward )
			{
				topOrLeft++;
				if( topOrLeft == getParent().getDimensions().getAbsHeight() )
				{
					topOrLeft = -thickness - Random.getRandIntInclusive(0, offset);
				}
			}
			else if( direction == Direction.Upward )
			{
				topOrLeft--;
				if( topOrLeft == -thickness )
				{
					topOrLeft = getParent().getDimensions().getAbsHeight() + Random.getRandIntInclusive(0, offset);
				}
			}
			else if( direction == Direction.Leftward )
			{
				topOrLeft--;
				if( topOrLeft == -thickness )
				{
					topOrLeft = getParent().getDimensions().getAbsWidth() + Random.getRandIntInclusive(0, offset);
				}
			}
			else if( direction == Direction.Rightward )
			{
				topOrLeft++;
				if( topOrLeft == getParent().getDimensions().getAbsWidth() )
				{
					topOrLeft = -thickness - Random.getRandIntInclusive(0, offset);
				}
			}
			
			lastTick = System.currentTimeMillis();
						
			if( direction == Direction.Downward || direction == Direction.Upward )
			{
				topHalfDims.top = topOrLeft;
				topHalfDims.bottom = topOrLeft + thickness / 2;
				bottomHalfDims.top = topHalfDims.bottom;
				bottomHalfDims.bottom = bottomHalfDims.top + thickness / 2;
			}
			else if( direction == Direction.Leftward || direction == Direction.Rightward )
			{
				leftHalfDims.left = topOrLeft;
				leftHalfDims.right = topOrLeft + thickness / 2;
				rightHalfDims.left = leftHalfDims.right;
				rightHalfDims.right = rightHalfDims.left + thickness / 2;
			}
		}

		if( direction == Direction.Downward || direction == Direction.Upward )
		{
			graphics.drawGradientRect(dark, light, bottomHalfDims, direction);
			graphics.drawGradientRect(light, dark, topHalfDims, direction);
		}
		else if( direction == Direction.Leftward || direction == Direction.Rightward )
		{
			graphics.drawGradientRect(dark, light, rightHalfDims, direction);
			graphics.drawGradientRect(light, dark, leftHalfDims, direction);			
		}
		
	}
		
	public Direction getDirection()
	{
		return direction;
	}

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}


}
