package de.scorpiacraft.features;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.entity.Player;

import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlot;
import de.scorpiacraft.util.Disk;

public class Plot
{
	private static Map<String, SPlot> plots = new HashMap<String, SPlot>();
	
	public static enum PlotType
	{
		Sale, Purchased, Server, NPC, None
	}
	
	public static void save()
	{
		Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/plots.json", ScorpiaCraft.gson.toJson(plots, new TypeToken<Map<String, SPlot>>() {}.getType()));
	}
	
	public static void load(boolean reload)
	{
		if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/plots.json"))
		{
			plots = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/plots.json"), new TypeToken<Map<String, SPlot>>() {}.getType());
		}
		else
		{
			ScorpiaCraft.log("CreatePlotsFile");
			ScorpiaCraft.save("plots");
		}
	}
	
	public static SPlot getPlot(String id)
	{
		return plots.get(id);
	}
	
	public static SPlot getPlot(Chunk chunk)
	{
		return getPlot(chunk.getX() + "," + chunk.getZ());
	}
	
	public static SPlot getPlot(Player player)
	{
		return getPlot(player.getLocation().getChunk());
	}
	
	public static void setPlot(String id, PlotType type, String holder)
	{
		SPlot plot = new SPlot();
		plot.setType(type);
		plot.setHolder(holder);
		plots.put(id, plot);
	}
	
	public static void setPlot(String id, PlotType type)
	{
		setPlot(id, type, "");
	}
	
	public static void setPlot(String id, String holder)
	{
		setPlot(id, PlotType.Purchased, holder);
	}
	
	public static void setPlot(Chunk chunk, PlotType type, String holder)
	{
		setPlot(chunk.getX() + "," + chunk.getZ(), type, holder);
	}
	
	public static void setPlot(Chunk chunk, PlotType type)
	{
		setPlot(chunk, type, "");
	}
	
	public static void setPlot(Chunk chunk, String holder)
	{
		setPlot(chunk, PlotType.Purchased, holder);
	}
	
	public static boolean checkPlot(Chunk chunk, String player, boolean interact)
	{
		return checkPlot(chunk, Bukkit.getPlayerExact(player), interact);
	}
	
	public static boolean isForSale(Chunk chunk)
	{
		SPlot plot = getPlot(chunk);
		if(plot != null && plot.getType() == PlotType.Sale)
			return true;
		else
			return false;
	}
	
	public static boolean isPlayersPlot(Chunk chunk, String player)
	{
		SPlot plot = getPlot(chunk);
		if(plot != null && plot.getType() == PlotType.Purchased && plot.getHolder().equals(player))
			return true;
		else
			return false;
	}
	
	public static boolean checkPlot(Chunk chunk, Player player, boolean interact)
	{
		SPlot plot = getPlot(chunk);
		if(plot == null)
			return !((interact && player.hasPermission("scorpiacraft.interact.none")) || (!interact && player.hasPermission("scorpiacraft.build.none")));
		
		if(plot.getType() == PlotType.Server)
			return !((interact && player.hasPermission("scorpiacraft.interact.server")) || (!interact && player.hasPermission("scorpiacraft.build.server")));
		
		if(plot.getType() == PlotType.NPC)
			return !((interact && player.hasPermission("scorpiacraft.interact.npc")) || (!interact && player.hasPermission("scorpiacraft.build.npc")));
		
		if(plot.getHolder().equals(player.getName()) || player.hasPermission("scorpiacraft.build.other"))
			return false;
		
		return true;
	}
	
	public static void removePlot(String id)
	{
		plots.remove(id);
	}
	
	public static void removePlot(Chunk chunk)
	{
		removePlot(chunk.getX() + "," + chunk.getZ());
	}
	
	public static void calculatePrice(Chunk chunk)
	{
		SPlot pl = getPlot(chunk);
		if(pl != null)
		{
			int price = 0;
			ChunkSnapshot snap = chunk.getChunkSnapshot(true, false, false);
			
			for(int x = 0; x < 16; x++)
			{
				for(int z = 0; z < 16; z++)
				{
					for(int y = 0; y < snap.getHighestBlockYAt(x, z); y++)
					{
						int id = snap.getBlockTypeId(x, y, z);
						if(id != 0)
						{
							price++;
							switch(id)
							{
								case 16: // Kohle
									price += 10;
									break;
									
								case 15: // Eisen
									price += 100;
									break;
									
								case 14: // Gold
									price += 1000;
									break;
									
								case 56: // Diamant
									price += 10000;
									break;
									
								case 129: // Emerald
									price += 100000;
									break;
									
								case 42: // Eisenblock
									price += 1000;
									break;
									
								case 41: // Goldblock
									price += 10000;
									break;
									
								case 57: // Diamantblock
									price += 100000;
									break;
									
								case 133: // Emeraldblock
									price += 1000000;
									break;
									
								case 21: // Lapis
									price += 500;
									break;
									
								case 22: // Lapisblock
									price += 5000;
									break;
									
								case 45: // Ziegel
									price += 9;
									break;
							}
						}
					}
				}
			}
			
			pl.setPrice(price / 10000);
		}
	}
}
