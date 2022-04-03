package com.rallycallsoftware.cellojws.controls;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.Map;

import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.DownButtonType;
import com.rallycallsoftware.cellojws.controls.button.UpButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;


public abstract class LabelPin extends Label implements Pin
{
	
	private Button up;
	
	private Button down;

	public LabelPin(final AbsDims dims, final String text_, final Map<String, CommandToken<?>> tokens_,
			final boolean moveable)
	{
		super(dims, text_);
		
		tokenMap(tokens_, moveable);
		
	}

	public void tokenMap(final Map<String, CommandToken<?>> tokens_, final boolean moveable) 
	{
		if( tokens_.get("dragDrop") != null )
		{
			@SuppressWarnings("unchecked")
			final CommandToken<DragDropBean<Control, Control>> dropToken = (CommandToken<DragDropBean<Control, Control>>) tokens_.get("dragDrop"); 
			setDropToken(dropToken);
		}
		
		if( moveable )
		{
			up = new BasicButton(UpButtonType.getInstance(), "", tokens_ != null ? tokens_.get("up") : null, getUpBoxPos());
			addControl(up);
			
			down = new BasicButton(DownButtonType.getInstance(), "", tokens_ != null ? tokens_.get("down") : null, getDownBoxPos());
			addControl(down);			
		}
	}

	private Point getUpBoxPos()
	{
		final AbsDims dims = getDimensions();
		final int boxHeight = dims.getAbsHeight();
		
		final int boxesToShift = 2;
		return new Point(dims.getAbsWidth() - boxHeight * boxesToShift, dims.getAbsHeight() - boxHeight);	
	}

	private Point getDownBoxPos()
	{
		final AbsDims dims = getDimensions();
		final int boxHeight = dims.getAbsHeight();
		
		final int boxesToShift = 1;
		return new Point(dims.getAbsWidth() - boxHeight * boxesToShift, dims.getAbsHeight() - boxHeight);	
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent)
	{

	}
		
	public CommandToken<?> getUpToken()
	{
		if( up != null )
		{
			return up.getToken();
		}
		
		return null;
	}

	public CommandToken<?> getDownToken()
	{
		if( down != null )
		{
			return down.getToken();
		}
		
		return null;
	}

}
