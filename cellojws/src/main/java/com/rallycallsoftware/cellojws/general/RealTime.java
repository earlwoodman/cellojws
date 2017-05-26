/*
 * Created on 2010-06-18
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class RealTime extends Thread implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	private int pauses = 1;

	private int multiplier;

	private long lastTickCount;

	private final Collection<RealTimeListener> listeners = new ArrayList<RealTimeListener>();

	private DateTime currentDateTime;

	private boolean kill;

	private final SerializableLock statusLock = new SerializableLock();

	private final SerializableLock listenerLock = new SerializableLock();

	private long oldMinutes = 0;
	private long oldHours = 0;
	private double minutesPassed = 0;

	public RealTime() {

		synchronized (statusLock) {
			currentDateTime = new DateTime();

			resetMultiplier();
			currentDateTime.set(1970, 1, 1, 1, 1, 1);
		}
	}

	public void pulse() {
		long currentTick = System.currentTimeMillis();

		long diffTick = currentTick - lastTickCount;

		if (diffTick > 0) {
			final long milliseconds = diffTick * multiplier;
			oldMinutes = currentDateTime.getMinute();
			oldHours = currentDateTime.getHour();

			minutesPassed += (float) milliseconds / 60000F;

			for (long i = 0; i < (long) minutesPassed; i++) {
				if (!isRunning()) {
					minutesPassed -= (i + 1);
					lastTickCount = currentTick - (milliseconds - (i + 1) * 60000);
					return;
				}
				currentDateTime.incrementMinutes(1);
				minutesIncreased(currentDateTime);
				oldMinutes++;
				if (oldMinutes == 60) {
					oldMinutes = 0;
					oldHours++;
					hoursIncreased(currentDateTime);
					if (oldHours == 24) {
						oldHours = 0;
						daysIncreased(currentDateTime);
					}
				}
			}

			if (minutesPassed >= 1) {
				minutesPassed = 0D;
			}

			lastTickCount = currentTick;

		}

	}

	private void resetTickCount() {
		lastTickCount = System.currentTimeMillis();
	}

	public void minutesIncreased(final DateTime date_time) {
		synchronized (listenerLock) {
			for (RealTimeListener listener : listeners) {
				listener.minutesIncreased(date_time);
			}
		}
		resetTickCount();
	}

	public void hoursIncreased(final DateTime date_time) {
		synchronized (listenerLock) {
			for (RealTimeListener listener : listeners) {
				listener.hoursIncreased(date_time);
			}
		}
		resetTickCount();
	}

	public void daysIncreased(final DateTime date_time) {
		synchronized (listenerLock) {
			for (RealTimeListener listener : listeners) {
				listener.daysIncreased(date_time);
			}
		}
		resetTickCount();
	}

	public void unpause() {
		synchronized (statusLock) {
			if (pauses == 1) {
				// Reset the last tick count, otherwise all the pause time
				// will be considered 'game' time, and that is REAL bad!
				lastTickCount = System.currentTimeMillis();
			}

			pauses--;

		}
	}

	public void pause() {
		synchronized (statusLock) {
			pauses++;
		}
	}

	public boolean isRunning() {
		synchronized (statusLock) {
			return pauses == 0;
		}
	}

	public void kill() {
		synchronized (statusLock) {
			kill = true;
		}
	}

	public void run() {
		synchronized (statusLock) {
			pauses = 0;
			lastTickCount = System.currentTimeMillis();
		}

		while (!kill) {
			if (pauses == 0) {
				pulse();
			}
		}
	}

	public void addListener(final RealTimeListener listener) {
		synchronized (listenerLock) {
			listeners.add(listener);
		}
	}

	public String toString() {
		synchronized (this) {
			return currentDateTime.toString();
		}
	}

	public void setDateTime(final DateTime date_time) {
		synchronized (this) {
			currentDateTime = date_time;
			lastTickCount = System.currentTimeMillis();
		}
	}

	public DateTime getDateTime() {
		synchronized (this) {
			return currentDateTime.makeCopy();
		}
	}

	public void removeListener(final RealTimeListener listener) {
		synchronized (listenerLock) {
			listeners.remove(listener);
		}
	}

	public void removeAllListeners() {
		synchronized (listenerLock) {
			listeners.clear();
		}

	}

	public void setMultiplier(final int multiplier_) {
		multiplier = multiplier_;
	}

	public void resetMultiplier() {
		// multiplier = 245000;
		multiplier = 25000;
	}

	public long getLastTickCount() {
		return lastTickCount;
	}

	public int getPauses() {
		return pauses;
	}

}
