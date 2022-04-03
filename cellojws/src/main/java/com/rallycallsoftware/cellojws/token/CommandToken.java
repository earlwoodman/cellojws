/*
 * Created on 2010-08-07
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.token;

import java.util.function.Consumer;


public class CommandToken<E>
{
  
    private Consumer<CommandToken<E>> action;
    
    private E payload;
    
    public CommandToken(final Consumer<CommandToken<E>> action, final E payload)
    {
    	this.action = action;
    	this.payload = payload;
    }
    
	public CommandToken(final Consumer<CommandToken<E>> action)
    {
    	this.action = action;
    }
    
	public boolean execute() 
    {
    	action.accept(this);
    	
    	return true;
    }

	public E getPayload()
	{
		return payload;
	}

	public void setPayload(E payload)
	{
		this.payload = payload;
	}

}
