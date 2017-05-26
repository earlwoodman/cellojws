package com.rallycallsoftware.cellojws.general.core;

import java.util.EnumMap;
import java.util.Map;

public class LanguageManager {
	public enum Language {
		English, French;
	}

	private static Map<LangKey, String> english = new EnumMap<LangKey, String>(LangKey.class);
	private static Map<LangKey, String> french = new EnumMap<LangKey, String>(LangKey.class);

	public static String get(final Language language, final LangKey key) {
		return english.get(key);
	}

	static {
		english.put(LangKey.New, "New");
		english.put(LangKey.Options, "Options");
		english.put(LangKey.Exit, "Exit");
		english.put(LangKey.Country, "New England");
		english.put(LangKey.ProvNL, "Newfoundland");
		english.put(LangKey.ProvNS, "Nova Scotia");
		english.put(LangKey.ProvNB, "New Brunswick");
		english.put(LangKey.ProvME, "Maine");
		english.put(LangKey.ProvNH, "New Hampshire");
		english.put(LangKey.ProvVT, "Vermont");
		english.put(LangKey.ProvCT, "Connecticut");
		english.put(LangKey.ProvMS, "Massachusetts");
		english.put(LangKey.ProvRI, "Rhode Island");

		english.put(LangKey.CitySJ, "St. John's");
		english.put(LangKey.CityHfx, "Halifax");
		english.put(LangKey.CityFrd, "Fredericton");
		english.put(LangKey.CityFrd, "Augusta");
		english.put(LangKey.CityCnc, "Concord");
		english.put(LangKey.CityMtp, "Montpelier");
		english.put(LangKey.CityHfd, "Hartford");
		english.put(LangKey.CityBos, "Boston");
		english.put(LangKey.CityPrv, "Providence");

		french.put(LangKey.New, "Nouveau");
		french.put(LangKey.Options, "Options");
		french.put(LangKey.Exit, "Sortie");
		french.put(LangKey.Country, "");
	}
}
