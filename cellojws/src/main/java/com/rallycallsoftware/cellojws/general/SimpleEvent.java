/*
 * Created on 2011-05-25
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

public class SimpleEvent extends Event
{

    /**
     * 
     */
    private static final long serialVersionUID = 200L;

    public SimpleEvent(final EventLevel level, final String text, final DateTime time)
    {
        super(level, text, time);
    }

}
