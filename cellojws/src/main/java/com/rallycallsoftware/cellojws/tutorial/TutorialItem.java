package com.rallycallsoftware.cellojws.tutorial;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;

public class TutorialItem {
	private String message;

	private AbsDims screenDims;

	private boolean shown;

	public TutorialItem(final String message_, final AbsDims screenDims_) {
		setMessage(message_);
		setScreenDims(screenDims_);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AbsDims getScreenDims() {
		return screenDims;
	}

	public void setScreenDims(AbsDims screenDims) {
		this.screenDims = screenDims;
	}

	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

}
