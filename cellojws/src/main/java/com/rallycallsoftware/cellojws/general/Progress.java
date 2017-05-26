package com.rallycallsoftware.cellojws.general;

public interface Progress {
	void setProgress(final float progress);

	void setProgressMessage(final String message);

	void turnOffProgress();
}
