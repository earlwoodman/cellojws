package com.cellojws.controls.button;

import java.awt.Color;

import com.cellojws.adapter.Graphics;
import com.cellojws.controls.Control;
import com.cellojws.dimensions.AbsDims;

public class RadioButtonType extends FixedSizeButtonType 
{
	private boolean checked;

	private AbsDims savedDims;

	private AbsDims innerDims;
	
	private static Integer size = null;
		
	private boolean circle;

	private AbsDims outerDims;
	
	public RadioButtonType(final boolean checked, final boolean circle)
	{
		confirmSize();
		this.checked = checked;
		this.circle = circle;
	}
		
	private void confirmSize()
	{
		if( size == null )
		{
			size = SmallButtonType.getInstance().getHeight();
		}
	}

	public RadioButtonType()
	{
		confirmSize();
		this.checked = false;
	}

	@Override
	public int getHeight()
	{
		return size;
	}

	@Override
	public int getWidth() 
	{
		return size;
	}

	public void setChecked(boolean checked) 
	{
		this.checked = checked;
	}

	public boolean isChecked()
	{
		return this.checked;
	}

	public void toggleChecked() 
	{
		this.checked = !this.checked;
	}

	@Override
	public void renderMousedown(final Graphics graphics, final AbsDims dims, final Color colour)
	{
		drawRadioButton(graphics, dims, colour);
	}

	private void drawRadioButton(final Graphics graphics, final AbsDims dims, final Color colour)
	{
		if( !dims.equals(savedDims) )
		{
			savedDims = dims;
			outerDims = savedDims.makeCopy();
			outerDims.right = outerDims.getAbsHeight() + outerDims.left;
			innerDims = outerDims.makeCopy();
			innerDims.shrink(Math.round(dims.getAbsHeight() / 5F));
		}

		if( circle )
		{
			graphics.drawSolidCircle(outerDims, colour);
			if( checked )
			{
				graphics.drawSolidCircle(innerDims, colour.brighter().brighter());
			}
		}
		else
		{
			graphics.drawSolidRect(outerDims, colour);
			if( checked )
			{
				graphics.drawSolidRect(innerDims, colour.brighter().brighter());
			}			
		}
	}

	@Override
	public void renderNormal(final Graphics graphics, final AbsDims dims, final Color colour) 
	{
		drawRadioButton(graphics, dims, colour);
	}

	@Override
	public void renderMouseover(final Graphics graphics, final AbsDims dims, final Color colour)
	{
		drawRadioButton(graphics, dims, colour);
	}

	public static int getRadioGap()
	{
		return Control.getStdGap(); 
	}
	
}
