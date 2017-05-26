package com.rallycallsoftware.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rallycallsoftware.cellojws.general.Random;

public class RandomTest
{

	@Test
	public void zero()
	{
	     assertEquals(Random.getRandIntInclusive(0, 0), 0);
	}
	
	@Test
	public void zeroOrOne()
	{
        final int result = Random.getRandIntInclusive(0, 1);
        
        assertEquals(result == 0 || result == 1, true);		
	}
	
	@Test
	public void boundary()
	{
		
        // If we run this test a bunch of times
        // and the result is never outside the expected range
        // then it's safe to consider this test passed
		int rand;
        for( int i = 0; i < 1000; i++ )
        {
            rand = Random.getRandIntInclusive(0, 10);
            assertEquals(rand >= 0 && rand <= 10, true);
        }

	}

}
