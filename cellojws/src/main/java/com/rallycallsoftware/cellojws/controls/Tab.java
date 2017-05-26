package com.rallycallsoftware.cellojws.controls;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.TabButton;
import com.rallycallsoftware.cellojws.controls.button.TabButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.token.CommandToken;

public class Tab extends Control {

	private List<TabButton> buttons = new ArrayList<>();

	private List<TabContainer> sheets = new ArrayList<>();

	private AbsDims containerDims;

	private int currentTab = 0;

	private FontInfo highlight;

	private boolean smallTabs;

	public Tab(final AbsDims dim) {
		super(dim, null);
		containerDims = new AbsDims(0, TabButtonType.getInstance().getHeight(), dim.getAbsWidth(), dim.getAbsHeight());

		highlight = environment.getButtonFontInfo().makeCopy();
		highlight.setTextColor(Color.WHITE);
	}

	@Override
	public void render(Graphics graphics, boolean mousedown) {

	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		return true;
	}

	@Override
	public void processMouseWheel(MouseWheelEvent wheelEvent) {

	}

	/**
	 * Sometimes we don't want to create a token for a tab because everything is
	 * preloaded
	 * 
	 * @param name
	 * @return
	 */
	public TabContainer addTab(final String name) {
		return addTab(name, new CommandToken<>(ControlController::noOp));
	}

	/**
	 * 
	 * Creates a new tab, adds it to the map and references it by the button
	 * name
	 * 
	 * @param name
	 * @param controls
	 */
	public TabContainer addTab(final String name, final CommandToken<?> token) {
		final TabContainer container = new TabContainer(containerDims, this);

		sheets.add(container);
		container.setToken(token != null ? token : new CommandToken<>(ControlController::noOp));

		final int buttonWidth = TabButtonType.getInstance().getWidth(name, environment.getButtonFontInfo());
		final int xPoint = buttons.size() * buttonWidth;

		final AbsDims dims = new AbsDims(xPoint, 0, xPoint + buttonWidth, TabButtonType.getInstance().getHeight());
		final CommandToken<TabButton> buttonToken = new CommandToken<>(ControlController::changeTab);
		final TabButton button = new TabButton(dims, name, buttonToken, this, container);
		buttonToken.setPayload(button);

		buttons.add(button);
		addControl(button);

		return container;
	}

	/*
	 * private AbsDims getNextButtonDims(final String text) { AbsDims next;
	 * 
	 * if( lastButtonDims == null ) { next = new AbsDims( 0, 0, getWidth(text),
	 * Button.getStandardButtonHeight() ); } else { next = new AbsDims(
	 * lastButtonDims.right, 0, lastButtonDims.right + getWidth(text),
	 * Button.getStandardButtonHeight() ); }
	 * 
	 * lastButtonDims = next; return next; }
	 */
	/*
	 * private int getWidth(final String text) { if( smallTabs ) { return
	 * (int)(Button.getStandardButtonWidth() * 0.85F); }
	 * 
	 * final int width = WindowManager.getTextWidth(text, getFontInfo());
	 * 
	 * if( width < Button.getStandardButtonWidth() - 62 ) { return
	 * Button.getStandardButtonWidth(); } else { return width + 62; } }
	 */

	public void setCurrentTab(final int index) {
		hideAllTabs();

		if (sheets.size() > index) {
			final Container sheet = sheets.get(index);

			addControl(sheet);
			currentTab = index;

			buttons.forEach(x -> {
				x.setFontInfo(getStandardFont());
				x.setMousedownOverride(false);
			});

			if (buttons.size() > index) {
				buttons.get(index).setFontInfo(highlight);
				buttons.get(index).setMousedownOverride(true);
			}
		}
	}

	private void hideAllTabs() {
		sheets.forEach(x -> {
			removeControl(x);
		});
	}

	public boolean isSmallTabs() {
		return smallTabs;
	}

	public void setSmallTabs(boolean smallTabs) {
		this.smallTabs = smallTabs;
	}

	public int getClientHeight() {
		return getDimensions().getAbsHeight() - Button.getStandardButtonHeight();
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Container getSheet(int sheet) {
		if (sheets.size() > sheet && sheet >= 0) {
			return sheets.get(sheet);
		}

		return null;
	}

	public void setCurrentTab(TabButton tabButton) {
		setCurrentTab(buttons.indexOf(tabButton));
	}

	public int getTabIndex(TabContainer container) {
		return sheets.indexOf(container);
	}

	public int getTabHeight() {
		return getDimensions().getAbsHeight() - getClientHeight();
	}

}
