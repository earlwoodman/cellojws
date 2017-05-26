package com.rallycallsoftware.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rallycallsoftware.cellojws.general.DateTime;

public class DateTimeESTTest
{
	
	@Test
	public void basic()
	{
        DateTime date = new DateTime(2009, 7, 14, 2, 27, 30);
        
        DateTime inEST = date.getESTEquivalent(5);
        
        assertEquals(inEST.getDay(), 14);
        assertEquals(inEST.getHour(), 7);
        assertEquals(inEST.getMinute(), 27);
	}
	
	@Test
	public void reverseBasic()
	{
		DateTime date = new DateTime(2009, 7, 14, 2, 27, 30);
		
        DateTime inEST = date.getESTEquivalent(-2);
        assertEquals(inEST.getDay(), 14);
        assertEquals(inEST.getHour(), 0);
        assertEquals(inEST.getMinute(), 27);
	}
	
	@Test
	public void fractional()
	{
		DateTime date = new DateTime(2009, 7, 14, 2, 27, 30);
		
        DateTime inEST = date.getESTEquivalent(1.5F);
        
        assertEquals(inEST.getDay(), 14);
        assertEquals(inEST.getHour(), 3);
        assertEquals(inEST.getMinute(), 57);
	}

	@Test 
	public void dayAfterNewYears()
	{
		DateTime later = new DateTime(2016, DateTime.JANUARY, 1);
		DateTime earlier = new DateTime(2015, DateTime.DECEMBER, 31);
		
		assertEquals(later.isDayAfter(earlier), true);
	}
	
	@Test
	public void notDayAfter()
	{
		DateTime later = new DateTime(2016, DateTime.JANUARY, 1);
		DateTime earlier = new DateTime(2015, DateTime.FEBRUARY, 30);
		assertEquals(later.isDayAfter(earlier), false);
	}
	
	@Test
	public void dayAfterSameMonth()
	{
		DateTime later = new DateTime(2016, DateTime.JANUARY, 5);
		DateTime earlier = new DateTime(2016, DateTime.JANUARY, 4);
		
		assertEquals(later.isDayAfter(earlier), true);
	}
	
	@Test
	public void dayAfterDifferentMonth()
	{
		DateTime later = new DateTime(2016, DateTime.MARCH, 1);
		DateTime earlier = new DateTime(2016, DateTime.FEBRUARY, (int)DateTime.getDaysPerMonth(DateTime.FEBRUARY, 2016));
		
		assertEquals(later.isDayAfter(earlier), true);
	}
	
}
