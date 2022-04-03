package com.rallycallsoftware.cellojws.controls;

import java.awt.Color;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class ImageFader extends Control 
{		
	private boolean loop;
	
	private Mode mode;
	
	private long currentModeMillis = 0;
	
	private CompletionCallback callback;

	private long startingTick;
	
	public ImageFader(final FaderDetailsBean details_, final AbsDims dims, final CommandToken<?> token, 
			final CompletionCallback callback_)
	{
		super(dims, token);
		
		mode = Mode.Invisible;
		Mode.FadeIn.setMillis(details_.getFadeInMillis());
		Mode.FadeOut.setMillis(details_.getFadeOutMillis());
		Mode.Opaque.setMillis(details_.getOpaqueMillis());
		Mode.Invisible.setMillis(details_.getInvisibleMillis());
		
		loop = details_.isLoop();
		
		callback = callback_;
		
		startingTick = System.currentTimeMillis();
	}


	@Override
	public void render(Graphics graphics, boolean mousedown) 
	{
		final long latestTick = System.currentTimeMillis();

		currentModeMillis = latestTick - startingTick;
		
		final float percentage = (float)( currentModeMillis ) / mode.getMillis();
		
		if( mode == Mode.FadeIn )
		{
			WorkerLog.info("Fade in");
			graphics.drawImage(getImage().getBufferedImage(), getDimensions(), makeComposite(percentage));
		}
		else if( mode == Mode.FadeOut )
		{
			WorkerLog.info("Fade out");
			graphics.drawImage(getImage().getBufferedImage(), getDimensions(), makeComposite(1 - percentage));
		}
		else if( mode == Mode.Opaque )
		{
			WorkerLog.info("Opaque");
			graphics.drawImage(getImage().getBufferedImage(), getDimensions());
		}
		else
		{
			WorkerLog.info("Invisible");
		}

		if( percentage > 1F )
		{
			currentModeMillis = 0;
			advanceMode();
			if( mode == null )
			{
				// Make sure there's no residual fade out, since percentage steps may be fairly coarse-grained
				graphics.drawSolidRect(getDimensions(), Color.BLACK);
				callback.completed();
			}
			startingTick = System.currentTimeMillis();
		}
		
	}
	
	private void advanceMode() 
	{
		if( mode == Mode.Invisible )
		{
			mode = Mode.FadeIn;
		}
		else if( mode == Mode.FadeIn )
		{
			mode = Mode.Opaque;
		}
		else if( mode == Mode.Opaque )
		{
			mode = Mode.FadeOut;
		}
		else if( mode == Mode.FadeOut )
		{
			if( loop )
			{
				mode = Mode.Invisible;
			}
			else
			{
				mode = null;
			}
		}
		
	}

	private enum Mode
	{
		
		FadeIn,
		Opaque,
		FadeOut,
		Invisible;
		
		private int millis;

		public int getMillis() 
		{
			return millis;
		}

		public void setMillis(int millis) 
		{
			this.millis = millis;
		}		
		
	}
	
}
