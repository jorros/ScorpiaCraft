package de.scorpiacraft.obj;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SClass
{
	private String name;
	private Map<String, Integer> xp = new HashMap<String, Integer>();
	private Map<String, Integer> ability = new HashMap<String, Integer>();
	private ChatColor color = ChatColor.WHITE;
	private Location choice = null;
	
	public SClass setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public SClass setChoiceLocation(Location loc)
	{
		this.choice = loc;
		return this;
	}
	
	public Location getChoiceLocation()
	{
		return this.choice;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getExperience(String type, Player player)
	{
		if(this.xp.containsKey(type))
		{
			return this.xp.get(type) + (int)(player.getLevel() * 0.25);
		}
		else
			return 0;
	}
	
	public boolean hasAbility(String type, Player player)
	{
		return (ability.containsKey(type) && player.getLevel() >= ability.get(type));
	}
	
	public ChatColor getColor()
	{
		return this.color;
	}
}
