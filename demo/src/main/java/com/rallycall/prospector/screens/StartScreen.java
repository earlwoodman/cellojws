/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycall.prospector.screens;

import java.awt.Point;

import com.rallycallsoftware.cellojws.controls.ControlController;
import com.rallycallsoftware.cellojws.controls.Label;
import com.rallycallsoftware.cellojws.controls.button.BasicButton;
import com.rallycallsoftware.cellojws.controls.button.Button;
import com.rallycallsoftware.cellojws.controls.button.LargeButtonType;
import com.rallycallsoftware.cellojws.dimensions.AbsDims;
import com.rallycallsoftware.cellojws.general.FontInfo;
import com.rallycallsoftware.cellojws.general.Justification;
import com.rallycallsoftware.cellojws.general.core.Environment;
import com.rallycallsoftware.cellojws.stock.Screen;
import com.rallycallsoftware.cellojws.token.CommandToken;
import com.rallycallsoftware.cellojws.windowing.WindowManager;

public class StartScreen extends Screen
{
       
    private Button newLeague;
    
    private Button continueGame;
    
    private Button exit;        
    
    private Label earlyAccess;
        
    public StartScreen(final AbsDims dims)
    {
        super(dims, null);
    
        setTitle("Start Screen");
        
        final int buttonWidth = Button.getLargeButtonWidth();
        final int buttonSeparation = (dims.getAbsWidth() - 3 * buttonWidth) / 4;

        final int buttonsTop = Math.round(.865F * getDimensions().getAbsHeight());
        final Point customLeaguePt = new Point(
                getLeftCentered(buttonWidth * 3 + buttonSeparation * 2), 
                buttonsTop);
        newLeague = new BasicButton(LargeButtonType.getInstance(), "New Game", null, customLeaguePt);
        
        final Point openSavedGamePt = new Point(
        		getLeftCentered(buttonWidth * 3 + buttonSeparation * 2) + buttonWidth + buttonSeparation,
        		buttonsTop);
        continueGame = new BasicButton(LargeButtonType.getInstance(), "Load Game", null, openSavedGamePt);

        final Point exitPt = new Point(
        		getLeftCentered(buttonWidth * 3 + buttonSeparation * 2) + buttonWidth * 2 + buttonSeparation * 2,
        		buttonsTop);
        exit = new BasicButton(LargeButtonType.getInstance(), "Exit", null, exitPt);
        
        addControl("continueGame", continueGame);
        addControl("exit", exit);
        addControl("newLeague", newLeague);

        final FontInfo earlyAccessLabelFontInfo = Environment.getLabelFontInfo().makeCopy();
        earlyAccessLabelFontInfo.setDropShadow(Environment.getWindowingSystemLight());
        earlyAccessLabelFontInfo.setTextColor(Environment.getOffWhite());
        earlyAccessLabelFontInfo.setFontSize(42);
        
        final String earlyAccessText = "Steam Early Access";

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
