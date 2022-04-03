/*
 * Created on 2010-07-12
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

import java.util.ArrayList;
import java.util.Collection;

public class WindowedList extends ArrayList<String>
{

    /**
     * 
     */
    private static final long serialVersionUID = 155L;
    
    private int limit;
    
    public WindowedList(final int limit_) 
    {
        super();
        limit = limit_;
    }
    
    @Override
    public boolean add(String arg0)
    {     
        super.add(arg0);
        if( super.size() > limit )
        {
            super.remove(0);
        }
        
        return true;
    }

    @Override
    public void add(final int arg0, final String arg1)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends String> arg0)
    {
        throw new UnsupportedOperationException();    }

    @Override
    public boolean addAll(int arg0, Collection<? extends String> arg1)
    {
        throw new UnsupportedOperationException();
    }

    
    
}
