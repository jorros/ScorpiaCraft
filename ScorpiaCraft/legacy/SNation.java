package de.scorpiacraft.obj;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.dynmap.markers.AreaMarker;
import org.getspout.spoutapi.player.SpoutPlayer;

import de.scorpiacraft.Helper;
import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Nations;

public class SNation extends Object
{
	private String name, tagname;
	private Map<String, Double> multipliers = new HashMap<String, Double>();
	private Location spawn;
	private String world;
	private ChatColor color;
	private String ability;
	private LinkedList<String> region = new LinkedList<String>();
	private LinkedList<String> capital = new LinkedList<String>();
	private LinkedList<Location> flags = new LinkedList<Location>();
	private HashMap<String, String> plot = new HashMap<String, String>();
	private Location throne;
	private int treasury, capitalprice, regionprice;
	public transient Map<String, AreaMarker> dynareas = new HashMap<String, AreaMarker>();
	
	public SNation()
	{
		this.name = "";
		this.tagname = "";
		
		multipliers.put("Gravity", 1.0);
		multipliers.put("Jumping", 1.0);
		multipliers.put("AirSpeed", 1.0);
		multipliers.put("Swimming", 1.0);
		multipliers.put("Walking", 1.0);
		
		this.spawn = null;
		this.throne = null;
		this.world = "";
		
		this.color = ChatColor.WHITE;
		this.ability = "";
		
		this.treasury = 0;
		this.capitalprice = 20;
		this.regionprice = 10;
	}
	
	public void kickPlayer(SPlayer sp)
	{
		sp.setNation(null);
		for(Iterator<Map.Entry<String, String>> iter = this.plot.entrySet().iterator(); iter.hasNext();)
		{
			Map.Entry<String, String> entry = iter.next();
			if(sp.getName().equals(entry.getValue()))
				iter.remove();
		}
		
		sp.sendMessage("Du wurdest aus der Nation entlassen.");
		ScorpiaCraft.broadcast(sp.getName() + " hat die Nation " + this.getColor() + this.getName() + ChatColor.WHITE + " verlassen");
	}
	
	public int getBalance()
	{
		return this.treasury;
	}
	
	public SNation setBalance(int amount)
	{
		this.treasury = amount;
		return this;
	}
	
	public void addCoins(int amount)
	{
		this.treasury += amount;
		Nations.payedIn += amount;
	}
	
	public boolean removeCoins(int amount)
	{
		if(this.treasury >= amount)
		{
			this.treasury-=amount;
			Nations.payedIn += amount;
			return true;
		}
		else
			return false;
	}
	
	public boolean isNear(Location loc, int distance)
	{
		try
		{
			if(this.throne.distance(loc) <= distance)
				return true;
			for(Location l : this.flags)
			{
				if(l.distance(loc) <= distance)
					return true;
			}
			return false;
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	public List<String> getRegions(boolean capital)
	{
		if(capital)
			return this.capital;
		else
			return this.region;
	}
	
	public List<String> getAllRegions()
	{
		List<String> list = new LinkedList<String>(this.region);
		list.addAll(this.capital);
		return list;
	}
	
	public void setThrone(Location loc)
	{
		this.throne = loc;
	}
	
	public String getPlotOwner(int x, int z)
	{
		return this.plot.get(x + "," + z);
	}
	
	public String getPlotOwner(Chunk c)
	{
		return this.getPlotOwner(c.getX(), c.getZ());
	}
	
	public void renewPlot(int x, int z)
	{
		this.setPlotOwner(x, z, "Free");
	}
	
	public void setPlotOwner(int x, int z, String name)
	{
		this.plot.put(x + "," + z, name);
	}
	
	public void deletePlot(int x, int z)
	{
		this.plot.remove(x + "," + z);
	}
	
	public int getPrice(String type)
	{
		if(type.equalsIgnoreCase("capital"))
			return this.capitalprice;
		else if(type.equalsIgnoreCase("region"))
			return this.regionprice;
		
		return 0;
	}
	
	public void setPrice(String type, int price)
	{
		if(type.equalsIgnoreCase("capital"))
			this.capitalprice = price;
		else if(type.equalsIgnoreCase("region"))
			this.regionprice = price;
	}
	
	public Location getThrone()
	{
		return this.throne;
	}
	
	public void removeThrone()
	{
		this.throne = null;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setTag(String tag)
	{
		this.tagname = tag;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getAbility()
	{
		return this.ability;
	}
	
	public List<Location> getFlags()
	{
		return this.flags;
	}
	
	public Location getSpawn()
	{
		if(this.spawn == null)
			return this.throne;
		return this.spawn;
	}
	
	public void setSpawn(int x, int z)
	{
		this.setSpawn(new Location(Bukkit.getServer().getWorld(this.world), x, Bukkit.getServer().getWorld(this.world).getHighestBlockYAt(x, z), z));
	}
	
	public void setSpawn(Location spawn)
	{
		this.spawn = new Location(spawn.getWorld(), spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), 0);
	}
	
	public boolean containsChunk(Chunk c)
	{
		return (this.region.contains(c.getX() + "," + c.getZ()) || this.capital.contains(c.getX() + "," + c.getZ()));
	}
	
	public boolean isCapital(Chunk c)
	{
		return this.capital.contains(c.getX() + "," + c.getZ());
	}
	
	public void addChunk(Chunk c, boolean capital)
	{
		if(capital)
			this.capital.add(c.getX() + "," + c.getZ());
		else
			this.region.add(c.getX() + "," + c.getZ());
	}
	
	public void clearChunks()
	{
		this.region.clear();
	}
	
	public void clearCapitalChunks()
	{
		this.capital.clear();
	}
	
	public String getTag()
	{
		return this.tagname;
	}
	
	public void addMultiplier(String type, double value)
	{
		multipliers.put(type, value);
	}
	
	public void setAbility(String ability)
	{
		this.ability = ability;
	}
	
	public void setColor(ChatColor col)
	{
		this.color = col;
	}
	
	public String getStringColor()
	{
		return Helper.ChatColorToString(this.color);
	}
		
	public ChatColor getColor()
	{
		return this.color;
	}
	
	public void applyMultiplier(SpoutPlayer player)
	{
		if(multipliers.containsKey("Gravity"))
			player.setGravityMultiplier(multipliers.get("Gravity"));
		
		if(multipliers.containsKey("Jumping"))
			player.setJumpingMultiplier(multipliers.get("Jumping"));
		
		if(multipliers.containsKey("AirSpeed"))
			player.setAirSpeedMultiplier(multipliers.get("AirSpeed"));
		
		if(multipliers.containsKey("Swimming"))
			player.setSwimmingMultiplier(multipliers.get("Swimming"));
		
		if(multipliers.containsKey("Walking"))
			player.setWalkingMultiplier(multipliers.get("Walking"));
	}
}
