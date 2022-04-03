/*
 * Created on 2010-06-18
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.general;

import java.io.Serializable;

public interface RealTimeListener extends Serializable
{

	void minutesIncreased(final DateTime date_time);
	
    void hoursIncreased(final DateTime date_time);

    void daysIncreased(final DateTime date_time);
    
    void yearsIncreased(final DateTime date_time);

}
