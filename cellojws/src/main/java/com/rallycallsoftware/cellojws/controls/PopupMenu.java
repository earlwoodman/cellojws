package com.rallycallsoftware.cellojws.controls;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.Gradient.Direction;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class PopupMenu<T> extends Control implements TransientControl {

	private List<PopupMenuItem<T>> items = new ArrayList<>();

	private PopupMenuItem<T> lastMousedOver;

	private FontInfo fontInfo;

	public PopupMenu() {
		fontInfo = environment.getMenuFontInfo();
	}

	public void addItem(final PopupMenuItem<T> item) {
		items.add(item);
	}

	public void reset() {
		items = new ArrayList<>();
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {
		super.render(graphics, mousedown);

		final int itemHeight = getItemHeight();
		final int height = itemHeight * items.size();

		final AbsDims dims = getScreenDims().makeCopy();
		dims.bottom = dims.top + height;
		graphics.drawGradientRect(Color.BLACK, Color.DARK_GRAY, dims, Direction.Downward);

		int i = 0;
		for (final PopupMenuItem<T> item : items) {
			final AbsDims itemDims = new AbsDims();
			itemDims.left = dims.left;
			itemDims.right = dims.right;
			itemDims.top = dims.top + (i++ * itemHeight);
			render(graphics, item, itemDims);
		}

	}

	private int getItemHeight() {
		return Control.getStdGap() + WindowManager.getTextHeight(fontInfo) + Control.getStdGap();
	}

	private void render(final Graphics graphics, final PopupMenuItem<T> item, final AbsDims dims) {

		graphics.setFontInfo(fontInfo);
		final Color saved = fontInfo.getTextColor();
		if (lastMousedOver == item) {
			fontInfo.setTextColor(Color.WHITE);
		}
		graphics.drawText(item.getMessage(), dims.left + Control.getStdGap(), dims.top + Control.getStdGap());

		fontInfo.setTextColor(saved);
	}

	@Override
	public boolean doSpecialMoveActions(int x, int y) {
		boolean ret = super.doSpecialMoveActions(x, y);

		int item = y / getItemHeight();
		if (item >= 0 && item < items.size()) {
			lastMousedOver = items.get(item);
		}

		return ret;
	}

	public String getLongestMessage() {
		final Comparator<PopupMenuItem<T>> comp = (p1, p2) -> Integer.compare(p1.getMessage().length(),
				p2.getMessage().length());

		return items.stream().max(comp).get().getMessage();
	}

	@Override
	public CommandToken<?> getToken() {
		if (lastMousedOver != null) {
			return lastMousedOver.getToken();
		}

		return null;
	}

}
