package com.cellojws.windowing;

public interface WindowListener
{
	void showPopup(int count);
	
	void closePopup(int count);
		
	void closeAllPopups();
}
