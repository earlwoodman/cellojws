package com.cellojws.stock;

import com.cellojws.controls.CompletionCallback;
import com.cellojws.controls.Control;
import com.cellojws.controls.kenburns.KBControl;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.Image;
import com.cellojws.token.CommandToken;

public class KenBurnsScreen extends Screen implements CompletionCallback
{

	private CompletionCallback callback = null;
	
	private KBControl main;
	
	private String overlay;
	
	private Control overlayCtrl;
	
	public KenBurnsScreen(final CompletionCallback callback_, final String file, final String overlay_) 
	{
		super(Environment.getFullScreenDims(), null);
		
		AbsDims dims = Environment.getFullScreenDims();
		callback = callback_;
		
		overlay = overlay_;
		overlayCtrl = new Control(dims, null);
		overlayCtrl.setImage(new Image("/" + overlay, true));
		
		main = new KBControl(file, dims, null);
		addControl(main);
		addControl(overlayCtrl);		
		
	}

	public void start()
	{
		main.start();
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

	@Override
	public void completed() 
	{
		if( callback != null )
		{
			callback.completed();
		}
	}

}
