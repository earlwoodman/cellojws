package com.cellojws.controls;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.image.Image;
import com.cellojws.token.CommandToken;


public abstract class PinBoard extends Control
{

	private List<Pin> pins = new ArrayList<Pin>();
	
	private final int gap = 10;

	private boolean moveable;
		
	public PinBoard(final AbsDims dim, final CommandToken<?> token_, final Image backgroundPic, final boolean moveable)
	{
		super(dim, token_);
	
		this.moveable = moveable;
		final AbsDims backgrdDims = new AbsDims(0, 0, dim.getAbsWidth(), dim.getAbsHeight());
		final Control background = new Control(backgrdDims, null);
		background.setImage(backgroundPic);
		addControl(background);
	}

	/**
	 * Adds the specified pin to the board.
	 * 
	 * @param pin
	 */
	public void addNewPin(final Pin pin)
	{
		pins.add(pin);
		addControl((Control)pin);
	}

	/**
	 * Returns the dims of the next pin
	 * @return
	 */
	public AbsDims getNextPinDims()
	{
		return getNextPinDims(pins.size() + 1);
	}
	
	private AbsDims getNextPinDims(final int count)
	{

		final int width = getDimensions().getAbsWidth();
		final int height = 20;
		
		return new AbsDims(
				gap,
				(count - 1) * height + 5,
				width - gap,
				count * height + 5
				);
		
	}
	
	@Override
	public void render(Graphics graphics, boolean mousedown)
	{	

	}

	@Override
	public boolean doSpecialClickActions(int x, int y)
	{
		return true;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent)
	{

	}

	/**
	 * Removes all pins from the board by removing from both the list here
	 * and from all child controls
	 */
	public void removeAllPins()
	{
		for( Pin pin : pins )
		{
			if( pin instanceof Control )
			{
				removeControl((Control)pin);
			}
		}
		
		pins.clear();
			
	}

	public boolean isMoveable() 
	{
		return moveable;
	}

	public List<Pin> getCopyOfPins() 
	{
		return new ArrayList<>(pins);
	}

}
