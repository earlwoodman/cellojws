package com.cellojws.dimensions;

import java.awt.Rectangle;

public interface Dimensions
{
	Dimensions makeCopy(); 
	   
    int getAbsHeight();
    
    int getAbsWidth();
    
    void shrink(final int i); 

	void move(final int x, final int y);
	
	Rectangle getRect();

	AbsDims absoluteify();

}
