package com.cellojws.controls.listbox;

import java.util.Collection;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.controls.ControlContainer;
import com.cellojws.special.Tooltip;

public interface ListBoxItemControl extends ListBoxItem, ControlContainer
{
	void setLeftDraw(final int leftDraw);
	
	void setTopDraw(final int topDraw);
	
	int getLeftDraw();
	
	int getTopDraw();
	
	void prepareToRender(final Graphics graphics);
		
	default void drawChildren(final Graphics graphics, final Collection<Control> children) 
	{
		// Since we are not using the rendering framework
		// we'll have to render any sub controls manually.

		if( children != null )
		{
			for( final Control control : children )
			{
				if( !(control instanceof Tooltip) )
				{
					if( control instanceof PositionedListBoxItem )
					{
						((PositionedListBoxItem)control).setLeftDraw(getLeftDraw() + control.getDimensions().left);
						((PositionedListBoxItem)control).setTopDraw(getTopDraw() + control.getDimensions().top);
					}
					control.render(graphics, false);
					drawChildren(graphics, control.getControls());
				}
			}
		}
	}
	
	default int getHeight()
	{
		return getDimensions().getAbsHeight();
	}

	default int getWidth()
	{
		return getDimensions().getAbsWidth();
	}
	
	default void draw(final Graphics graphics, final int leftDraw, final int top, final int widthMax)
	{
		prepareToRender(graphics);
		
		setLeftDraw(leftDraw);
		setTopDraw(top);
		
		render(graphics, false);
		
		drawChildren(graphics, getControls());
	}
}
