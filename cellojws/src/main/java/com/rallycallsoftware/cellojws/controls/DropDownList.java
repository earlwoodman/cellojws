package com.rallycallsoftware.cellojws.controls;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import com.rallycallsoftware.cellojws.adapter.Graphics;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.ButtonType;
import com.rallycallsoftware.cellojws.controls.button.DownScrollButtonType;
import com.rallycallsoftware.cellojws.controls.listbox.ListBox;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class DropDownList extends ListBox {

	private AbsDims containerRolledUpDims;

	private AbsDims containerDroppedDownDims;

	private boolean droppedDown = false;

	private Label selection;

	private Button downArrow;

	// Cache the captions here, since we will need them again.
	private List<String> captions;

	public DropDownList(final AbsDims droppedDownDims_, final List<String> captions_, final CommandToken<?> token) {
		super(droppedDownDims_, captions_, token, true, false, null);

		captions = captions_;

		containerDroppedDownDims = getDimensions().makeCopy();

		containerRolledUpDims = new AbsDims(getDimensions().left, getDimensions().top, getDimensions().right,
				getDimensions().top + WindowManager.getTextHeight(environment.getListBoxFontInfo()) + 5);

		final CommandToken<DropDownList> dropDown = new CommandToken<DropDownList>(ControlController::dropDown, this);

		final AbsDims selectionDims = new AbsDims(Control.getStdGap(), 0, containerRolledUpDims.getAbsWidth(),
				containerRolledUpDims.getAbsHeight());
		selection = new Label(selectionDims, "", dropDown);

		downArrow = new BasicButton(DownScrollButtonType.getInstance(), "", dropDown,
				new Point(
						containerRolledUpDims.getAbsWidth() - Control.getStdGap()
								- DownScrollButtonType.getInstance().getWidth(),
						(containerRolledUpDims.getAbsHeight() - DownScrollButtonType.getInstance().getHeight()) / 2));

		select(0);
		rollUp();
	}

	@Override
	public boolean doSpecialClickActions(int x, int y) {
		if (droppedDown) {
			super.doSpecialClickActions(x, y);
			rollUp();
		}

		return true;
	}

	public void rollUp() {
		setDimensions(containerRolledUpDims);
		droppedDown = false;
		addControl(selection);
		addControl(downArrow);
		if (getSelectedIndex() > -1) {
			selection.setText(captions.get(getSelectedIndex()));
		}
		hideScrollBar();
	}

	public void dropDown() {
		setDimensions(containerDroppedDownDims);
		droppedDown = true;
		removeControl(selection);
		removeControl(downArrow);
		unhideScrollBar();
		((Window) getWindow()).childDroppingDown(this);
	}

	@Override
	public synchronized void render(Graphics graphics, boolean mousedown) {
		if (droppedDown) {
			super.render(graphics, mousedown);
		} else {
			graphics.drawRect(getScreenDims(), Color.DARK_GRAY);
		}
	}

	@Override
	public void select(int item) {
		super.select(item);

		if (droppedDown == false) {
			selection.setText(captions.get(item));
		}
	}

}
