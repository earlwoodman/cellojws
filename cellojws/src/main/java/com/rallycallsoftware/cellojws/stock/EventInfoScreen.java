package com.rallycallsoftware.cellojws.stock;

import java.awt.Point;

import com.rallycallsoftware.cellojws.controls.Control;
import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.SmallButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.Event;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.general.image.Image;
import com.rallycallsoftware.cellojws.logging.WorkerLog;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;


public class EventInfoScreen extends Screen 
{

	private Event event;
	
	private Label caption;
	
	private Button close;
	
	private Label title;
	
	private Control logoCtr;

	public EventInfoScreen(final AbsDims dim, final Event event_, final Image logo) 
	{
		super(dim, Environment.getSmallPopupImage());
		
		event = event_;
		
		if( logo == null )
		{
			WorkerLog.error("This screen requires an image!");
			return;
		}
		
		final int gap = Environment.smallGap();
		
	    final AbsDims logoDims = new AbsDims(
        		gap, 
        		gap, 
        		gap + logo.getWidth(), 
        		gap + logo.getHeight()
        		);
        logoCtr = new Control(logoDims, null);
        logoCtr.setImage(logo);
        addControl(logoCtr);
		
		final FontInfo override = Environment.getLabelFontInfo().makeCopy();
		override.setFontSize(36);
		final AbsDims titleDims = new AbsDims(
				gap, 
				gap,
				gap + dim.getAbsWidth(),  
				WindowManager.getTextHeight(override));
		title = new Label(titleDims, "Notice", Justification.Center);
		title.setFontInfo(override);

        final FontInfo largeLabelFont = Environment.getListBoxFontInfo().makeCopy();
		largeLabelFont.setFontSize(22);
		final AbsDims captionDims = new AbsDims(
				logoDims.right + gap, 
				titleDims.bottom + gap,
				dim.getAbsWidth() - gap,  
				dim.getAbsHeight() - gap);
		caption = new Label(captionDims, event.toString(), Justification.Center);
		caption.setWrap(true);
		caption.setFontInfo(largeLabelFont);
		addControl(caption);
		
		close = new BasicButton(SmallButtonType.getInstance(), "Close", new CommandToken<Object>(ControlController::close), new Point((dim.getAbsWidth() - 125) / 2, dim.getAbsHeight() - 60));
		addControl("close", close);
		
		addControl(title);
				
	}

	@Override
	public CommandToken<?> enterKeyPressed()
	{
		return new CommandToken<Object>(ControlController::close);
	}

	@Override
	public CommandToken<?> escapeKeyPressed()
	{
		return enterKeyPressed();
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

}
