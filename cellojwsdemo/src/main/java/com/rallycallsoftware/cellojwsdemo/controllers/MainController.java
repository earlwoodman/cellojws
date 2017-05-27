package com.rallycallsoftware.cellojwsdemo.controllers;

import com.rallycallsoftware.cellojws.token.CommandToken;

public class MainController {

	public static void exit(final CommandToken<?> token)
	{
		System.exit(0);
	}
}
