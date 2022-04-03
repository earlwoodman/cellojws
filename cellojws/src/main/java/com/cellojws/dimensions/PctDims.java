package com.cellojws.dimensions;

import com.cellojws.general.core.Environment;
import com.cellojws.windowing.WindowManager;

public class PctDims extends AbsDims
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8946119556073946868L;
	
	public PctDims(int left, int top, int right, int bottom)
	{
		super(left, top, right, bottom);
	}

	@Override
	public PctDims makeCopy() 
    {
    	return new PctDims(left, top, right, bottom);
	}    

	@Override
    public int getAbsHeight()
    {
		
        return (int)((bottom - top) * totalHeight() / 100F);
    }
    
	@Override	
    public int getAbsWidth()
    {
        return (int)((right - left) * totalWidth() / 100F);
    }

	@Override
	public AbsDims absoluteify()
	{		
		
		final int totalWidth = totalWidth();
		final int totalHeight = totalHeight();
		
		AbsDims dimensions = new AbsDims();
		dimensions.left = (int)(left * totalWidth / 100F);
		dimensions.top = (int)(top * totalHeight / 100F);
		dimensions.right = (int)(right * totalWidth / 100F);
		dimensions.bottom = (int)(bottom * totalHeight / 100F);
		
		return dimensions;
	}

    public int totalWidth()
	{
    	final Environment env = Environment.getEnvironment();
		final WindowManager windowManager = env.getWindowManager();
		
		return windowManager.getScreenWidth();
	}

	public int totalHeight()
	{
    	final Environment env = Environment.getEnvironment();
		final WindowManager windowManager = env.getWindowManager();
		
		return windowManager.getScreenHeight();

	}

}
