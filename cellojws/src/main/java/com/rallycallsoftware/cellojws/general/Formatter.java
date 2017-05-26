package com.rallycallsoftware.cellojws.general;

import java.text.DecimalFormat;

public class Formatter {
	public static String DollarsMillions(final int value) {
		final long in100Thousands = Math.round(value / 100000D);

		final DecimalFormat df = new DecimalFormat("#.0");
		return "$" + df.format(in100Thousands / 10D) + "M";
	}
}
