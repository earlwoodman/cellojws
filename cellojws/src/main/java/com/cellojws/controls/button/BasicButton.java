/*
 * Created on 2010-10-18
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.controls.button;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;

import com.cellojws.dimensions.AbsDims;
import com.cellojws.token.CommandToken;


public class BasicButton extends Button
{

    public BasicButton(final FixedSizeButtonType type, final String text, final CommandToken<?> token, final Point pos)
    {
        super(type, text, token, pos);
    }
    
	public BasicButton(final AbsDims dim, String text, CommandToken<?> token, final ButtonType size)
	{
		super(dim, text, token, size);
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) 
	{	
		
	}

}
