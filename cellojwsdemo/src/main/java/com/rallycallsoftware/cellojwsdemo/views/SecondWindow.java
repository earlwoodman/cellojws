package com.rallycallsoftware.cellojwsdemo.views;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.rallycallsoftware.cellojws.controls.listbox.ListBox;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojwsdemo.controllers.MainController;
import com.rallycallsoftware.cellojwsdemo.models.SecondWindowBean;

public class SecondWindow extends Window<SecondWindowBean> {

	private ListBox months;
	
	public SecondWindow(final AbsDims dim, final SecondWindowBean bean) {
		super(dim, null, bean);
		
		setBackground(Color.YELLOW.darker());
        setTitle("Second Window");
        
		final int halfInch = environment.halfInch();
		
		final AbsDims monthsDims = new AbsDims(
				halfInch, halfInch, dim.getAbsWidth() - halfInch, dim.getAbsHeight() - halfInch);
		months = new ListBox(monthsDims, null);
		addControl(months);
	}

	@Override
	public CommandToken<?> enterKeyPressed() {
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed() {
		return new CommandToken<Object>(MainController::exit);
	}

	@Override
	public CommandToken<?> processKeyPress(KeyEvent keyEvent) {
		return null;
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public boolean validate() {
		return false;
	}

}
