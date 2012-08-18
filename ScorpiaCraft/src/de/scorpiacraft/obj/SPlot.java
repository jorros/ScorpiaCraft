package de.scorpiacraft.obj;

import org.bukkit.entity.Player;

import de.scorpiacraft.features.Plot.PlotType;

public class SPlot
{
	private String holder;
	private PlotType type;
	private int price;
	
	public String getHolder()
	{
		if(this.holder != null)
			return holder;
		else
			return "";
	}
	
	public SPlot setPrice(int price)
	{
		this.price = price;
		return this;
	}
	
	public int getPrice()
	{
		return this.price;
	}
	
	public SPlot setHolder(String player)
	{
		this.holder = player;
		return this;
	}
	
	public SPlot setHolder(Player player)
	{
		return this.setHolder(player.getName());
	}
	
	public SPlot setHolder(SPlayer player)
	{
		return this.setHolder(player.getName());
	}
	
	public PlotType getType()
	{
		if(this.type != null)
			return this.type;
		else
			return PlotType.None;
	}
	
	public SPlot setType(PlotType type)
	{
		this.type = type;
		return this;
	}
}
