package com.rallycallsoftware.cellojwsdemo.views;

import java.awt.Color;
import java.awt.event.KeyEvent;

import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.Window;
import com.rallycallsoftware.cellojwsdemo.controllers.MainController;

public class Wallpaper extends Window {

	
	public Wallpaper(final AbsDims dim) {
		super(dim, null);
		
		setBackground(Color.GRAY.darker());
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

}
