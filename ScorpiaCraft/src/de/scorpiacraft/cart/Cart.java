package de.scorpiacraft.cart;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;

public class Cart
{
	public Map<UUID, Station> stations = new HashMap<UUID, Station>();
	public Map<String, Line> lines = new HashMap<String, Line>();
	public Map<UUID, String> cartMap = new HashMap<UUID, String>();
	public Map<UUID, BlockFace> fromCart = new HashMap<UUID, BlockFace>();
	private String selectedLine = "";
	
	public Cart()
	{
	}
	
	public boolean doCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) throws IOException
	{
		try
		{	
			Player p = (Player)sender;
			World w = p.getLocation().getWorld();
			Block pBlock = w.getBlockAt(p.getLocation());
			
			if(args[0].equalsIgnoreCase("create") && sender instanceof Player)
			{
				if(args[1].equalsIgnoreCase("station"))
				{
					if(args[2].isEmpty())
					{
						SendMessage(p, "No name for station specified.");
						return false;
					}
					
					if(pBlock.getType() == Material.RAILS)
					{
						Station temp = new Station(pBlock.getX(), pBlock.getY(), pBlock.getZ(), p.getLocation().getYaw(), args[2], w);
						stations.put(temp.getUUID(), temp);
						SendMessage(p, "Station " + temp.getName() + " created.");
					}
					else
						SendMessage(p, "No rails at your position.");
				}
				else if(args[1].equalsIgnoreCase("line"))
				{
					Station start = null;
					Station end = null;
					
					for(Iterator<Station> i = stations.values().iterator(); i.hasNext();)
					{
						Station pStation = i.next();
						if(pStation.getName().equalsIgnoreCase(args[2]))
							start = pStation;
						else if(pStation.getName().equalsIgnoreCase(args[3]))
							end = pStation;
					}
					
					if(start == null)
					{
						SendMessage(p, "Station " + args[2] + " does not exist.");
						return true;
					}
					
					if(end == null)
					{
						SendMessage(p, "Station " + args[3] + " does not exist.");
						return true;
					}
					
					lineCreate(start, end);
					selectedLine = this.lineName(start, end);
					SendMessage(p, "Line " + selectedLine + " created.");
				}
				else if(args[1].equalsIgnoreCase("node"))
				{
					this.nodeCreate(p.getLocation(), selectedLine);
					SendMessage(p, "Node created.");
				}
				
				return true;
			}
			
			else if(args[0].equalsIgnoreCase("save"))
			{
				this.Save();
				
				if(sender instanceof Player)
					SendMessage(p, "Cart System saved.");
				else
					ScorpiaCraft.log("Cart System saved.");
				
				return true;
			}
			
			else if(args[0].equalsIgnoreCase("delete") && sender instanceof Player)
			{
				if(args[1].equalsIgnoreCase("line"))
				{
					if(args[2].isEmpty())
					{
						SendMessage(p, "No line name specified.");
						return false;
					}
					
					if(!lines.containsKey(args[2]))
					{
						SendMessage(p, "Line " + args[2] + " does not exist.");
						return true;
					}
						
					lines.remove(args[2]);
					SendMessage(p, "Line " + args[2] + " deleted.");
				}
				else if(args[1].equalsIgnoreCase("station"))
				{
					if(args[2].isEmpty())
					{
						SendMessage(p, "No station name specified.");
						return false;
					}
					UUID station = null;
					for(Iterator<Station> i = stations.values().iterator(); i.hasNext();)
					{
						Station pStation = i.next();
						if(pStation.getName().equalsIgnoreCase(args[2]))
						{
							station = pStation.getUUID();
							break;
						}
					}
					if(station == null)
					{
						SendMessage(p, "Station " + args[2] + " does not exist.");
						return true;
					}
					stations.remove(station);
					SendMessage(p, "Station " + args[2] + " deleted.");
				}
				
				return true;
			}
		}
		catch(Exception ex)
		{
		}
		return false;
	}
	
	/*
	 * 		lineStart(line) - 0 Erfolgreich; 1 Keine line angegeben; 2 existiert nicht
	 */
	public int lineStart(String line, Player p)
	{
		if(line.isEmpty())
		{
			return 1;
		}
		if(!lines.containsKey(line))
		{
			return 2;
		}
		
		Line pLine = lines.get(line);
		Minecart cart = pLine.getStart().getWorld().spawn(pLine.getStart().getLocation(), Minecart.class);
		cart.setPassenger(p);
		
		cart.setVelocity(pLine.getStart().getDirection());
		cartMap.put(cart.getUniqueId(), line);
		fromCart.put(cart.getUniqueId(), pLine.getStart().getFace());
		
		return 0;
	}
	
	public boolean lineCreate(Station start, Station end)
	{		
		String linesName = start.getName() + "-" + end.getName();
		if(lines.containsKey(linesName)) // Line existiert bereits
			return false;
			
		lines.put(linesName, new Line(start, end, linesName));
		
		return true;
	}
	
	public String lineName(Station start, Station end)
	{
		return start.getName() + "-" + end.getName();
	}
	
	public void lineDelete(String line)
	{
		lines.remove(line);
	}
	
	public void nodeCreate(Location loc, String line)
	{
		lines.get(line).addNode(loc);
	}
	
	public void Save()
	{
		try
		{
			//Helper.save(stations, plugin.getDataFolder().getPath() + "/stations.bin");
			//Helper.save(lines, plugin.getDataFolder().getPath() + "/lines.bin");
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void SendMessage(Player p, String message)
	{
		p.sendMessage("["+ScorpiaCraft.p.getDescription().getName()+"] " + message);
	}
}
