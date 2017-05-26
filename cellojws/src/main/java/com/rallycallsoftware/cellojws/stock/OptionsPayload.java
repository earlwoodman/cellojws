package com.rallycallsoftware.cellojws.stock;

import com.rallycallsoftware.cellojws.controls.CheckBox;

public class OptionsPayload {

	private String key;

	private boolean value;

	private OptionsScreen optionsScreen;

	private CheckBox checkBox;

	public OptionsPayload() {

	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public OptionsScreen getOptionsScreen() {
		return optionsScreen;
	}

	public void setOptionsScreen(OptionsScreen optionsScreen) {
		this.optionsScreen = optionsScreen;
	}

}
