package de.scorpiacraft.obj;

import org.getspout.spoutapi.gui.GenericButton;

public class SButton extends GenericButton implements SControl
{
	private String name;
	
	public SButton(String text, String name)
	{
		super(text);
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
