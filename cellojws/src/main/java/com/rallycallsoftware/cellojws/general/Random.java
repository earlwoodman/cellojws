/*
 * Created on 2010-06-19
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

import java.util.List;
import java.util.Map;

import com.rallycallsoftware.cellojws.logging.WorkerLog;

public final class Random {

	private static java.util.Random random = new java.util.Random();

	private Random() {
	}

	public static int getRandIntInclusive(final int lower_bound, final int upper_bound) {
		if (lower_bound == upper_bound) {
			return lower_bound;
		}

		if (upper_bound < lower_bound) {
			WorkerLog.error("Cannot call Random.getRandIntInclusive with upper bound lower than lower bound.");
			return 0;
		}

		final double rand_val = getRand();

		return (int) ((rand_val * (upper_bound - lower_bound) + lower_bound) + 0.5);

	}

	public static double getRand() {
		return Math.random();
	}

	public static int getNormalRand(final float mean, final float standardDev) {
		return (int) (random.nextGaussian() * standardDev + mean);
	}

	public static int getNormalRightHandSide(final int lower_bound, final int upper_bound, final int standardDev) {
		int ret = 0;
		do {
			ret = getNormalRand(lower_bound, standardDev);
		} while (ret > upper_bound || ret < lower_bound);

		return ret;
	}

	public static float getRandInclusiveBetween0And1(final float lowerBound, final float upperBound) {
		if (upperBound < lowerBound) {
			return -1F;
		}

		if (upperBound == lowerBound) {
			return lowerBound;
		}

		return (upperBound - lowerBound) * (float) getRand() + lowerBound;

	}

	/**
	 * Choose an element from the map given that the values are weights
	 * 
	 * @return
	 */
	public static <T> T randomElement(final Map<T, Integer> elements) {
		final int total = elements.values().stream().mapToInt(Integer::intValue).sum();

		final int rnd = getRandIntInclusive(1, total);

		int running = 0;
		for (final T element : elements.keySet()) {
			running += elements.get(element);
			if (rnd <= running) {
				return element;
			}
		}

		throw new RuntimeException("Could not select random element.");
	}

	public static <T> T randomElement(final List<T> elements) {
		if (elements == null || elements.size() == 0) {
			return null;
		}

		final int rnd = getRandIntInclusive(0, elements.size() - 1);

		return elements.get(rnd);
	}
}
