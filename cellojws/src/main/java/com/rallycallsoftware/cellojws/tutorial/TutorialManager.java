package com.rallycallsoftware.cellojws.tutorial;

import java.util.HashMap;
import java.util.Map;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.tutorial.TutorialItem;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class TutorialManager {

	private static Map<String, TutorialItem> tutorialItems = new HashMap<String, TutorialItem>();

	public static AbsDims getTutorialDimensions(final AbsDims parent) {
		final AbsDims tutorialDims = new AbsDims((parent.getAbsWidth() - 400) / 2, (parent.getAbsHeight() - 250) / 2,
				(parent.getAbsWidth() - 400) / 2 + 400, (parent.getAbsHeight() - 250) / 2 + 250);

		return tutorialDims;
	}

	public static TutorialItem getTutorialItem(final String name, final String message, final AbsDims tutorialDims) {

		if (message != null && tutorialDims != null) {
			final TutorialItem item = new TutorialItem(message, tutorialDims);
			if (!tutorialItems.containsKey(name)) {
				tutorialItems.put(name, item);
				return item;
			}
		}

		return null;
	}

	public static TutorialItem getTutorialItem(final String name) {
		if (tutorialItems.get(name) != null) {
			return tutorialItems.get(name);
		}

		return null;
	}

	public static void setupTutorialItem(final AbsDims tutorialDims, final String name, final String message,
			final Environment environment, final WindowManager windowManager) {
		TutorialItem item = getTutorialItem(name);

		if (environment.isTutorialMode()) {
			if (item == null) {
				item = TutorialManager.getTutorialItem(name, message, tutorialDims);
			}

			if (!item.isShown()) {
				windowManager.setTutorialItem(item);
				item.setShown(true);
			}
		}
	}

}
