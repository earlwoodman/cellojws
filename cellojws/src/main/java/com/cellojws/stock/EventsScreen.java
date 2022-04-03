/*
 * Created on 2010-12-02
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.stock;

import java.awt.Point;
import java.util.List;

import com.cellojws.controls.ControlController;
import com.cellojws.controls.ScrollingText;
import com.cellojws.controls.button.BasicButton;
import com.cellojws.controls.button.Button;
import com.cellojws.controls.button.SmallButtonType;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.Event;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.Image;
import com.cellojws.general.image.ImageFactory;
import com.cellojws.token.CommandToken;


public class EventsScreen extends Screen
{
    
    private ScrollingText eventBox;
    
    private List<? extends Event> events;
    
    private Button close;
    
    public EventsScreen(final AbsDims dims)
    {
        super(dims, Environment.getLargePopupImage());
        initialize();
    }
    
    @Override
    public void refresh()
    {
    	events = null;
    	
    	for( Event event : events )
    	{
    		eventBox.add(event.toString());
    	}
    }
    
    private void initialize()
    {
        
        final AbsDims dims = getDimensions();
        
        final AbsDims eventDims = new AbsDims(25, 25, dims.getAbsWidth() - 25, dims.getAbsHeight() - 71);
        eventBox = new ScrollingText(eventDims);
        refresh();
        
        final Image image = ImageFactory.getImage("ListBox.png");
        setImage(image);
        
		final Point closePos = new Point(
				(dims.getAbsWidth() - Button.getStandardButtonWidth()) / 2, 
				dims.getAbsHeight() - 50); 
		close = new BasicButton(SmallButtonType.getInstance(), "Close", new CommandToken<Object>(ControlController::close), closePos);
		addControl(close);
		
        addControl(eventBox);
    }

    @Override
	public boolean validate() 
	{	
		return true;
	}

	@Override
	public CommandToken<?> enterKeyPressed()
	{
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed()
	{
		return null;
	}

}
