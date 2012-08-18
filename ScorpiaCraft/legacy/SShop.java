package de.scorpiacraft.obj;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SShop
{
	private String owner, name;
	private Location loc;
	private Map<String, SItem> items = new HashMap<String, SItem>();
	
	public List<SItem> getItems()
	{
		LinkedList<SItem> list = new LinkedList<SItem>();
		if(items == null || items.isEmpty())
			return list;
		for(SItem item : items.values())
		{
			list.add(item.setShop(this));
		}
		return list;
	}
	
	public SItem addItem(SpoutItemStack item, int price)
	{
		String id = item.getMaterial().getNotchianName();
		SItem it = null;
		if(this.items.containsKey(id))
		{
			it = this.items.get(id);
			it.setAmount(it.getAmount() + item.getAmount());
			it.setPrice(price);
			items.put(id, it);
		}
		else
		{
			it = (new SItem()).setAmount(item.getAmount()).setName(id).setPrice(price);
			items.put(id, it);
		}
		return it;
	}
	
	public SShop removeItem(String item)
	{
		this.items.remove(item);
		return this;
	}
	
	public SShop removeItem(String item, int amount)
	{
		SItem it = this.items.get(item);
		if(it.getAmount() - amount <= 0)
			this.items.remove(item);
		else
			this.items.put(item, it.setAmount(it.getAmount()-amount));
		return this;
	}
	
	public String getOwner()
	{
		return this.owner;
	}
	
	public SShop setLocation(Location loc)
	{
		this.loc = loc;
		return this;
	}
	
	public SShop setLocation(String loc)
	{
		String[] pos = loc.split(",");
		this.loc = new Location(Bukkit.getWorld("world"), Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
		return this;
	}
	
	public Location getLocation()
	{
		return this.loc;
	}
	
	public SShop setOwner(String owner)
	{
		this.owner = owner;
		return this;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public SShop setName(String name)
	{
		this.name = name;
		return this;
	}
}
