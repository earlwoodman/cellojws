package com.rallycallsoftware.cellojws.controls;

import java.awt.event.MouseWheelEvent;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;


public class Container extends Control 
{

	public Container(final AbsDims dim, final CommandToken<?> token_) 
	{
		super(dim, token_);
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


}
