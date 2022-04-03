package com.cellojws.controls;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.Random;
import com.cellojws.general.core.Environment;
import com.cellojws.windowing.WindowManager;

public class ConfettiControl extends Control 
{
		
	private int screenHeight;

	private long latestTime;
	
	private static final int frameRate = 30; 
	
	private static final int millisBetweenFrames = 1000 / frameRate;
	
	private List<Confetti> confettis = new ArrayList<>();
	
	private long renderCount;
	
	private static final int size = Environment.verySmallGap();
	
	public ConfettiControl(final AbsDims dims)	
	{
		super(dims);
		
		final WindowManager windowManager = Environment.getEnvironment().getWindowManager();
		
		screenHeight = windowManager.getScreenHeight();

		latestTime = System.currentTimeMillis();
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) 
	{
		super.render(graphics, mousedown);
		renderCount++;
		int max = Random.getNormalRightHandSide(1, 100, 7) + (renderCount % 50 == 0 ? 25 : 0);
		final int spew = 2;
		for( int i = 0; i < max; i++ )
		{
			final Confetti confetti = new Confetti();
			confettis.add(confetti);
			do
			{
				confetti.setxAmt(Random.getRandIntInclusive(-20, 20));
			}
			while(confetti.getxAmt() == 0);
			
			confetti.setyAmt(Random.getRandIntInclusive(-50, -40));
			confetti.setSpew(spew);
			
			final int r = Random.getRandIntInclusive(0, 255);
			final int g = Random.getRandIntInclusive(0, 255);
			final int b = Random.getRandIntInclusive(0, 255);
			confetti.setColor(new Color(r, g, b));
			final int left = getScreenDims().left;
			final int top = getScreenDims().top;
			confetti.setDims(new AbsDims(left - size / 2,
										top - size / 2,
										(left + size / 2) + size,
										(top + size / 2) + size));
		}
		
		if (System.currentTimeMillis() > latestTime + millisBetweenFrames)
		{
			latestTime = System.currentTimeMillis();
			 
			for( final Confetti c : confettis )
			{
				c.move();
				graphics.drawSolidRect(c.getDropShadowDims(), Color.black);
				graphics.drawSolidRect(c.getDims(), c.getColor());
			}
			
		}
		
		boolean removing = true;
		while(removing)
		{
			removing = false;
			for( final Confetti confetti : confettis )
			{				
				if( confetti.getDims().top > screenHeight )
				{
					confettis.remove(confetti);
					removing = true;
					break;
				}
			}
		}
	}
		
	private class Confetti 
	{
		private Color color;
		
		private AbsDims dims;

		private AbsDims dropShadowDims;

		private int xAmt;
							
		private int yAmt;
		
		private int spew;
				
		public Confetti() 
		{
			
		}
		
		public void move() 
		{
			yAmt += spew; 
			dims.move(xAmt, yAmt);
			dropShadowDims.move(xAmt, yAmt);
		}

		public int getxAmt() 
		{
			return xAmt;
		}

		public void setxAmt(int xAmt) 
		{
			this.xAmt = xAmt;
		}

		public void setyAmt(int yAmt) 
		{
			this.yAmt = yAmt;
		}

		public Color getColor() 
		{
			return color;
		}
		
		public void setColor(Color color) 
		{
			this.color = color;
		}

		public AbsDims getDims() 
		{
			return dims;
		}

		public void setDims(AbsDims dims) 
		{
			this.dims = dims;
			this.dropShadowDims = dims.makeCopy();
			this.dropShadowDims.move(Environment.veryVerySmallGap(), Environment.veryVerySmallGap());
		}

		public AbsDims getDropShadowDims() 
		{
			return dropShadowDims;
		}

		public void setSpew(int spew)
		{
			this.spew = spew;
		}
		
	}
	
	
}
