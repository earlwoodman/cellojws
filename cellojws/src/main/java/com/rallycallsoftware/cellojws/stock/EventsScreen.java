/*
 * Created on 2010-12-02
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.stock;

import java.awt.Point;
import java.util.List;

import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.ScrollingText;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Event;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.token.CommandToken;


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
