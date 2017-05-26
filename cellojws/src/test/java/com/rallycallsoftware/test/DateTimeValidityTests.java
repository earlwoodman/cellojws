package com.rallycallsoftware.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rallycallsoftware.cellojws.general.DateTime;

public class DateTimeValidityTests
{

	@Test
	public void validity()
	{
        // Make random times and validate them
        for( int i = 0; i < 10000; i++ )
        {
            DateTime dateTime = DateTime.getRandomDateInYear(1850 + i / 10);
            assertEquals(DateTime.isValidDate(dateTime), true);
        }
	}
	
}
