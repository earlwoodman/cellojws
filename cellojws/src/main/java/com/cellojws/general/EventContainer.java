package com.cellojws.general;

import java.util.List;

public interface EventContainer 
{

	void addEvent(Event e);
	
	List<? extends Event> getXEvents(final int x);
	
}
