package com.rallycallsoftware.cellojws.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class TurnBased extends Thread implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6050375131492489515L;

	private final Collection<TurnTaker> turnTakers = new ArrayList<TurnTaker>();

	private TurnTaker currentTurnTaker = null;

	private SerializableLock turnTakerLock = new SerializableLock();

	public TurnBased() {

	}

	public void addTurnTaker(final TurnTaker turnTaker) {
		turnTakers.add(turnTaker);
	}

	public void takeTurns() {
		for (final TurnTaker turnTaker : turnTakers) {
			synchronized (turnTakerLock) {
				currentTurnTaker = turnTaker;
			}
			turnTaker.takeTurn();
		}

		synchronized (turnTakerLock) {
			currentTurnTaker = null;
		}
	}

	public TurnTaker getCurrentTurnTaker() {
		synchronized (turnTakerLock) {
			return currentTurnTaker;
		}
	}

	public void removeTurnTaker(TurnTaker turnTaker) {
		synchronized (turnTakerLock) {
			turnTakers.remove(turnTaker);
		}
	}

}
