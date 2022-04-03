/*
 * Created on 2011-05-08
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.general.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.cellojws.adapter.Graphics;
import com.cellojws.dimensions.AbsDims;
import com.cellojws.general.FileHelper;
import com.cellojws.general.core.Environment;
import com.cellojws.logging.WorkerLog;


public class Image implements Serializable
{

	/**
     * 
     */
    private static final long serialVersionUID = 155L;

	private static final int IMAGE_SIZE_CUTOFF = 300000000; // in bytes

    private BufferedImage image;
    
    private String filename;
    
    private boolean loaded;
    
    private AbsDims drawDimensions;
 
	private boolean loadFailed = false;
	
	private String shortFilename;
	
	private String filenameSansExtension;
	
	private Integer height = null;
	
	private Integer width = null;

	private Collection<String> failed = new ArrayList<>();

	private static Map<String, Image> images = new HashMap<String, Image>();

	public Image(BufferedImage image)
	{
		this.image = image;		
		loaded = true;
		width = image.getWidth();
		height = image.getHeight();
	}
	
    public Image(final String filename_, final boolean load)
    {
    	initialize(filename_, load);
    }
        
    public Image(final String filename_)
    {
    	initialize(filename_, true);
    }
    
    private void initialize(final String filename_, final boolean load)
    {
    	if( failed.contains(filename_) )
    	{
    		return;
    	}
    	
        loaded = false;
        String executionPath = Environment.getExecutionPath();
        if( !filename_.startsWith(executionPath) )
        {
        	filename = FileHelper.joinPathAndFile(executionPath + "/Graphics/", filename_);
        }
        else
    	{
        	filename = filename_;    
    	}
        setShortFilename(filename);
        if( load )
        {
            load();
        }
        
        if( !loadFailed )
        {
        	images.put(filename, this);
        }
        else
        {
        	failed.add(filename_);
        }
    }
    
	private void setShortFilename(String filename) 
	{
		int lastIndex = -1;
		
		lastIndex = filename.lastIndexOf("/");
		if( filename.lastIndexOf("\\") > lastIndex )
		{
			lastIndex = filename.lastIndexOf("\\");			
		}
		
		shortFilename = filename.substring(lastIndex + 1);
		
		filenameSansExtension = shortFilename.substring(0, shortFilename.lastIndexOf('.'));
	}

	public void load()
    {
    	if( loaded )
    	{
    		return;
    	}
    	
        try 
        {        	
        	checkImageMapSize();
            image = ImageIO.read(new File(filename));
            loaded = true;
            loadFailed = false;
        }
        catch(IOException e)
        {
            loaded = false;
            loadFailed = true;
        }
            
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public void render(final Graphics graphics, final AbsDims dims)
    {
        graphics.drawImage(image, dims);
    }
    
    public BufferedImage getBufferedImage()
    {
    	if( !loaded )
    	{
    		load();
    	}
        return image;
    }
    
    public int getWidth()
    {
    	if( width == null )
    	{
	    	SimpleImageInfo imageInfo = null;
			try {
				imageInfo = new SimpleImageInfo(new File(filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if( imageInfo != null )
			{
				width = imageInfo.getWidth();
			}
    	}
    	
		return width;

    }
    
    public int getHeight()
    {
    	if( height == null )
    	{    		    
	    	SimpleImageInfo imageInfo = null;
			try {
				imageInfo = new SimpleImageInfo(new File(filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if( imageInfo != null )
			{
				height = imageInfo.getHeight();
			}
    	}
				
		return height;

    }
    
    public AbsDims getFillingDims(final AbsDims clientDims)
    {
    	if( image == null )
    	{
    		return null;
    	}
    	
    	final float aspectRatioImage = (float)getWidth() / getHeight();
    	final float aspectRatioClient = (float)clientDims.getAbsWidth() / clientDims.getAbsHeight(); 
    	
    	final AbsDims ret = new AbsDims();
    	if( aspectRatioImage < aspectRatioClient )
    	{
    		// Does it reach all the way to the top?
    		if( getHeight() >= clientDims.getAbsHeight() )
    		{
    			ret.top = 0;
    			ret.bottom = clientDims.getAbsHeight();
    			ret.left = (int)(clientDims.getAbsWidth() - clientDims.getAbsHeight() * aspectRatioImage) / 2; 
    			ret.right = (int)(ret.left + clientDims.getAbsHeight() * aspectRatioImage);
    			return ret;
    		}
    	}
    	else if( aspectRatioImage > aspectRatioClient )
    	{
    		// Does it reach all the way to the left side?
    		if( getHeight() >= clientDims.getAbsHeight() )
    		{
    			ret.top = (int)(clientDims.getAbsHeight() - clientDims.getAbsWidth() * aspectRatioImage) / 2;
    			ret.bottom = (int)(ret.top + clientDims.getAbsWidth() * aspectRatioImage);
    			ret.left = 0;
    			ret.right = clientDims.getAbsWidth();
    			return ret;
    		}
    	}
    	else
    	{
    		return clientDims;
    	}
    	
		ret.top = (clientDims.getAbsHeight() - getHeight()) / 2;
		ret.bottom = ret.top + getHeight();
		ret.left = (clientDims.getAbsWidth() - getWidth()) / 2;
		ret.right = ret.left + getWidth();

		return ret;
    }
    
    @Override
    public String toString()
    {
    	return filenameSansExtension;
    }

	public void unload() 
	{
		System.out.println("Unloading: " + filename + System.currentTimeMillis());
		loaded = false;
		image = null;
	}

	public void reload() 
	{
		unload();
		load();
	}

    public static void checkImageMapSize() 
    {
    	int total = 0;
    	for( Image image : images.values() )
    	{
    		if( image.isLoaded() )
    		{
    			total += image.getHeight() * image.getWidth() * 4; // 4 bpp in 32-bit images
    		}
    	}
    	
    	if( total > IMAGE_SIZE_CUTOFF )
    	{
    		WorkerLog.debug("Current memory load: " + (int)(((float)total / IMAGE_SIZE_CUTOFF) * 100) + "%");
    		// Unload everything. There's too much in memory
    		WorkerLog.debug("Unloading everything. Memory full.");
        	for( Image image : images.values() )
        	{
        		image.unload();
        	}
        	WorkerLog.debug("Current memory load: " + (int)(((float)total / IMAGE_SIZE_CUTOFF) * 100) + "%");
    	}
    	
	}

    public AbsDims getDrawDimensions() 
    {
		return drawDimensions;
	}

	public void setDrawDimensions(AbsDims drawDimensions) 
	{
		this.drawDimensions = drawDimensions;
	}

    public String getFilename() 
    {
		return filename;
	}

    public boolean isLoadFailed() 
    {
		return loadFailed;
	}

	public static Image get(String name) 
	{
		if( images != null && images.containsKey(name) )
		{
			return images.get(name);
		}
		
		return null;
	}

	public String getShortFilename() 
	{
		return shortFilename;
	}

	public String getPath() 
	{
		String lastSlashType = "/";
		if( filename.lastIndexOf("\\") > filename.lastIndexOf("/") )
		{
			lastSlashType = "\\";
		}
		
		return filename.substring(0, filename.lastIndexOf(lastSlashType));
	}
   
}
