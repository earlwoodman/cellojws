/*
 * Created on 2010-10-19
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.controls;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.Window;


public class TextBox extends Label
{
        
	private static final Collection<Integer> legalCharacters;

	private int maxChars; 
	
	static
	{		
		legalCharacters = 
			Arrays.asList(
				KeyEvent.VK_0,
				KeyEvent.VK_1,
				KeyEvent.VK_2,
				KeyEvent.VK_3,
				KeyEvent.VK_4,
				KeyEvent.VK_5,
				KeyEvent.VK_6,
				KeyEvent.VK_7,
				KeyEvent.VK_8,
				KeyEvent.VK_9,
				KeyEvent.VK_A,
				KeyEvent.VK_B,
				KeyEvent.VK_C,
				KeyEvent.VK_D,
				KeyEvent.VK_E,
				KeyEvent.VK_F,
				KeyEvent.VK_G,
				KeyEvent.VK_H,
				KeyEvent.VK_I,
				KeyEvent.VK_J,
				KeyEvent.VK_K,
				KeyEvent.VK_L,
				KeyEvent.VK_M,
				KeyEvent.VK_N,
				KeyEvent.VK_O,
				KeyEvent.VK_P,
				KeyEvent.VK_Q,
				KeyEvent.VK_R,
				KeyEvent.VK_S,
				KeyEvent.VK_T,
				KeyEvent.VK_U,
				KeyEvent.VK_V,
				KeyEvent.VK_W,
				KeyEvent.VK_X,
				KeyEvent.VK_Y,
				KeyEvent.VK_Z,
				KeyEvent.VK_COMMA,
				KeyEvent.VK_PERIOD,
				KeyEvent.VK_BACK_QUOTE,
				KeyEvent.VK_SPACE
			);
	};
	
	public TextBox(final AbsDims dim, final String text, final int maxChars_)
    {
        super(dim, text);

        maxChars = maxChars_;
        setBackground(Color.BLACK);
		setSolidBackground(true);
    }

    
    @Override
    public CommandToken<?> processKeyPress(KeyEvent keyEvent)
    {
    	if( keyEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE )
    	{
    		if( getText().length() > 0 )
    		{
    			setText(getText().substring(0, getText().length() - 1));
    		}
    	}
    	else if( legalCharacters.contains(keyEvent.getKeyCode()) )
    	{
    		if( getText().length() < maxChars )
    		{
	    		setText(getText() + keyEvent.getKeyChar());
			}
    	}
    	
    	return null;
    }


    @Override
    public boolean doSpecialClickActions(int x, int y)
    {
    	((Window)getWindow()).setFocus(this);
    	return true;
    }

    @Override
    public void render(final Graphics graphics, final boolean mousedown)
    {
    	if( isFocus() )
    	{
    		setText(getText() + "_");
    	}
        super.render(graphics, mousedown);
        
        if( isFocus() )
        {
        	setText(getText().substring(0, getText().length() - 1));
        }
    }


	private boolean isFocus()
	{
		return getWindow() != null && ((Window)getWindow()).getFocus() == this;
	}

}
