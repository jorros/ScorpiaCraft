package de.scorpiacraft.obj;

import org.getspout.spoutapi.gui.GenericPopup;

public class SPopup extends GenericPopup implements SControl
{
	private String name;
	
	public SPopup(String name)
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
