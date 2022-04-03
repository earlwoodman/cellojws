package com.cellojws.controls.button;

import java.util.List;

import com.cellojws.token.CommandToken;

public class RadioButtonInfoBean 
{
	private List<Button> buttons;
	
	private Button clickedButton;
	
	private String caption;
	
	private CommandToken<?> token;

	public List<Button> getButtons() 
	{	
		return buttons;
	}

	public void setButtons(List<Button> buttons) 
	{
		this.buttons = buttons;
	}

	public Button getClickedButton() 
	{
		return clickedButton;
	}

	public void setClickedButton(Button clickedButton) 
	{
		this.clickedButton = clickedButton;
	}

	public String getCaption()
	{
		return caption;
	}

	public void setCaption(String caption)
	{
		this.caption = caption;
	}

	public CommandToken<?> getToken()
	{
		return token;
	}

	public void setToken(CommandToken<?> token)
	{
		this.token = token;
	}	
	
}
