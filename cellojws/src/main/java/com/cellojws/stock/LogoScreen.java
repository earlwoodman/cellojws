package com.cellojws.stock;

import java.awt.Color;

import com.cellojws.controls.CompletionCallback;
import com.cellojws.controls.FaderDetailsBean;
import com.cellojws.controls.ImageFader;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.Image;
import com.cellojws.token.CommandToken;

public class LogoScreen extends Screen 
{
	
	private ImageFader fader;

	private CompletionCallback callback;
	
	public LogoScreen(CompletionCallback callback_) 
	{
		super(Environment.getFullScreenDims(), null);
		
		callback = callback_;
		
		final FaderDetailsBean bean = new FaderDetailsBean();
		bean.setLoop(false);
		bean.setFadeInMillis(800);
		bean.setFadeOutMillis(1400);
		bean.setOpaqueMillis(400);
		bean.setInvisibleMillis(0);
		final Image image = new Image("/Logo/Logo.png", true);
		fader = new ImageFader(bean, image.getFillingDims(getScreenDims()), null, callback);
		fader.setImage(image);
		addControl(fader);
		setImage(null);
		setBackground(Color.WHITE);
	}
	
	@Override
	public void refresh() 
	{
	
	}

	@Override
	public boolean validate() 
	{
		return false;
	}

	@Override
	public CommandToken<?> enterKeyPressed()
	{
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed() 
	{
		return null;
	}

}
