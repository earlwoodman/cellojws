package com.cellojws.controls;

import com.cellojws.token.CommandToken;

public class PopupMenuItem<T> 
{

	private String message;
	
	private CommandToken<T> token;
	
	public PopupMenuItem() 
	{
		
	}

	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) 
	{
		this.message = message;
	}

	public CommandToken<T> getToken() 
	{
		return token;
	}

	public void setToken(CommandToken<T> token) 
	{
		this.token = token;
	}
	
	
}
