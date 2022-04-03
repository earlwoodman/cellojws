package com.rallycallsoftware.cellojws.stock;

import java.awt.Point;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.ImageFactory;
import com.rallycallsoftware.cellojws.token.CommandToken;


public class DemoNotice extends Screen
{

	private Button close;
	
	private Control imageCtrl;
	
	public DemoNotice() 
	{
		super(Environment.getSmallPopupDims(),
			  Environment.getSmallPopupImage());
		
		final AbsDims dims = Environment.getSmallPopupDims();			
		
		final AbsDims imageCtrlDims = new AbsDims(25, 0, dims.getAbsWidth() - 25, dims.getAbsHeight() - 50);
		imageCtrl = new Control(
					imageCtrlDims,
					new CommandToken<Object>(ControlController::showWebsite)
				);
		imageCtrl.setImage(ImageFactory.getImage("Demo.png"));
		addControl(imageCtrl);
		
		close = new BasicButton(SmallButtonType.getInstance(), "Close", endGame(), new Point((dims.getAbsWidth() - 125) / 2, dims.getAbsHeight() - 60));
		addControl(close);

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
		return endGame();
	}

	@Override
	public CommandToken<?> escapeKeyPressed() 
	{
		return endGame();
	}

	private CommandToken<?> endGame() 
	{
		return new CommandToken<DemoNotice>(ControlController::exitDemo);
	}

	public void exitDemo()
	{
		Environment.getEnvironment().exit();
	}

}
