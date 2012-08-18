package de.scorpiacraft.obj;

import org.getspout.spoutapi.gui.GenericTextField;

public class STextField extends GenericTextField implements SControl
{
	private String name;
	
	public STextField(String name)
	{
		super();
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	
}