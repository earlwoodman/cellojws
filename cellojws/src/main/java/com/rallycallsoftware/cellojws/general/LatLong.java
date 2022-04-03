package com.rallycallsoftware.cellojws.general;

public class LatLong 
{
	
    private double latitude;
    
    private double longitude;

    public LatLong()
    {
    	
    }
    
    public LatLong(final double lat, final double long_)
    {
    	setLatitude(lat);
    	setLongitude(long_);
    }
    
	public double getLatitude() 
	{
		return latitude;
	}

	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}

	public double getLongitude() 
	{
		return longitude;
	}

	public void setLongitude(double longitude) 
	{
		this.longitude = longitude;
	}
    
}
