package com.rallycallsoftware.cellojws.general;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.logging.WorkerLog;

public class Options implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1233410761750949345L;

	private List<String> optionNames = new ArrayList<String>();

	private List<Boolean> optionValues = new ArrayList<Boolean>();

	public void setOption(final String name, final boolean value) {
		if (!optionNames.contains(name)) {
			optionNames.add(name);
			optionValues.add(value);
		} else {
			optionValues.add(optionNames.indexOf(name), value);
			optionValues.remove(optionNames.indexOf(name) + 1);
		}
	}

	public boolean load(final String executionPath) {

		ObjectInputStream ois = null;
		try {
			final FileInputStream fis = new FileInputStream(executionPath + "/settings.bin");
			ois = new ObjectInputStream(fis);
			Options o = (Options) ois.readObject();
			if (o != null) {
				List<String> oNames = new ArrayList<String>();
				List<Boolean> oValues = new ArrayList<Boolean>();

				for (String name : o.optionNames) {
					oNames.add(name);
				}

				for (Boolean val : o.optionValues) {
					oValues.add(val);
				}

				optionNames = oNames;
				optionValues = oValues;
			}

			return true;
		} catch (IOException ioException) {
			System.out.println(ioException);
			return false;
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe);
			return false;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					// Silently go away
					WorkerLog.error(e.getMessage());
				}
			}
		}

	}

	public boolean save(final String executionPath) {
		try {
			final FileOutputStream fos = new FileOutputStream(executionPath + "/settings.bin");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.writeInt(1);
			oos.close();
		} catch (IOException ioException) {
			System.out.println(ioException);
		}

		return false;
	}

	public List<String> getAllOptionKeys() {
		return optionNames;
	}

	public List<Boolean> getAllOptionValues() {
		return optionValues;
	}

	public Boolean getOptionValue(final String message) {
		int i = 0;
		for (String test : optionNames) {
			if (test.equals(message)) {
				return optionValues.get(i);
			}
			i++;
		}

		return false;
	}

}
