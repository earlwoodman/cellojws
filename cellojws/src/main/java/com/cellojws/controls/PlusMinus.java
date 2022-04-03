package com.cellojws.controls;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.button.BasicButton;
import com.cellojws.controls.button.BigDownScrollButtonType;
import com.cellojws.controls.button.BigUpScrollButtonType;
import com.cellojws.controls.button.Button;
import com.cellojws.controls.button.DownScrollButtonType;
import com.cellojws.controls.button.UpScrollButtonType;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.Justification;
import com.cellojws.general.core.Environment;
import com.cellojws.token.CommandToken;


public class PlusMinus extends Control
{	
	
	private Button down;
	
	private Button up;
	
	private Button downBig;
	
	private Button upBig;
	
	private Label caption;
	
	private int value;
	
	private int topIndex;
	
	private int bottomIndex;
	
	private int step;
	
	private int bigStep;

	private boolean formatAsDollars = false;
	
	public PlusMinus(final AbsDims dim, final int topIndex_, final int bottomIndex_, final int value_, final CommandToken<?> token, 
			final int step_, final boolean formatAsDollars_, final String prefix, final int bigStep) 
	{
		super(dim, token);
		
		value = value_;
		bottomIndex = bottomIndex_;
		topIndex = topIndex_;
		step = step_;
		this.bigStep = bigStep;
		
		formatAsDollars = formatAsDollars_;
		
		if( bigStep != step_ )
		{
			upBig = new BasicButton(
					BigUpScrollButtonType.getInstance(), 
					"", 
					new CommandToken<PlusMinus>(ControlController::upBig, this), 
					new Point(dim.getAbsWidth() - VerticalScrollBar.getControlBoxHeight(), 0));
			addControl(prefix + "upBig", upBig);
			
			downBig = new BasicButton(
					BigDownScrollButtonType.getInstance(), 
					"", 
					new CommandToken<PlusMinus>(ControlController::downBig, this), 
					new Point(0, 0));
			addControl(prefix + "downBig", downBig);

			up = new BasicButton(
					UpScrollButtonType.getInstance(), 
					"", 
					new CommandToken<PlusMinus>(ControlController::up, this), 
					new Point(dim.getAbsWidth() - VerticalScrollBar.getControlBoxHeight() * 2 - Environment.verySmallGap(), 0));
			addControl(prefix + "up", up);
			
			down = new BasicButton(
					DownScrollButtonType.getInstance(), 
					"", 
					new CommandToken<PlusMinus>(ControlController::down, this), 
					new Point(VerticalScrollBar.getControlBoxHeight() + Environment.verySmallGap(), 0));
			addControl(prefix + "down", down);
		}
		else
		{
			up = new BasicButton(
					UpScrollButtonType.getInstance(), 
					"", 
					new CommandToken<PlusMinus>(ControlController::up, this), 
					new Point(dim.getAbsWidth() - VerticalScrollBar.getControlBoxHeight(), 0));
			addControl(prefix + "up", up);
			
			down = new BasicButton(
					DownScrollButtonType.getInstance(), 
					"", 
					new CommandToken<PlusMinus>(ControlController::down, this), 
					new Point(0, 0));
			addControl(prefix + "down", down);
		}
		
		final AbsDims captionDims = new AbsDims(
				VerticalScrollBar.getControlBoxHeight(), 
				0, 
				dim.getAbsWidth() - VerticalScrollBar.getControlBoxHeight() * 2, 
				VerticalScrollBar.getControlBoxHeight());
		caption = new Label(captionDims, "");
		caption.setJustification(Justification.Center);
		redrawCaption();
		addControl(caption);
		
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

	public void decrement() 
	{	
		if( value >= bottomIndex + step )
		{
			value -= step;
		}
		else if( value >= bottomIndex )
		{
			value = bottomIndex;
		}
		redrawCaption();
	}

	public void increment() 
	{
		if( value <= topIndex - step )
		{
			value += step;
		}
		else if( value <= topIndex )
		{
			value = topIndex;
		}
		redrawCaption();		
	}

	public void decrementBig() 
	{	
		if( value >= bottomIndex + bigStep )
		{
			value -= bigStep;
		}
		else if( value >= bottomIndex )
		{
			value = bottomIndex;
		}
		redrawCaption();
	}

	public void incrementBig() 
	{
		if( value <= topIndex - bigStep )
		{
			value += bigStep;
		}
		else if( value <= topIndex )
		{
			value = topIndex;
		}
		redrawCaption();		
	}

	public int getValue() 
	{
		return value;
	}

	public void setValue(final int value_) 
	{
		value = value_;
		redrawCaption();
		
	}

	private void redrawCaption() 
	{
		if( formatAsDollars )
		{
			final DecimalFormat df1 = new DecimalFormat("$#,###.##");
			caption.setText(df1.format((float)value/ 1000000F) + "M");
		}
		else
		{
			caption.setText(new Integer(value).toString());
		}
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) 
	{
		
	}

	@Override
	public void disable() 
	{
		super.disable();
		
		up.disable();
		
		down.disable();
	}

	
}
