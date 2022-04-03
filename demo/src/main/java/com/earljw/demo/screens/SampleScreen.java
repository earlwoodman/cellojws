/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.earljw.demo.screens;

import java.awt.Point;

import com.cellojws.controls.ControlController;
import com.cellojws.controls.Label;
import com.cellojws.controls.button.BasicButton;
import com.cellojws.controls.button.Button;
import com.cellojws.controls.button.LargeButtonType;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FontInfo;
import com.cellojws.general.Justification;
import com.cellojws.general.core.Environment;
import com.cellojws.stock.Screen;
import com.cellojws.token.CommandToken;
import com.cellojws.windowing.WindowManager;

public class SampleScreen extends Screen
{
       
    private Button newLeague;
    
    private Button continueGame;
    
    private Button exit;        
    
    private Label earlyAccess;
        
    public SampleScreen(final AbsDims dims)
    {
        super(dims, null);
    
        setTitle("Sample Screen");
        
        final int buttonWidth = Button.getLargeButtonWidth();
        final int buttonSeparation = (dims.getAbsWidth() - 3 * buttonWidth) / 4;

        final int buttonsTop = Math.round(.865F * getDimensions().getAbsHeight());
        final Point customLeaguePt = new Point(
                getLeftCentered(buttonWidth * 3 + buttonSeparation * 2), 
                buttonsTop);
        newLeague = new BasicButton(LargeButtonType.getInstance(), "Button 1", null, customLeaguePt);
        
        final Point openSavedGamePt = new Point(
        		getLeftCentered(buttonWidth * 3 + buttonSeparation * 2) + buttonWidth + buttonSeparation,
        		buttonsTop);
        continueGame = new BasicButton(LargeButtonType.getInstance(), "Button 2", null, openSavedGamePt);

        final Point exitPt = new Point(
        		getLeftCentered(buttonWidth * 3 + buttonSeparation * 2) + buttonWidth * 2 + buttonSeparation * 2,
        		buttonsTop);
        exit = new BasicButton(LargeButtonType.getInstance(), "Button 3", null, exitPt);
        
        addControl("continueGame", continueGame);
        addControl("exit", exit);
        addControl("newLeague", newLeague);

        final FontInfo earlyAccessLabelFontInfo = Environment.getLabelFontInfo().makeCopy();
        earlyAccessLabelFontInfo.setDropShadow(Environment.getWindowingSystemLight());
        earlyAccessLabelFontInfo.setTextColor(Environment.getOffWhite());
        earlyAccessLabelFontInfo.setFontSize(42);
        
        final String earlyAccessText = "Demo";

        final int left = 0 - getScreenDims().left;
        final int top = 0 - getScreenDims().top;
        
        final AbsDims earlyAccessDims = new AbsDims(
        		left + getDblGap(), 
        		top + getDblGap(), 
        		left + getScreenWidth() - getDblGap(),
        		top + WindowManager.getTextHeight(earlyAccessLabelFontInfo)
        		);
        earlyAccess = new Label(earlyAccessDims, earlyAccessText, Justification.Right);
        earlyAccess.setFontInfo(earlyAccessLabelFontInfo);
        addControl(earlyAccess);
    }    
    
	@Override
    public void refresh()
    {
   
    }

    @Override
	public boolean validate() 
	{	
		return true;
	}

	@Override
	public CommandToken<?> enterKeyPressed()
	{
		return null;
	}

	@Override
	public CommandToken<?> escapeKeyPressed()
	{
		return new CommandToken<Object>(ControlController::exitGame);
	}

}
