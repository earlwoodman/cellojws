/*
 * Created on 2010-07-12
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.controls;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.SerializableLock;
import com.cellojws.general.WindowedList;
import com.cellojws.general.core.Environment;


public class ScrollingText extends Control
{

    private List<String> items; 

    private final SerializableLock itemLock = new SerializableLock();
    
    private Collection<String> cache = new ArrayList<String>();
    
    public ScrollingText(final AbsDims dims)
    {
        super(dims, null);
    }
    
    @Override
    public boolean doSpecialClickActions(int x, int y)
    {
    	return true;
    }

    @Override
    public void render(Graphics graphics, final boolean mousedown)
    {
        final int textHeight = graphics.getTextHeight(Environment.getListBoxFontInfo()) + 5;
        
        graphics.setFontInfo(Environment.getListBoxFontInfo());
        
        if( items == null )
        {
            items = new WindowedList(getDimensions().getAbsHeight() / textHeight);
            if( cache.size() > 0 )
            {
                for( String str : cache )
                {
                    items.add(str);
                }
                cache.clear();
            }
        }
        
        final AbsDims dims = getScreenDims();
        graphics.drawRect(dims);
        int i = 0;
        synchronized(itemLock)
        {
            for( String item : items )
            {
                i++;
                graphics.drawText(item, dims.left + 2, dims.top + (i - 1) * textHeight, dims.getAbsWidth());
            }
        }
    }
    
    public void add(final String item)
    {
        synchronized(itemLock)
        {
            if( items == null )
            {
                cache.add(item);
            }
            else
            {
                items.add(item);
            }
        }
    }

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) 
	{
		
	}
   
}
