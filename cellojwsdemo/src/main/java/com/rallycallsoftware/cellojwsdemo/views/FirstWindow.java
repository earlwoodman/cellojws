package com.rallycallsoftware.cellojwsdemo.views;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;

import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojwsdemo.controllers.FirstWindowController;
import com.rallycallsoftware.cellojwsdemo.controllers.MainController;
import com.rallycallsoftware.cellojwsdemo.models.FirstWindowBean;

public class FirstWindow extends Window<FirstWindowBean> {

	private Button clickMe;
	
	public FirstWindow(final AbsDims dim, final FirstWindowBean bean) {
		super(dim, null, bean);
		
		setBackground(Color.BLUE.brighter());
        setTitle("First Window");
        
		final int halfInch = environment.halfInch();
		
		final CommandToken<?> clickMeToken = new CommandToken<>(FirstWindowController::clickMe);
		final Point clickMePt = new Point(halfInch, halfInch);
		clickMe = new BasicButton(SmallButtonType.getInstance(), "Click Me", clickMeToken, clickMePt);
		addControl(clickMe);
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
