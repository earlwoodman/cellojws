package com.cellojws.stock.messagescreen;

import com.cellojws.general.image.Image;
import com.cellojws.token.CommandToken;

public class MessageScreenBean
{
	private CommandToken<?> clientToken;
	
	private String message;
	
	private Image image;

	private MessageScreenType messageScreenType;
	
	public CommandToken<?> getClientToken()
	{
		return clientToken;
	}

	public void setClientToken(CommandToken<?> clientToken)
	{
		this.clientToken = clientToken;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public MessageScreenType getMessageScreenType()
	{
		return messageScreenType;
	}

	public void setMessageScreenType(MessageScreenType messageScreenType)
	{
		this.messageScreenType = messageScreenType;
	}

}
