/*
 * Created on 2011-04-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.general;

import java.io.Serializable;

public class City implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 70L;

    private String name;
       		
    private float timeZoneFromEST;

    private LatLong latLong;
    
    private String withStateProv;
    
    public City(final String name_, final float timeZoneFromEST_, final double lat, final double longitude_, final String withStateProv_)
    {
        setName(name_);
        setTimeZoneFromEST(timeZoneFromEST_);
        setLatLong(latLong);
        setWithStateProv(withStateProv_);
    }
    
	public City(final String name_, final float timeZoneFromEST_)
    {
        setName(name_);
        setTimeZoneFromEST(timeZoneFromEST_);
    }
    
    public String getWithStateProv() 
    {
		return withStateProv;
	}

	public void setWithStateProv(String withStateProv) 
	{
		this.withStateProv = withStateProv;
	}
	
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public float getTimeZoneFromEST()
    {
        return timeZoneFromEST;
    }

    public void setTimeZoneFromEST(final float timeZoneFromEST)
    {
        this.timeZoneFromEST = timeZoneFromEST;
    }
    
    @Override
    public String toString()
    {
    	return name;
    }

	public double getLatitude() 
	{
		return latLong != null ? latLong.getLatitude() : 0D;
	}

	public void setLatitude(double latitude) 
	{
		ensureLatLong();
		
		latLong.setLatitude(latitude);
	}

	public double getLongitude() 
	{
		return latLong != null ? latLong.getLongitude() : 0D;
	}

	public void setLongitude(double longitude) 
	{
		ensureLatLong();
		
		latLong.setLongitude(longitude);
	}
    
    public LatLong getLatLong() 
    {
		return latLong;
	}

	public void setLatLong(LatLong latLong) 
	{
		this.latLong = latLong;
	}

	private void ensureLatLong() 
	{
		if( latLong == null )
		{
			latLong = new LatLong();			
		}	
	}


    
}
