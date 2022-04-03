package com.rallycallsoftware.cellojws.controls;

import java.util.Collection;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;


public interface ControlContainer 
{
    void addControl(final Control control);
    
    Collection<Control> getControls();
    
    void removeControl(final Control control);
    
    AbsDims getScreenDims();
    
    AbsDims getDimensions();
    
    void render(final Graphics graphics, final boolean mousedown);
}
