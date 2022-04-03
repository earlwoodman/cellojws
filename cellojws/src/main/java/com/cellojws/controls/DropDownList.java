package com.cellojws.controls;

import java.awt.Point;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.button.BasicButton;
import com.cellojws.controls.button.Button;
import com.cellojws.controls.button.DownScrollButtonType;
import com.cellojws.controls.listbox.ListBox;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.core.Environment;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.Window;
import com.cellojws.windowing.WindowManager;


public class DropDownList extends ListBox
{

	private AbsDims containerRolledUpDims;
	
	private AbsDims containerDroppedDownDims;
	
	private boolean droppedDown = false;

	private Label selection;
	
	private Button downArrow;
	
	// Cache the captions here, since we will need them again.
	private List<String> captions;
	
	public DropDownList(final AbsDims droppedDownDims_, final List<String> captions_, final CommandToken<?> token) 
	{
		super(droppedDownDims_, captions_, token, true, false, null);
	
		captions = captions_;

		containerDroppedDownDims = getDimensions().makeCopy();
		
		containerRolledUpDims = new AbsDims(
				getDimensions().left,
				getDimensions().top,
				getDimensions().right,
				getDimensions().top + WindowManager.getTextHeight(Environment.getListBoxFontInfo()) + 5
				);
					
		final CommandToken<DropDownList> dropDown = new CommandToken<DropDownList>(ControlController::dropDown, this);
		
		final AbsDims selectionDims = new AbsDims(Control.getStdGap(), 0, containerRolledUpDims.getAbsWidth(), containerRolledUpDims.getAbsHeight());
		selection = new Label(selectionDims, "", dropDown);
		
		downArrow = new BasicButton(DownScrollButtonType.getInstance(), "", dropDown, 
				new Point(containerRolledUpDims.getAbsWidth() - ListBox.SCROLLBAR_WIDTH, 
						(containerRolledUpDims.getAbsHeight() - DownScrollButtonType.getInstance().getHeight()) / 2));
		
		select(0);
		rollUp();
	}

	@Override
	public boolean doSpecialClickActions(int x, int y) 
	{
		if( droppedDown )
		{
			super.doSpecialClickActions(x, y);
			rollUp();
		}
		
		return true;
	}

	public void rollUp() 
	{
		setDimensions(containerRolledUpDims);
		droppedDown = false;
		addControl(selection);
		addControl(downArrow);
		if( getSelectedIndex() > -1 )
		{
			selection.setText(captions.get(getSelectedIndex()));
		}
		hideScrollBar();
	}

	public void dropDown() 
	{		
		setDimensions(containerDroppedDownDims);
		droppedDown = true;
		removeControl(selection);
		removeControl(downArrow);
		unhideScrollBar();
		((Window)getWindow()).childDroppingDown(this);
	}

	@Override
	public synchronized void render(Graphics graphics, boolean mousedown) 
	{
		if( droppedDown )
		{
			if( getBackground() != null )
			{
				graphics.drawSolidRect(getScreenDims(), getBackground());
			}
			super.render(graphics, mousedown);
		}

		graphics.drawRect(getScreenDims(), getControlBorderColour());
	}

	@Override
	public void select(int item) 
	{
		super.select(item);
		
		if( droppedDown == false )
		{
			selection.setText(captions.get(item));			
		}
	}

	@Override
	public int getItemsVisibleExcludeHeader()
	{
		if( droppedDown )
		{
			return super.getItemsVisibleExcludeHeader();
		}
		
		return 1;
	}
	

}
