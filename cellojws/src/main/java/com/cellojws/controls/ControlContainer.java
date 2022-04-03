package com.cellojws.controls;

import java.util.Collection;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;


public interface ControlContainer 
{
    void addControl(final Control control);
    
    Collection<Control> getControls();
    
    void removeControl(final Control control);
    
    AbsDims getScreenDims();
    
    AbsDims getDimensions();
    
    void render(final Graphics graphics, final boolean mousedown);
}
