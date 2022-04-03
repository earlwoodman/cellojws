package com.rallycallsoftware.cellojws.windowing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;

public class Hourglass 
{	
	private long start = 0;

	private AbsDims dims = null;

	private static final int circles = 5;
		
	private static final int timeBetweenCircles = 200;
	
	private List<AbsDims> circlesDims = new ArrayList<>(); 
	
	private List<AbsDims> circlesShadowsDims = new ArrayList<>();
	
	private static final int circleSize = Environment.smallGap();
	
	private static final Color normalCircleColour = Environment.getEnvironment().getNormalColour();
	
	private static final Color highlightCircleColour = Environment.getEnvironment().getMouseoverColour();
			
	private Graphics graphics;
	
	public Hourglass(final Graphics graphics, final int size, final int screenWidth, final int screenHeight) 
	{
		this.graphics = graphics;
		dims = new AbsDims(
				(screenWidth - size) / 2,
				(screenHeight - size) / 2,
				(screenWidth - size) / 2 + size,
				(screenHeight - size) / 2 + size
				);
		
		final int gap = Control.getStdGap();
		final int circleGap = (dims.getAbsWidth() - gap * 2 - circleSize * circles) / (circles - 1);
		
		for( int i = 0; i < circles; i++ )
		{
			// Because this is not a control, I have to manually add in the parent dims
			final AbsDims circleDims = new AbsDims(
					dims.left + gap + (i * (circleSize + circleGap)),
					dims.top + (dims.getAbsHeight() - circleSize) / 2,
					dims.left + gap + (i * (circleSize + circleGap)) + circleSize,
					dims.top + (dims.getAbsHeight() - circleSize) / 2 + circleSize
					);
			circlesDims.add(circleDims);
			final AbsDims shadowDims = circleDims.makeCopy();
			shadowDims.move(Environment.verySmallGap(), Environment.verySmallGap());
			circlesShadowsDims.add(shadowDims);
		}
		
		start = System.currentTimeMillis();
	}

	public void render() 
	{
		long currentTime = System.currentTimeMillis();
		
		for( int i = 0; i < circles; i++ )
		{
			long circleToHighlight = (currentTime - start) / timeBetweenCircles;
			if( circleToHighlight > circles - 1 )
			{
				start = System.currentTimeMillis();
				currentTime = start;
				circleToHighlight = 0;
			}
			renderCircle(circleToHighlight == i, circlesDims.get(i), circlesShadowsDims.get(i));
		}
	}

	private void renderCircle(final boolean highlight, final AbsDims dims, final AbsDims shadow) 
	{
		graphics.drawSolidCircle(shadow, Color.black);
		graphics.drawSolidCircle(dims, highlight ? highlightCircleColour : normalCircleColour);
	}	

}
