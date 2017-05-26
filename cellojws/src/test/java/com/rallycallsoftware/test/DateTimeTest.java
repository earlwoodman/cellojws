package com.rallycallsoftware.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rallycallsoftware.cellojws.general.DateTime;

public class DateTimeTest
{

	@Test
	public void incrementsAndDecrements()
	{
        DateTime date = new DateTime(2009, DateTime.AUGUST, 15, 2, 27, 30);

        date.incrementSeconds(5);
        date.incrementSeconds(56);

        date.incrementMinutes(5);
        date.incrementMinutes(181);

        date.incrementHours(1);
        date.incrementHours(25);

        date.incrementDays(2);
        date.incrementDays(35);
        date.decrementDay();
        date.incrementDays(275);

        date.incrementMonths(5);
        date.incrementMonths(50);
        date.incrementMonths(14);

        date.decrementDay();
        date.incrementYear(1);
        date.incrementYear(4);

        date.incrementMilliseconds(500);
        date.incrementMilliseconds(1000);
        
        date.decrementDay();
        
        assertEquals(DateTime.MARCH, date.getMonth());
        assertEquals(21, date.getDay());
        assertEquals(2021, date.getYear());
        assertEquals(7, date.getHour());
        assertEquals(34, date.getMinute());
        assertEquals(32, date.getSecond());
        assertEquals(500, date.getMillisecond());
	}

}
