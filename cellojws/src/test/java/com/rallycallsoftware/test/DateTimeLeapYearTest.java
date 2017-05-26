package com.rallycallsoftware.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rallycallsoftware.cellojws.general.DateTime;

public class DateTimeLeapYearTest
{

	@Test
	public void multiple400Century()
	{
        DateTime date = new DateTime(2000, DateTime.FEBRUARY, 28, 2, 27, 30);        
        date.incrementDays(1);
        assertEquals(date.getDay(), 29);
	}
	
	@Test
	public void non400MultipleCentury()
	{

        DateTime date = new DateTime(1700, DateTime.FEBRUARY, 28, 2, 27, 30);
        date.incrementDays(1);
        assertEquals(date.getDay(), 1);
	}
	
	@Test
	public void leapYear()
	{
		DateTime date = new DateTime(2008, DateTime.FEBRUARY, 28, 2, 27, 30);
        date.incrementDays(1);
        assertEquals(date.getDay(), 29);
	}
	
	@Test
	public void nonLeapYear()
	{
		DateTime date = new DateTime(2007, DateTime.FEBRUARY, 28, 2, 27, 30);
        date.incrementDays(1);
        assertEquals(date.getDay(), 1);
	}
	
	@Test
	public void twoMonths()
	{
        DateTime date = new DateTime(2011, DateTime.DECEMBER, 31, 0, 0, 0);
        date.incrementDays(65);
        assertEquals(date.getDay(), 5);
	}
	

}
