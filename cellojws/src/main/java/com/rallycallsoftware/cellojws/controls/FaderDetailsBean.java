package com.rallycallsoftware.cellojws.controls;

public class FaderDetailsBean {
	private boolean loop;

	private int fadeInMillis;

	private int fadeOutMillis;

	private int opaqueMillis;

	private int invisibleMillis;

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public int getFadeInMillis() {
		return fadeInMillis;
	}

	public void setFadeInMillis(int fadeInMillis) {
		this.fadeInMillis = fadeInMillis;
	}

	public int getFadeOutMillis() {
		return fadeOutMillis;
	}

	public void setFadeOutMillis(int fadeOutMillis) {
		this.fadeOutMillis = fadeOutMillis;
	}

	public int getOpaqueMillis() {
		return opaqueMillis;
	}

	public void setOpaqueMillis(int opaqueMillis) {
		this.opaqueMillis = opaqueMillis;
	}

	public int getInvisibleMillis() {
		return invisibleMillis;
	}

	public void setInvisibleMillis(int invisibleMillis) {
		this.invisibleMillis = invisibleMillis;
	}

}