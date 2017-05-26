package com.rallycallsoftware.cellojws.controls.listbox;

import java.text.DecimalFormat;

public class CurrencyListBoxItem extends StringListBoxItem {

	private int value;

	public CurrencyListBoxItem(int value) {
		super();

		setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		final DecimalFormat df1 = new DecimalFormat("$#,###.##");
		setCaption(df1.format((float) value / 1000000F) + "M");
	}

}
