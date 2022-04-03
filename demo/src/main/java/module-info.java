module demo {
	exports com.rallycall.prospector;
	exports com.rallycall.prospector.screens;
	exports com.rallycall.prospector.controllers;

	requires transitive java.desktop;
	requires transitive cellojws;
}