package com.rallycallsoftware.cellojws.general;

import java.io.Serializable;

public class MonthDayTuple extends Tuple<Integer, Integer>implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2019720770102696376L;

	public MonthDayTuple(Integer month, Integer day) {
		super(month, day);
	}

	public int getMonth() {
		return getItem1();
	}

	public int getDay() {
		return getItem2();
	}

	public DateTime createDateTime(final int year) {
		return new DateTime(year, getMonth(), getDay());
	}
}
