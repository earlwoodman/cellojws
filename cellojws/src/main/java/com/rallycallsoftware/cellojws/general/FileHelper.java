package com.rallycallsoftware.cellojws.general;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.rallycallsoftware.cellojws.logging.WorkerLog;

public final class FileHelper {

	private FileHelper() {

	}

	/**
	 * Returns all files in a directory. Does not follow into subdirectories
	 * 
	 * @param directory
	 * @return
	 */
	public static List<String> getAllFilesInDirectory(final String directory) {

		final List<String> ret = new ArrayList<String>();

		final File folder = new File(directory);
		final File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				ret.add(file.getName().toString());
			}
		}

		return ret;
	}

	/**
	 * Loads the file and returns the contents as a List Not wise to use for
	 * large files as there is no buffering, paging, etc.
	 * 
	 * @param target
	 * @param file
	 */
	public static List<String> loadFileContentsToStringArray(final String fileName) {
		final Path path = Paths.get(fileName);

		try {
			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}
	}

	public static List<String> getAllLinesInFile(final String filename) {
		final File file = new File(filename);

		final List<String> ret = new ArrayList<String>();

		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				ret.add(scanner.nextLine());
			}
		} catch (NoSuchElementException nsee) {
			// Silently ignore
		} catch (Exception e) {
			WorkerLog.error(e.getMessage());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return ret;
	}

	/**
	 * Remove any trailing or leading slashes, add one and then join
	 * 
	 * @param path
	 * @param file
	 * @return
	 */
	public static String joinPathAndFile(String path, String file) {
		String firstPart = null;
		String secondPart = null;

		if (path.endsWith("/")) {
			firstPart = path.substring(0, path.length() - 1);
		} else if (path.endsWith("/")) {
			firstPart = path.substring(0, path.length() - 2);
		} else {
			firstPart = path;
		}

		if (file.startsWith("/")) {
			secondPart = file.substring(1);
		} else if (file.startsWith("\\")) {
			secondPart = file.substring(2);
		} else {
			secondPart = file;
		}

		return firstPart + "/" + secondPart;
	}

}
