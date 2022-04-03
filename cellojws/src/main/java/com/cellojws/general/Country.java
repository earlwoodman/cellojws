package com.cellojws.general;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public enum Country 
{
	Unknown(0, "Unknownish"),
	CanadaE(400, "Canadian"),
	CanadaF(100, "Canadian"),
	Canada(0, "Canadian"),
	USA(250, "American"),
	UK(10, "British"),
	Czechia(100, "Czech"),
	Sweden(100, "Swedish"),
	Finland(100, "Finnish"),
	Russia(150, "Russian"),
	France(15, "French"),
	Austria(30, "Austrian"), 
	Germany(20, "German"),
	Slovakia(75, "Slovak"),
	Kazakhstan(15, "Kazakhstani"), 
	Norway(10, "Norwegian"), 
	Switzerland(25, "Swiss"),
	Denmark(10, "Danish"),
	Netherlands(10, "Dutch"),
	Slovenia(15, "Slovene"),
	Latvia(10, "Latvian");
	
	private int ratio;
	
	private String demonym;
	
	public int getRatio()
	{
		return ratio;
	}
	
	public String getDemonym()
	{
		return demonym;
	}
	
	private Country(final int ratio, final String demonym)
	{
		this.ratio = ratio;
		this.demonym = demonym;
	}
	
	public static int totalRatios()
	{
		final Collection<Country> countries = Arrays.asList(Country.class.getEnumConstants());
		
		return countries.stream().mapToInt(x -> x.getRatio()).sum();		
	}
	
	public static List<Integer> weights()
	{
		final Collection<Country> countries = Arrays.asList(Country.class.getEnumConstants());

		return countries.stream().mapToInt(x -> x.getRatio()).boxed().collect(Collectors.toList());
	}
}
