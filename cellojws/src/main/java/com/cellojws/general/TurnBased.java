package com.cellojws.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class TurnBased extends Thread implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6050375131492489515L;

	private final Collection<TurnTaker> turnTakers = new ArrayList<>();

	private TurnTaker currentTurnTaker = null;
		
	private boolean takingTurns = false;

	private final Collection<TurnTaker> turnTakersToAdd = new ArrayList<>();
	
	private final Collection<TurnTaker> turnTakersToRemove = new ArrayList<>();
	
	public TurnBased()
	{
		
	}
	
    public void addTurnTaker(final TurnTaker turnTaker)
    {
    	if( !takingTurns )
    	{
    		turnTakers.add(turnTaker);
    	}
    	else
    	{
    		turnTakersToAdd.add(turnTaker);
    	}
    }
    
    /**
     * In case we need to add or remove turn takers while processing a set of turns
     * we keep them in a list and do either action when the current
     * set of turns has been completely processed.
     * 
     */
    public void takeTurns()
    {
    	takingTurns = true;
    	
		for( final TurnTaker turnTaker : turnTakers )
    	{
    		currentTurnTaker = turnTaker;
    		turnTaker.takeTurn();
    	}
		
		currentTurnTaker = null;
		
		turnTakers.removeAll(turnTakersToRemove);
		turnTakers.addAll(turnTakersToAdd);
		
		turnTakersToRemove.clear();
		turnTakersToAdd.clear();
		
		takingTurns = false;
    }
    
    public TurnTaker getCurrentTurnTaker()
    {
   		return currentTurnTaker;
    }

	public void removeTurnTaker(TurnTaker turnTaker) 
	{
		if( !takingTurns )
		{
			turnTakers.remove(turnTaker);
		}
		else
		{
			turnTakersToRemove.add(turnTaker);
		}
	}
    
}
