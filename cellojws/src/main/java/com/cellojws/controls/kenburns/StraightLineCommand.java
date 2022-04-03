package com.cellojws.controls.kenburns;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.image.Image;

public class StraightLineCommand implements KBCommand
{
	private Image image;
	
	private AbsDims start;
	
	private AbsDims finish;
	
	private long elapsed;

	private BufferedImage startImage;
	
	private BufferedImage finishImage;

	private long startTime;
	
	private long finishTime;

	private Control owner;		
	
	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime) 
	{
		this.startTime = startTime;
	}

	public long getFinishTime() 
	{
		return finishTime;
	}

	public void setFinishTime(long finishTime) 
	{
		this.finishTime = finishTime;
	}

	public Image getImage() 
	{
		return image;
	}

	public BufferedImage getFinishImage() 
	{
		if( finishImage == null )
		{
			finishImage = scaleImage(getImage().getBufferedImage().getSubimage(
				finish.left, finish.top, finish.getAbsWidth(), finish.getAbsHeight()), 
				owner.getScreenDims().getAbsWidth(), owner.getScreenDims().getAbsHeight());
		}
		
		return finishImage;
	}

	public BufferedImage getStartImage() 
	{
		if( startImage == null )
		{
			startImage = scaleImage(getImage().getBufferedImage().getSubimage(
				start.left, start.top, start.getAbsWidth(), start.getAbsHeight()), 
				owner.getScreenDims().getAbsWidth(), owner.getScreenDims().getAbsHeight());
		}
		
		return startImage;
	}

	public void setImage(Image image) 
	{
		this.image = image;
	}

	public AbsDims getStart() 
	{
		return start;
	}

	public void setStart(final AbsDims start, final Control owner) 
	{		
		this.start = start;
		this.owner = owner;
	}	

	public AbsDims getFinish() 
	{
		return finish;
	}

	public void setFinish(final AbsDims finish, final Control owner) 
	{
		this.finish = finish;
		this.owner = owner;
	}

	public long getElapsed() 
	{
		return elapsed;
	}

	public void setElapsed(long elapsed) 
	{
		this.elapsed = elapsed;
	}
	
	public BufferedImage scaleImage(BufferedImage img, int width, int height)
	{
	    final BufferedImage newImage = new BufferedImage(width, height,
	            BufferedImage.TYPE_INT_RGB);
	    final Graphics2D g = newImage.createGraphics();
	    try 
	    {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        //g.setBackground(Color.white);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(img, 0, 0, width, height, null);
	    }
	    finally 
	    {
	        g.dispose();
	    }
	    return newImage;
	}

	@Override
	public void render(final Graphics graphics, final float completePct, final AbsDims screenDims) 
	{
		getImage().load();
		
		final AbsDims start = getStart();
		final AbsDims finish = getFinish();
		
		// Size of the viewport is dependent on amount that is completed
		final int left = (int) ((finish.left - start.left) * completePct + start.left);
		final int top = (int) ((finish.top - start.top) * completePct + start.top);
		final int right = (int) ((finish.right - start.right) * completePct + start.right);
		final int bottom = (int) ((finish.bottom - start.bottom) * completePct + start.bottom);
		
		final AbsDims newDims = new AbsDims(left, top, right, bottom);
		
		graphics.drawImage(getImage().getBufferedImage(), screenDims, newDims);
	
	}

}
