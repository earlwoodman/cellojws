/*
 * Created on 2010-10-07
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Event implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 34L;

	private String text;

	private DateTime time;

	private EventLevel level;

	public enum EventLevel {
		notice, important, critical
	}

	public Event(final EventLevel level_, final String text_, final DateTime time_) {
		text = text_;
		time = time_;
		level = level_;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(final DateTime time) {
		this.time = time;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public EventLevel getLevel() {
		return level;
	}

	public String toString() {
		return (time != null ? time.toStringDateOnly() : "") + " " + text;
	}

	public static List<? extends Event> getXEvents(final List<? extends Event> events, final int x) {
		if (x <= 0) {
			return Collections.unmodifiableList(new ArrayList<Event>());
		}

		final int lowBound = x >= events.size() ? 0 : events.size() - x;

		return events.subList(lowBound, events.size());
	}

}
