/*
 * Created on 2011-05-08
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general.image;

import java.util.HashMap;
import java.util.Map;

import com.rallycallsoftware.cellojws.controls.button.ButtonImage;
import com.rallycallsoftware.cellojws.general.FileHelper;
import com.rallycallsoftware.cellojws.general.core.Environment;

public class ImageFactory {

	private static Map<String, ButtonImage> buttonImages = new HashMap<String, ButtonImage>();

	private static final int buttonSmallSize = 14;

	private static Image logoBackground = null;

	private static Image marker = null;

	private static Image blur = null;

	private static Image popupButtonPanel = null;

	private static Image popupPanel = null;

	private static Image listBoxRow = null;

	private static Image smallPopupLowDpi = null;

	private static Image smallPopupHighDpi = null;

	private static Image largePopupLowDpi = null;

	private static Image largePopupHighDpi = null;

	private static Image dropShadowHighDpi = null;

	private static Image dropShadowLowDpi = null;

	private static Image checkboxHighDpiChecked = null;

	private static Image checkboxLowDpiChecked = null;

	private static Image checkboxHighDpiUnchecked = null;

	private static Image checkboxLowDpiUnchecked = null;

	private static Image tutorialBackground = null;

	private static Map<String, Image> characters = new HashMap<String, Image>();

	public static Image getBlur() {
		if (blur == null) {
			blur = new Image("/blur.png", true);
		}

		return blur;

	}

	public static ButtonImage getButtonImage(final String key) {
		ButtonImage buttonImage = buttonImages.get(key);

		if (buttonImage == null) {
			buttonImage = new ButtonImage();
			buttonImages.put(key, buttonImage);

			buttonImage.setNormal(new Image(key + "_button_normal.png", true));
			buttonImage.setMousedown(new Image(key + "_button_pressed.png", true));
			buttonImage.setMouseover(new Image(key + "_button_mouseover.png", true));
		}

		return buttonImage;
	}

	public static Image getSmallButtonNormal() {
		return getButtonImage("small").getNormal();
	}

	public static Image getSmallButtonMouseover() {
		return getButtonImage("small").getMouseover();
	}

	public static Image getSmallButtonPressed() {
		return getButtonImage("small").getMousedown();
	}

	public static Image getLargeButtonNormal() {
		return getButtonImage("large").getNormal();
	}

	public static Image getLargeButtonPressed() {
		return getButtonImage("large").getMousedown();
	}

	public static Image getLargeButtonMouseover() {
		return getButtonImage("large").getMouseover();
	}

	public static Image getUpScrollButtonNormal() {
		return getButtonImage("up_scroll").getNormal();
	}

	public static Image getUpScrollButtonPressed() {
		return getButtonImage("up_scroll").getMousedown();
	}

	public static Image getDownScrollButtonNormal() {
		return getButtonImage("down_scroll").getNormal();
	}

	public static Image getDownScrollButtonPressed() {
		return getButtonImage("down_scroll").getMousedown();
	}

	public static Image getDeleteButtonNormal() {
		return getButtonImage("delete_scroll").getNormal();
	}

	public static Image getDeleteButtonPressed() {
		return getButtonImage("delete_scroll").getMousedown();
	}

	public static Image getDeleteButtonNormalSmall() {
		return getButtonImage("small_delete").getNormal();
	}

	public static Image getDeleteButtonPressedSmall() {
		return getButtonImage("small_delete").getMousedown();
	}

	public static Image getUpButtonNormal() {
		return getButtonImage("up").getNormal();
	}

	public static Image getUpButtonPressed() {
		return getButtonImage("up").getMousedown();
	}

	public static Image getDownButtonNormal() {
		return getButtonImage("down").getNormal();
	}

	public static Image getDownButtonPressed() {
		return getButtonImage("down").getMousedown();
	}

	public static Image getLogoBackground() {
		if (logoBackground == null) {
			logoBackground = new Image("logo_background.png", true);
		}

		return logoBackground;
	}

	public static Image getMarker() {
		if (marker == null) {
			marker = new Image("marker.png", true);
		}

		return marker;
	}

	public static Image getPopupButtonPanel() {
		if (popupButtonPanel == null) {
			popupButtonPanel = new Image("popup-button-panel.png", true);
		}

		return popupButtonPanel;
	}

	public static Image getPopupPanel() {
		if (popupPanel == null) {
			popupPanel = new Image("popup-panel.png", true);
		}

		return popupPanel;
	}

	public static Image getListBoxRow() {
		if (listBoxRow == null) {
			listBoxRow = new Image("ListBoxRow.png", true);
		}

		return listBoxRow;
	}

	public static Image getSmallPopupLowDpi() {
		if (smallPopupLowDpi == null) {
			smallPopupLowDpi = new Image("/Popup/small-popup-low-dpi.png", true);
		}

		return smallPopupLowDpi;
	}

	public static Image getSmallPopupHighDpi() {
		if (smallPopupHighDpi == null) {
			smallPopupHighDpi = new Image("/Popup/small-popup-high-dpi.png", true);
		}

		return smallPopupHighDpi;
	}

	public static Image getLargePopupLowDpi() {
		if (largePopupLowDpi == null) {
			largePopupLowDpi = new Image("/Popup/large-popup-low-dpi.png", true);
		}

		return largePopupLowDpi;
	}

	public static Image getLargePopupHighDpi() {
		if (largePopupHighDpi == null) {
			largePopupHighDpi = new Image("/Popup/large-popup-high-dpi.png", true);
		}

		return largePopupHighDpi;
	}

	public static Image getDropShadowHighDpi() {
		if (dropShadowHighDpi == null) {
			dropShadowHighDpi = new Image("/Popup/drop-shadow-high-dpi.png", true);
		}

		return dropShadowHighDpi;
	}

	public static Image getDropShadowLowDpi() {
		if (dropShadowLowDpi == null) {
			dropShadowLowDpi = new Image("/Popup/drop-shadow-low-dpi.png", true);
		}

		return dropShadowLowDpi;
	}

	public static Image getTutorialBackground() {
		if (tutorialBackground == null) {
			tutorialBackground = new Image("/tutorialBackground.png", true);
		}

		return tutorialBackground;
	}

	public static Image getCheckboxUnchecked() {
		final boolean highDpi = Environment.getEnvironment().isHighDPI();

		if (highDpi) {
			if (checkboxHighDpiUnchecked == null) {
				checkboxHighDpiUnchecked = new Image("/checkbox/unchecked-high-dpi.png", true);
			}
			return checkboxHighDpiUnchecked;
		} else {
			if (checkboxLowDpiUnchecked == null) {
				checkboxLowDpiUnchecked = new Image("/checkbox/unchecked-low-dpi.png", true);
			}
			return checkboxLowDpiUnchecked;
		}
	}

	public static Image getCheckboxChecked() {
		final boolean highDpi = Environment.getEnvironment().isHighDPI();

		if (highDpi) {
			if (checkboxHighDpiChecked == null) {
				checkboxHighDpiChecked = new Image("/checkbox/checked-high-dpi.png", true);
			}
			return checkboxHighDpiChecked;
		} else {
			if (checkboxLowDpiChecked == null) {
				checkboxLowDpiChecked = new Image("/checkbox/checked-low-dpi.png", true);
			}
			return checkboxLowDpiChecked;
		}
	}

	public static Image getImage(final ImageType imageType, final boolean isHighDPI,
			final MouseMovement mouseMovement) {
		final String name = buildImageName(imageType, isHighDPI, mouseMovement);

		return getImage(name);
	}

	public static Image getImage(final String name) {
		String path = Environment.getEnvironment().getExecutionPath() + "/Graphics";

		final String joined = FileHelper.joinPathAndFile(path, name);

		Image image = Image.get(joined);
		if (image == null) {
			image = new Image(joined, true);
		}

		return image;
	}

	private static String buildImageName(ImageType imageType, boolean isHighDPI, MouseMovement mouseMovement) {
		return imageType.getFilename() + "/" + imageType.getFilename() + mouseMovement.getFilename()
				+ (isHighDPI ? "_high" : "_low") + ".png";
	}

	public static Image getCharacter(final String string, final boolean highlight) {
		if (string == null) {
			return null;
		}

		String searchString = convertCharacter(string);

		Image ret = null;
		if (string != null) {

			if (string.toLowerCase().equals(searchString) && containsSomeLetters(searchString)) {
				searchString = string + "-sm";
			}

			if (highlight) {
				if (characters.get(searchString + "highlight") == null) {
					characters.put(searchString + "highlight",
							new Image("/Alphabet/highlight/" + searchString + ".png", true));
				}
				ret = characters.get(searchString + "highlight");
			} else {
				if (characters.get(searchString) == null) {
					characters.put(searchString, new Image("/Alphabet/" + searchString + ".png", true));
				}
				ret = characters.get(searchString);
			}
		}

		if (ret != null && !ret.isLoaded()) {
			return getCharacter("NOTFOUND", highlight);
		}
		return ret;

	}

	// Returns true if the string contains at least 1 letter
	private static boolean containsSomeLetters(String searchString) {
		for (int i = 0; i < searchString.length(); i++) {
			if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(searchString.toUpperCase().substring(i, i + 1))) {
				return true;
			}
		}

		return false;
	}

	private static String convertCharacter(final String string) {
		// Replace with name for a special character

		if (string != null) {
			if (string.equals(" ")) {
				return "space";
			}
			if (string.equals("!")) {
				return "excl";
			}
			if (string.equals("@")) {
				return "at";
			}
			if (string.equals("#")) {
				return "pound";
			}
			if (string.equals("$")) {
				return "dollar";
			}
			if (string.equals("%")) {
				return "percent";
			}
			if (string.equals("^")) {
				return "caret";
			}
			if (string.equals("&")) {
				return "amp";
			}
			if (string.equals("*")) {
				return "asterisk";
			}
			if (string.equals("(")) {
				return "left-p";
			}
			if (string.equals(")")) {
				return "right-p";
			}
			if (string.equals(",")) {
				return "comma";
			}
			if (string.equals(".")) {
				return "period";
			}
			if (string.equals("/")) {
				return "slash";
			}
			if (string.equals(";")) {
				return "semicolon";
			}
			if (string.equals("'")) {
				return "apos";
			}
			if (string.equals("[")) {
				return "left-bracket";
			}
			if (string.equals("]")) {
				return "right-bracket";
			}
			if (string.equals("\\")) {
				return "backslash";
			}
			if (string.equals("<")) {
				return "lessthan";
			}
			if (string.equals(">")) {
				return "greaterthan";
			}
			if (string.equals("?")) {
				return "question";
			}
			if (string.equals(":")) {
				return "colon";
			}
			if (string.equals("\"")) {
				return "quotes";
			}
			if (string.equals("{")) {
				return "left-brace";
			}
			if (string.equals("}")) {
				return "right-brace";
			}
			if (string.equals("|")) {
				return "pipe";
			}
			if (string.equals("_")) {
				return "underscore";
			}
		}

		return string;

	}

	public static int getButtonSmallSize() {
		return buttonSmallSize;
	}

}
