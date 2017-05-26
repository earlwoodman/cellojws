package com.rallycallsoftware.cellojws.controls;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;

public class DoubleLabel extends Control {

	private String firstText;

	private String secondText;

	private FontInfo firstFont;

	private FontInfo secondFont;

	private Label firstLabel;

	private Label secondLabel;

	public DoubleLabel(final AbsDims dims, final String firstText, final String secondText, final FontInfo firstFont,
			final FontInfo secondFont) {
		super(dims);

		final AbsDims firstDims = new AbsDims(0, 0, dims.getAbsWidth() / 2, dims.getAbsHeight());
		firstLabel = new Label(firstDims);
		addControl(firstLabel);

		final AbsDims secondDims = new AbsDims(dims.getAbsWidth() / 2, 0, dims.getAbsWidth(), dims.getAbsHeight());
		secondLabel = new Label(secondDims);
		addControl(secondLabel);

		setFirstText(firstText);
		setSecondText(secondText);

		setFirstFont(firstFont);
		setSecondFont(secondFont);

	}

	public String getFirstText() {
		return firstText;
	}

	public void setFirstText(String firstText) {
		this.firstText = firstText;
		firstLabel.setText(firstText);
	}

	public String getSecondText() {
		return secondText;
	}

	public void setSecondText(String secondText) {
		this.secondText = secondText;
		secondLabel.setText(secondText);
	}

	public FontInfo getFirstFont() {
		return firstFont;
	}

	public void setFirstFont(FontInfo firstFont) {
		this.firstFont = firstFont;
		firstLabel.setFontInfo(firstFont);
	}

	public FontInfo getSecondFont() {
		return secondFont;
	}

	public void setSecondFont(FontInfo secondFont) {
		this.secondFont = secondFont;
		secondLabel.setFontInfo(secondFont);
	}

}
