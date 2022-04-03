/*
 * Created on 2011-05-08
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general.image;

import java.util.HashMap;
import java.util.Map;

import com.rallycallsoftware.cellojws.general.FileHelper;
import com.rallycallsoftware.cellojws.general.core.Environment;

public class ImageFactory
{
	
	private static Image listBoxRow = null;
	
	private static Map<Float, Image> smallPopups = new HashMap<>();
	
	private static Map<Float, Image> largePopups = new HashMap<>();
	
	private static Image dropShadowImage;
	
	private static Image tutorialBackground = null;
	
	private static Image explosion = null;
	
    private static Map<String, Image> characters = new HashMap<String, Image>();
            
	public static Image getListBoxRow()
	{
		if( listBoxRow == null )
    	{
    		listBoxRow = new Image("ListBoxRow.png", true);
    	}
    	
    	return listBoxRow;
	}
	
	public static Image getDropShadow()
	{
		if( dropShadowImage == null )
    	{
    		dropShadowImage = new Image("/Popup/drop-shadow.png", true);
    	}
    	
    	return dropShadowImage;
	}
	
	public static Image getSmallPopup()
	{
		final float scaling = Environment.getScaling();
		
		Image popup = smallPopups.get(scaling);
		if( popup == null )
		{
			popup = new Image("/popup/small-popup-" + Environment.getScalingString() + ".png", true);
			smallPopups.put(scaling, popup);
		}
		
		return popup;
	}

	public static Image getLargePopup()
	{
		final float scaling = Environment.getScaling();
		
		Image popup = largePopups.get(scaling);
		if( popup == null )
		{
			popup = new Image("/popup/large-popup-" + Environment.getScalingString() + ".png", true);
			largePopups.put(scaling, popup);
		}
		
		return popup;
	}
	
	public static Image getTutorialBackground()
	{
		if( tutorialBackground == null )
    	{
    		tutorialBackground = new Image("/tutorialBackground.png", true);
    	}
    	
    	return tutorialBackground;
	}
	
	public static Image getExplosion()
	{
		if( explosion == null )
    	{
    		explosion = new Image("/Achievements/Explosion.png", true);
    	}
    	
    	return explosion;
	}	

	public static Image getImage(final ImageType imageType, final boolean isHighDPI, final MouseMovement mouseMovement) 
	{
		final String name = buildImageName(imageType, isHighDPI, mouseMovement);
		
		return getImage(name);	
	}
	
	public static Image getImage(final String name)
	{
		String path = Environment.getExecutionPath() + "/Graphics";
		
		final String joined = FileHelper.joinPathAndFile(path, name);
		
		Image image = Image.get(joined);
		if( image == null )
		{
			image = new Image(joined, true);
		}
		
		return image;	
	}
	
	private static String buildImageName(ImageType imageType, boolean isHighDPI, MouseMovement mouseMovement) 
	{
		return imageType.getFilename() + "/" + imageType.getFilename() + mouseMovement.getFilename() + (isHighDPI ? "_high" : "_low") + ".png";  
	}

	public static Image getCharacter(final String string, final boolean highlight)
    {
        if( string == null )
        {
            return null;          
        }
        
        String searchString = convertCharacter(string);
        
        Image ret = null;
        if( string != null )
        {
            
            if( string.toLowerCase().equals(searchString) && containsSomeLetters(searchString) )
            {
                searchString = string + "-sm";
            }
            
            if( highlight )
            {
                if( characters.get(searchString + "highlight") == null )
                {
                    characters.put(searchString + "highlight", new Image("/Alphabet/highlight/" + searchString + ".png", true));
                }
                ret = characters.get(searchString + "highlight");
            }
            else
            {
                if( characters.get(searchString) == null )
                {
                    characters.put(searchString, new Image("/Alphabet/" + searchString + ".png", true));
                }
                ret = characters.get(searchString);
            }
        }
                
        if( ret != null && !ret.isLoaded() )
        {
            return getCharacter("NOTFOUND", highlight);
        }
        return ret;
        
    }

    // Returns true if the string contains at least 1 letter 
    private static boolean containsSomeLetters(String searchString)
    {
        for( int i = 0; i < searchString.length(); i++ )
        {
            if( "ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(searchString.toUpperCase().substring(i, i + 1)) )
            {
                return true;
            }
        }
        
        return false;
    }

    private static String convertCharacter(final String string)
    {
        // Replace with name for a special character
        
        if( string != null )
        {
            if( string.equals(" ") )
            {
                return "space";
            }
            if( string.equals("!") )
            {
                return "excl";
            }
            if( string.equals("@") )
            {
                return "at";
            }
            if( string.equals("#") )
            {
                return "pound";
            }
            if( string.equals("$") )
            {
                return "dollar";
            }
            if( string.equals("%") )
            {
                return "percent";
            }
            if( string.equals("^") )
            {
                return "caret";
            }
            if( string.equals("&") )
            {
                return "amp";
            }
            if( string.equals("*") )
            {
                return "asterisk";
            }
            if( string.equals("(") )
            {
                return "left-p";
            }
            if( string.equals(")") )
            {
                return "right-p";
            }
            if( string.equals(",") )
            {
                return "comma";
            }
            if( string.equals(".") )
            {
                return "period";
            }
            if( string.equals("/") )
            {
                return "slash";
            }
            if( string.equals(";") )
            {
                return "semicolon";
            }
            if( string.equals("'") )
            {
                return "apos";
            }
            if( string.equals("[") )
            {
                return "left-bracket";
            }
            if( string.equals("]") )
            {
                return "right-bracket";
            }
            if( string.equals("\\") )
            {
                return "backslash";
            }
            if( string.equals("<") )
            {
                return "lessthan";
            }
            if( string.equals(">") )
            {
                return "greaterthan";
            }
            if( string.equals("?") )
            {
                return "question";
            }
            if( string.equals(":") )
            {
                return "colon";
            }
            if( string.equals("\"") )
            {
                return "quotes";
            }
            if( string.equals("{") )
            {
                return "left-brace";
            }
            if( string.equals("}") )
            {
                return "right-brace";
            }
            if( string.equals("|") )
            {
                return "pipe";
            }
            if( string.equals("_") )
            {
                return "underscore";
            }
        }
     
        return string;        
                    
    }

}
