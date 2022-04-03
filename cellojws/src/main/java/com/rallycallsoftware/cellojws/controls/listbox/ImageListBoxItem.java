package com.rallycallsoftware.cellojws.controls.listbox;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.image.Image;

public class ImageListBoxItem implements ListBoxItem 
{

	private Image image;

	private int width;
	
	private int height;
	
	public ImageListBoxItem(Image image)
	{
		this.image = image;
		
		width = Math.round(image != null ? image.getWidth() : 20);
		height = Math.round(image != null ? image.getHeight() : 20);
	}

	public Image getImage() 
	{
		return image;
	}

	public void setImage(Image image) 
	{
		this.image = image;
	}

	@Override
	public int getHeight() 
	{		
		return height;
	}	

	@Override
	public int getWidth()
	{
		return ListBox.PADDING + width;
	}
	
	@Override
	public String toString()
	{
		return image.toString();
	}

	@Override
	public void draw(Graphics graphics, int leftDraw, int top, int widthMax) 
	{
		if( getImage() != null )
		{
			//TODO: Sucks making a new dimensions object every time here.
			graphics.drawImage( 
					getImage().getBufferedImage(), 
					new AbsDims(leftDraw, top, leftDraw + width, top + height));
		}	
	}
	
}
