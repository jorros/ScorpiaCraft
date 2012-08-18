package de.scorpiacraft.obj;

import org.getspout.spoutapi.gui.GenericListWidget;

public class SListWidget extends GenericListWidget implements SControl
{
private String name;
	
	public SListWidget(String name)
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
