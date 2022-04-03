package com.cellojws.controls.kenburns;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.image.Image;

public class CircleCommand implements KBCommand
{
	private Image image;
	
	private AbsDims start = null;
	
	private long elapsed;

	private BufferedImage startImage;
	
	private long startTime;
	
	private long finishTime;

	private Control owner;

	private int viewWidth;

	private int viewHeight;

	private Point midPoint = new Point();
	
	private int radius;
	
	public CircleCommand(final Control owner)
	{
		this.owner = owner;
	}
	
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
		return startImage;
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
		
		final BufferedImage bi = image.getBufferedImage();
		final int imageWidth = bi.getWidth();
		final int imageHeight = bi.getHeight();
		
		viewWidth = owner.getDimensions().getAbsWidth();
		viewHeight = owner.getDimensions().getAbsHeight();
			
		start = new AbsDims(
				(imageWidth - viewWidth) / 2,
				0,
				(imageWidth - viewWidth) / 2 + viewWidth,
				viewHeight
				);
		
		midPoint.x = imageWidth / 2;
		midPoint.y = viewHeight / 2;
		
		radius = (imageHeight - viewHeight) / 2;
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
		
		midPoint.x = (int)(radius * Math.cos(completePct * 2 * Math.PI)) + getImage().getBufferedImage().getWidth() / 2;
		midPoint.y = (int)(radius * Math.sin(completePct * 2 * Math.PI)) + getImage().getBufferedImage().getHeight() / 2;
				
		final int left = midPoint.x - viewWidth / 2; 
		final int top = midPoint.y - viewHeight / 2;
		final int right = midPoint.x + viewWidth / 2;
		final int bottom = midPoint.y + viewHeight / 2;
		
		final AbsDims newDims = new AbsDims(left, top, right, bottom);
		
		graphics.drawImage(getImage().getBufferedImage(), screenDims, newDims);
	
	}

	public static CircleCommand createCommand(final Control owner, final Image image, final int milliseconds)
	{
		final CircleCommand command = new CircleCommand(owner);
		
		command.setImage(image);
		command.setElapsed(milliseconds);
		
		return command;
	}

}
