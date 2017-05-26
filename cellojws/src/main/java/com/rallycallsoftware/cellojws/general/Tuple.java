package com.rallycallsoftware.cellojws.general;

import java.io.Serializable;

public class Tuple<T, U> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2586863333000738623L;

	private T item1;

	private U item2;

	public Tuple(T item1, U item2) {
		this.item1 = item1;
		this.item2 = item2;
	}

	public T getItem1() {
		return item1;
	}

	public void setItem1(T item1) {
		this.item1 = item1;
	}

	public U getItem2() {
		return item2;
	}

	public void setItem2(U item2) {
		this.item2 = item2;
	}

}