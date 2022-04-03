package com.cellojws.general.work;

import com.cellojws.general.core.Environment;

public class ShowDemoWork implements Work 
{

	@Override
	public boolean doWork() 
	{
		final Environment gameEnvironment = Environment.getEnvironment();
		//gameEnvironment.showDemo();
		
		return true;
	}

}
