package com.cellojws.controls.button;

import java.util.ArrayList;
import java.util.List;

import com.cellojws.controls.Control;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.token.CommandToken;

public class ButtonGroup extends Control
{
	private List<Button> buttons = new ArrayList<>();
	
	private List<String> keys = new ArrayList<>();

	public ButtonGroup(final AbsDims dims)
	{
		super(dims);
	}
	
	public Button addButton(String key, String text, CommandToken<?> token)
	{
		final Button button = new BasicButton(null, text, token, SmallButtonType.getInstance());
		
		buttons.add(button);
		keys.add(key);
		addControl(key, button);
		recreateButtonDims();
		return button;
	}

	public void removeButton(final String key)
	{
		if( keys.indexOf(key) != -1 )
		{		
			removeButton(buttons.get(keys.indexOf(key)));
			keys.remove(key);
		}
	}
	
	public void removeButton(Button button)
	{
		buttons.remove(button);
		removeControl(button);
		recreateButtonDims();
	}
	
	private void recreateButtonDims()
	{
		
		final AbsDims dims = getDimensions();
		
		final int initialLeft = 
				(dims.getAbsWidth() - (BasicButton.getStandardButtonWidth() * buttons.size() + Control.getStdGap() * (buttons.size() - 1))) 
				/ 2;
		
		for( int i = 0; i < buttons.size(); i++ )
		{
			final Button button = buttons.get(i);
			
			final int left = initialLeft + BasicButton.getStandardButtonWidth() * i + (Control.getStdGap() * i - 1);
			final AbsDims buttonDims = new AbsDims(
					left,
					dims.getAbsHeight() - BasicButton.getStandardButtonHeight(),
					left + BasicButton.getStandardButtonWidth(), 
					dims.getAbsHeight()
	 		);
			
			button.setDimensions(buttonDims);	
		}		
	}
	
}
