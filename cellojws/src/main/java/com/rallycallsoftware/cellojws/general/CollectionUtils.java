package com.rallycallsoftware.cellojws.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {

	/**
	 * Checks whether a collection contains duplicates (using ==, not .equals())
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean containsDuplicates(final Collection<? extends Object> collection) {

		if (collection == null || collection.size() == 0) {
			return false;
		}

		// Add all items to a set. If the set contains fewer than the original
		// collection
		// that means there were duplicates.

		final Set<Object> newColl = new HashSet<Object>();

		newColl.addAll(collection);

		return newColl.size() != collection.size();
	}

	/**
	 * Returns all objects in the given collection that are of the given type
	 * 
	 * @param <E>
	 * 
	 * @param collection
	 * @param clazz
	 * @return
	 */

	public static <E> Collection<E> getAllByType(final Collection<? extends Object> collection, final Class<E> clazz) {
		if (collection == null) {
			return null;
		}

		final Collection<E> ret = new ArrayList<E>();
		for (final Object object : collection) {
			if (clazz.isInstance(object)) {
				ret.add(clazz.cast(object));
			}
		}

		return ret;
	}

	public static Object selectRandom(final Collection<? extends Object> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}

		return collection.toArray()[Random.getRandIntInclusive(0, collection.size() - 1)];

	}
}
