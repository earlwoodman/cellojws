module demo {
	exports com.earljw.demo;
	exports com.earljw.demo.screens;
	exports com.earljw.demo.controllers;

	requires transitive java.desktop;
	requires transitive cellojws;
}