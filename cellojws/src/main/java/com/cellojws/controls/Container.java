package com.cellojws.controls;

import java.awt.event.MouseWheelEvent;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.token.CommandToken;


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
