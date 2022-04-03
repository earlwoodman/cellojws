package com.cellojws.general;

public interface ScheduledItem
{
	DateTime getDateTime();
	
	void setDateTime(DateTime dateTime);

	boolean isComplete();
}
