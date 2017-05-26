/*
 * Created on 2010-09-12
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.rallycallsoftware.cellojws.logging.WorkerLog;

public class PersonNameFactory {

	private static Collection<String> lastNames = new ArrayList<String>();

	private static Collection<String> maleFirstNames = new ArrayList<String>();

	private static Collection<String> femaleFirstNames = new ArrayList<String>();

	private static boolean initialized = false;

	private PersonNameFactory() {

	}

	public static String getMaleFirstName(final String executionPath) {
		return getName(maleFirstNames, executionPath);
	}

	public static String getFemaleFirstName(final String executionPath) {
		return getName(femaleFirstNames, executionPath);
	}

	public static String getLastName(final String executionPath) {
		return getName(lastNames, executionPath);
	}

	private static String getName(final Collection<String> names, final String executionPath) {
		if (!initialized) {
			initialize(executionPath);
			initialized = true;
		}

		final int index = Random.getRandIntInclusive(0, names.size() - 1);

		if (names != null && names.size() > 0) {
			return (String) names.toArray()[index];
		}

		return null;
	}

	private static void initialize(final String executionPath) {
		if (executionPath == null) {
			WorkerLog.error("Could not open name file because executionPath is null.");
			return;
		}

		loadNames(lastNames, executionPath, "last-names.txt");
		loadNames(maleFirstNames, executionPath, "male-first-names.txt");
		loadNames(femaleFirstNames, executionPath, "female-first-names.txt");

	}

	private static void loadNames(final Collection<String> names, final String executionPath, final String filename) {
		final File file = new File(executionPath + "/Data/" + filename);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				names.add(line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase());
			}
		} catch (Exception e) {
			WorkerLog.error("Could not open name file: " + file.getAbsolutePath());
			for (StackTraceElement element : e.getStackTrace()) {
				WorkerLog.error(element.toString());
			}
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				WorkerLog.error("Could not close name file: " + file.getAbsolutePath());
			}
		}
	}
}
