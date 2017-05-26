package com.rallycallsoftware.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rallycallsoftware.cellojws.general.DateTime;

public class DateTimeGapTest
{

	private DateTime earlier;
	private DateTime later;
	

	@Test
	public void initialGap()
	{
        earlier = new DateTime(2009, DateTime.JANUARY, 1, 1, 1, 1);
        later = new DateTime(2009, DateTime.JANUARY, 5, 1, 1, 1);

        assertEquals(later.getDifferenceDays(earlier), 4);
	}
	
	@Test
	public void nextMonth() 
	{
        earlier = new DateTime(2009, DateTime.JANUARY, 1, 1, 1, 1);
        later = new DateTime(2009, DateTime.JANUARY, 5, 1, 1, 1);

        later.setMonth(DateTime.FEBRUARY);
        
        assertEquals(later.getDifferenceDays(earlier), 35);
	}
	
    @Test
    public void nextYear()
    {
        earlier = new DateTime(2009, DateTime.JANUARY, 1, 1, 1, 1);
        later = new DateTime(2009, DateTime.FEBRUARY, 5, 1, 1, 1);

        later.setYear(later.getYear() + 1);

        assertEquals(later.getDifferenceDays(earlier), 400);
    }
    
    @Test
    public void twoYears()
    {
        earlier = new DateTime(2009, DateTime.JANUARY, 1, 1, 1, 1);
        later = new DateTime(2010, DateTime.FEBRUARY, 5, 1, 1, 1);

    	later.setYear(later.getYear() + 1);
    	
        assertEquals(later.getDifferenceDays(earlier), 765);
    }
    
    @Test
    public void fourYears()
    {
    	earlier = new DateTime(2009, DateTime.JANUARY, 1, 1, 1, 1);
        later = new DateTime(2009, DateTime.JANUARY, 1, 1, 1, 1);

        later.setYear(later.getYear() + 6);
        
        // 1 leap year + DateTime.getDaysPerMonth(DateTime.JANUARY, later.getYear()) + 4;	
        assertEquals(later.getDifferenceDays(earlier), 365 * 6 + 1); 
        
	}

}
