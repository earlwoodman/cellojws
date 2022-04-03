package com.cellojws.stock;

import java.awt.Point;

import com.cellojws.controls.Control;
import com.cellojws.controls.ControlController;
import com.cellojws.controls.Label;
import com.cellojws.controls.button.BasicButton;
import com.cellojws.controls.button.Button;
import com.cellojws.controls.button.SmallButtonType;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.core.Environment;
import com.cellojws.general.image.Image;
import com.cellojws.general.image.ImageFactory;
import com.cellojws.token.CommandToken;


public class About extends Screen
{

	private Button close;
	
    private Label programming;
    
    private Label graphics;
    
    private Label music;
    
    private Label programmingName;
    
    private Label graphicsName;
    
    private Label musicName;
    
    private Control teamLogo;

	
	public About()
	{
		super(Environment.getSmallPopupDims(),
			  Environment.getSmallPopupImage());
		
		final AbsDims dims = Environment.getSmallPopupDims();
			
		final CommandToken<?> closeToken = createCloseToken();
		
		close = new BasicButton(
				SmallButtonType.getInstance(), 
				"Close", 
				closeToken,
				new Point((dims.getAbsWidth() - 125) / 2, dims.getAbsHeight() - 60));
		
		addControl(close);

        final Image logoImg = ImageFactory.getImage("logo/HockeyBrass.png");
		final AbsDims logoDims = new AbsDims(
				(dims.getAbsWidth() - 300) / 2,
				95,
				(dims.getAbsWidth() - 300) / 2 + 300,
				95 + 103 
				);
		teamLogo = new Control(logoDims, null);
		teamLogo.setImage(logoImg);
		addControl(teamLogo);

		final int widthLessLogo = dims.getAbsWidth();
		final int heightLessButtons = close.getDimensions().top - logoDims.bottom; 
				
		final int nameWidth = 500;
		final int column1Left = (widthLessLogo - nameWidth) / 2 + 50;
		final int column1Right = column1Left + nameWidth / 2;
		final int column2Left = column1Right + 1;
		final int column2Right = column1Right + nameWidth / 2;
		
		final int rowHeight = 15;
		final int rowGap = 15;
		final int row1Top = (heightLessButtons - 3 * rowHeight - 2 * rowGap) / 2 + logoDims.bottom;
		final int row1Bottom = row1Top + rowHeight;				
		final int row2Top = row1Bottom + rowGap;
		final int row2Bottom = row2Top + rowHeight;
		final int row3Top = row2Bottom + rowGap;
		final int row3Bottom = row3Top + rowHeight;
		
		final AbsDims programmingDims = new AbsDims(
				column1Left, row1Top, column1Right, row1Bottom
				);
		programming = new Label(programmingDims, "Programming By:");
		addControl(programming);

		final AbsDims graphicsDims = new AbsDims(
				column1Left, row2Top, column1Right, row2Bottom
				);
		graphics = new Label(graphicsDims, "Graphics By:");
		addControl(graphics);

		final AbsDims musicDims = new AbsDims(
				column1Left, row3Top, column1Right, row3Bottom
				);
		music = new Label(musicDims, "Music By:");
		addControl(music);

		final AbsDims programmingNameDims = new AbsDims(
				column2Left, row1Top, column2Right, row1Bottom 
				);
		programmingName = new Label(programmingNameDims, "Earl Woodman");
		addControl(programmingName);

		final AbsDims graphicsNameDims = new AbsDims(
				column2Left, row2Top, column2Right, row2Bottom 
				);
		graphicsName = new Label(graphicsNameDims, "Judy Mesh");
		addControl(graphicsName);

		final AbsDims musicNameDims = new AbsDims(
				column2Left, row3Top, column2Right, row3Bottom 
				);
		musicName = new Label(musicNameDims, "Jayme McDonald");
		addControl(musicName);

	}

	public CommandToken<About> createCloseToken()
	{
		return new CommandToken<About>(ControlController::close, this);
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
		return escapeKeyPressed();
	}

	@Override
	public CommandToken<?> escapeKeyPressed()
	{
		return createCloseToken();
	}

}
