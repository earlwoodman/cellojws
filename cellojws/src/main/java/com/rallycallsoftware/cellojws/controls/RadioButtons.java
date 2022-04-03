package com.rallycallsoftware.cellojws.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.RadioButtonInfoBean;
import com.rallycallsoftware.cellojws.controls.button.RadioButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.Orientation;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class RadioButtons extends Control
{

	private List<Button> buttons = new ArrayList<>();
		
	public RadioButtons(final AbsDims dims, final Orientation orientation, final RadioButtonInfoBean... beans)
	{
		super(dims);
						
		buttons = new ArrayList<>();

		final RadioButtonType radioButtonType = new RadioButtonType();
		final int gap = getRadioGap();
		final int buttonHeight = radioButtonType.getHeight();
		final int buttonWidth = calculateButtonWidth(beans);
		
		for( int i = 0; i < beans.length; i++ )
		{
			AbsDims sliceDims = null;
			if( orientation == Orientation.Vertical )
			{
				sliceDims = new AbsDims(
						0,
						(i + 1) * gap + i * buttonHeight,
						radioButtonType.getWidth(),
						(i + 1) * gap + (i + 1) * buttonHeight
						);
			}
			else if( orientation == Orientation.Horizontal )
			{
				sliceDims = new AbsDims(
						(i + 1) * gap + i * buttonWidth,
						0,
						(i + 1) * gap + (i + 1) * buttonWidth,
						radioButtonType.getHeight()
						);				
			}				
			final CommandToken<RadioButtonInfoBean> token = new CommandToken<>(ControlController::turnOnRadioButton);
			final Button button = new BasicButton(
					sliceDims, 
					beans[i].getCaption(), 
					token,
					new RadioButtonType(0 == i, beans.length > 1));
			
			button.setFontInfo(Environment.getRadioButtonFontInfo());
			button.setJustification(Justification.Left);
			beans[i].setButtons(buttons);
			beans[i].setClickedButton(button);
			token.setPayload(beans[i]);
			addControl("button" + Integer.toString(i), button);
			buttons.add(button);
		}
	}
	
	private int calculateButtonWidth(RadioButtonInfoBean[] beans) 
	{
		int ret = new RadioButtonType().getWidth();
		
		if( beans.length > 0 )
		{
			ret += Arrays.asList(beans).stream()
				.map(x -> WindowManager.getTextWidth(x.getCaption(), Environment.getRadioButtonFontInfo()))
				.max(Comparator.naturalOrder()).get();
		}
		
		return ret;
	}

	private int getRadioGap()
	{
		return RadioButtonType.getRadioGap();
	}

	public int getChecked()
	{
		final Optional<Button> button = 
			buttons.stream()
				.filter(x -> ((RadioButtonType)x.getButtonType()).isChecked())
				.findFirst();
		
		if( button.isPresent() )
		{
			return buttons.indexOf(button.get());
		}
		
		return -1;
	}

	public void setChecked(final int toCheck) 
	{
		if( buttons.size() > toCheck )
		{
			((RadioButtonType)buttons.get(toCheck).getButtonType()).setChecked(true);
		}
		else
		{
			WorkerLog.error("There was no button with index " + toCheck);
		}
	}
	
	public void setUnchecked(final int toUncheck) 
	{
		if( buttons.size() > toUncheck )
		{
			((RadioButtonType)buttons.get(toUncheck).getButtonType()).setChecked(false);
		}
		else
		{
			WorkerLog.error("There was no button with index " + toUncheck);
		}
	}

	public CommandToken<?> getButtonToken(final int index)
	{
		if( buttons.size() > index ) 
		{
			return buttons.get(index).getToken();
		}
		
		WorkerLog.error("Invalid index into RadioButtons.getButtonToken: " + index + " but max size: " + buttons.size());
		return null;
	}
}
