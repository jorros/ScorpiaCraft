package de.scorpiacraft.features;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.dynmap.markers.AreaMarker;

import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.ScorpiaCraft.Rank;
import de.scorpiacraft.custom.FlagBlock;
import de.scorpiacraft.custom.ThroneBlock;
import de.scorpiacraft.obj.SNation;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Disk;
import de.scorpiacraft.util.TileFlags;

public class Nations
{
	public static Map<String, SNation> Nation = new HashMap<String, SNation>(); // <tag, house>
	
	public static FlagBlock flagBlock;
	public static ThroneBlock throneBlock;
	
	public static SNation getNation(String tag)
	{
		if(Nation.containsKey(tag))
			return Nation.get(tag);
		else
			return null;
	}
	
	public static SNation getNation(Chunk c)
	{
		if(c != null)
		{
			for(SNation nation : Nations.Nation.values())
			{
				if(nation.containsChunk(c))
					return nation;
			}
		}
		
		return null;
	}
	
	public static boolean checkNation(Player player, Chunk chunk)
	{
		if(player == null)
			return false;
		
		SPlayer sp = ScorpiaCraft.getPlayer(player);
		
		if(sp.getRank().getLevel() > Rank.Moderator.getLevel())
		{
			// Wenn Region niemandem gehört
			SNation nat = Nations.getNation(chunk);
			
			if(nat == null)
				return false;
			else if(sp.getNation().equals(nat)) // Wenn Region zur Nation des Spielers gehört
			{
				if(sp.getRank().getLevel() <= Rank.CoLeader.getLevel()) // Hat Spieler hohes Ranking
					return false;
				if(Nations.getNation("Delphi").getPlotOwner(chunk) == null) // Ist Land nicht zum Verkauf
					return false;
				else if(!Nations.getNation("Delphi").getPlotOwner(chunk).equals(sp.getName())) // Gehört dem Spieler nicht das Land
					return true;
			}
			else // Wenn Region nicht zur Nation des Spielers gehört
				return true;
		}
		
		return false;
	}
	
	private static void setChunk(int x, int z, SNation n, boolean capital)
	{
		Chunk c = Bukkit.getWorld("world").getChunkAt(x, z);
		SNation a = getNation(c);
		if(a == null || (capital && a.equals(n)))
		{
			n.addChunk(c, capital);
		}
	}
	
	private static void line(int x_1, int z_1, int x_2, int z_2, SNation n, boolean capital)
	{
		int x1 = x_1;
		int z1 = z_1;
		
		int x2 = x_2;
		int z2 = z_2;
		
		if(x_1 > x_2)
		{
			x1 = x_2;
			z1 = z_2;
			
			x2 = x_1;
			z2 = z_1;
		}
		
		int dx = Math.abs(x2 - x1);
		int dz = Math.abs(z2 - z1);
		int inc_dec = ((z2 >= z1) ? 1:-1);
		
		if(dx > dz)
		{
			int two_dz = (2*dz);
			int two_dz_dx = (2*(dz-dx));
			int p = ((2*dz)-dx);
			
			int x = x1;
			int z = z1;
			
			setChunk(x, z, n, capital);
			
			while(x < x2)
			{
				x++;
				
				if(p<0)
					p+=two_dz;
				else
				{
					z += inc_dec;
					p += two_dz_dx;
				}
				setChunk(x, z, n, capital);
			}
		}
		else
		{
			int two_dx = (2*dx);
			int two_dx_dz = (2*(dx-dz));
			int p = ((2*dx) - dz);
			
			int x = x1;
			int z = z1;
			
			setChunk(x, z, n, capital);
			
			while(z!=z2)
			{
				z += inc_dec;
				if(p<0)
					p += two_dx;
				else
				{
					x++;
					p += two_dx_dz;
				}
				setChunk(x, z, n, capital);
			}
		}
	}
	
	private static void setCircleChunk(Chunk mid, int radius, SNation n, boolean capital)
	{	
		int x1, x2;
		for(int z = mid.getZ(); z <= mid.getZ() + radius; ++z)
		{
			x1 = (int)Math.round(mid.getX() + Math.sqrt(Math.pow(radius, 2) - Math.pow(z - mid.getZ(), 2)));
			x2 = (int)Math.round(mid.getX() - Math.sqrt(Math.pow(radius, 2) - Math.pow(z - mid.getZ(), 2)));
			
			line(x1, z, x2, z, n, capital);
		}
		
		for(int z = mid.getZ(); z >= mid.getZ() - radius; --z)
		{
			x1 = (int)Math.round(mid.getX() + Math.sqrt(Math.pow(radius, 2) - Math.pow(z - mid.getZ(), 2)));
			x2 = (int)Math.round(mid.getX() - Math.sqrt(Math.pow(radius, 2) - Math.pow(z - mid.getZ(), 2)));
			
			line(x1, z, x2, z, n, capital);
		}
	}
	
	private static class NationBlock
	{
		int x, z;
    }
	
	public static void handleDynmap(SNation nation)
	{
		if(nation == null)
			return;
		
		if(Dynmap.markerAPI == null)
			return;
		
		Map<String,AreaMarker> newmap = new HashMap<String,AreaMarker>(); /* Build new map */
		
		LinkedList<NationBlock> blocks = new LinkedList<NationBlock>();
		
		for(String coord : nation.getAllRegions())
		{
			String[] split = coord.split(",");
			if(split.length == 2)
			{
                try
                {
                    NationBlock fb = new NationBlock();
                    fb.x = Integer.valueOf(split[0]);
                    fb.z = Integer.valueOf(split[1]);
                    blocks.add(fb);
                } 
                catch (NumberFormatException nfx)
                {
                }
            }
		}
		
		handleNation(nation, blocks, newmap);
		blocks.clear();	
		
		for(AreaMarker oldm : nation.dynareas.values())
		{
            oldm.deleteMarker();
        }
		nation.dynareas = newmap;
	}
	
	public static void calculate(SNation nation)
	{
		if(nation == null)
			return;
		
		nation.clearChunks();
		
		for(Location l : nation.getFlags())
		{
			Chunk c = l.getChunk();
			setCircleChunk(c, 10, nation, false);
		}
		handleDynmap(nation);
	}
	
	public static void calculateThrone(SNation nation)
	{
		if(nation == null)
			return;
		
		nation.clearCapitalChunks();
		
		if(nation.getThrone() == null)
			return;
		
		Chunk c = nation.getThrone().getChunk();
		setCircleChunk(c, 20, nation, true);
		handleDynmap(nation);
	}
	
	public static void load(boolean reload)
	{
		if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/nations.json"))
		{
			Nation = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/nations.json"), new TypeToken<Map<String, SNation>>() {}.getType());
		}
		else
		{
			ScorpiaCraft.log("CreateNationsFile");
			SNation delphi = new SNation();
			delphi.setColor(ChatColor.DARK_PURPLE);
			delphi.setName("Delphi");
			delphi.setTag("Delphi");
			Nation.put("Delphi", delphi);
			ScorpiaCraft.save("nations");
		}
	}
	
	public static void save()
	{
		Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/nations.json", ScorpiaCraft.gson.toJson(Nation, new TypeToken<Map<String, SNation>>() {}.getType()));
	}
	
	private static void handleNation(SNation nation, LinkedList<NationBlock> blocks, Map<String, AreaMarker> newmap)
	{
		double[] x = null;
		double[] z = null;
		int poly_index = 0; /* Index of polygon for given faction */
		
		ArrayList<SPlayer> lead = ScorpiaCraft.getPlayers(nation, Rank.Leader);
		
		String king = "";
		try
		{
			king = lead.get(0).getName();
		}
		catch(Exception ex)
		{
		}
		lead = ScorpiaCraft.getPlayers(nation, Rank.CoLeader);
		String coking = "";
		try
		{
			coking = lead.get(0).getName();
		}
		catch(Exception ex)
		{
		}
		
		/* Build popup */
		String desc = "<div class=\"infowindow\"><span style=\"font-size:120%; color:#"
				+ nation.getStringColor()
				+ "\">"
				+ nation.getName()
				+ "</span><br /> K&ouml;nig <span style=\"font-weight:bold;\">"+ king +"</span>"
				+ "<br /> Rechte Hand <span style=\"font-weight:bold;\">"+ coking +"</span></div>";
		
		if(blocks.isEmpty())
			return;
		
		LinkedList<NationBlock> nodevals = new LinkedList<NationBlock>();
		TileFlags curblks = new TileFlags();
		/* Loop through blocks: set flags on blockmaps */
		for(NationBlock b : blocks)
		{
			curblks.setFlag(b.x, b.z, true); /* Set flag for block */
			nodevals.addLast(b);
		}
		
		/* Loop through until we don't find more areas */
		while(nodevals != null)
		{
			LinkedList<NationBlock> ournodes = null;
			LinkedList<NationBlock> newlist = null;
			TileFlags ourblks = null;
			int minx = Integer.MAX_VALUE;
			int minz = Integer.MAX_VALUE;
			for(NationBlock node : nodevals)
			{
				int nodex = node.x;
				int nodez = node.z;
				/*
				 * If we need to start shape, and this block is not part of one
				 * yet
				 */
				if((ourblks == null) && curblks.getFlag(nodex, nodez))
				{
					ourblks = new TileFlags(); /* Create map for shape */
					ournodes = new LinkedList<NationBlock>();
					floodFillTarget(curblks, ourblks, nodex, nodez); /*
																	 * Copy
																	 * shape
																	 */
					ournodes.add(node); /* Add it to our node list */
					minx = nodex;
					minz = nodez;
				}
				/* If shape found, and we're in it, add to our node list */
				else if((ourblks != null) && ourblks.getFlag(nodex, nodez))
				{
					ournodes.add(node);
					if(nodex < minx)
					{
						minx = nodex;
						minz = nodez;
					}
					else if((nodex == minx) && (nodez < minz))
					{
						minz = nodez;
					}
				}
				else
				{ /* Else, keep it in the list for the next polygon */
					if(newlist == null)
						newlist = new LinkedList<NationBlock>();
					newlist.add(node);
				}
			}
			nodevals = newlist; /* Replace list (null if no more to process) */
			if(ourblks != null)
			{
				/* Trace outline of blocks - start from minx, minz going to x+ */
				int init_x = minx;
				int init_z = minz;
				int cur_x = minx;
				int cur_z = minz;
				direction dir = direction.XPLUS;
				ArrayList<int[]> linelist = new ArrayList<int[]>();
				linelist.add(new int[]
				{ init_x, init_z }); // Add start point
				while((cur_x != init_x) || (cur_z != init_z)
						|| (dir != direction.ZMINUS))
				{
					switch(dir)
					{
						case XPLUS: /* Segment in X+ direction */
							if(!ourblks.getFlag(cur_x + 1, cur_z))
							{ /* Right turn? */
								linelist.add(new int[]
								{ cur_x + 1, cur_z }); /* Finish line */
								dir = direction.ZPLUS; /* Change direction */
							}
							else if(!ourblks.getFlag(cur_x + 1, cur_z - 1))
							{ /* Straight? */
								cur_x++;
							}
							else
							{ /* Left turn */
								linelist.add(new int[]
								{ cur_x + 1, cur_z }); /* Finish line */
								dir = direction.ZMINUS;
								cur_x++;
								cur_z--;
							}
							break;
						case ZPLUS: /* Segment in Z+ direction */
							if(!ourblks.getFlag(cur_x, cur_z + 1))
							{ /* Right turn? */
								linelist.add(new int[]
								{ cur_x + 1, cur_z + 1 }); /* Finish line */
								dir = direction.XMINUS; /* Change direction */
							}
							else if(!ourblks.getFlag(cur_x + 1, cur_z + 1))
							{ /* Straight? */
								cur_z++;
							}
							else
							{ /* Left turn */
								linelist.add(new int[]
								{ cur_x + 1, cur_z + 1 }); /* Finish line */
								dir = direction.XPLUS;
								cur_x++;
								cur_z++;
							}
							break;
						case XMINUS: /* Segment in X- direction */
							if(!ourblks.getFlag(cur_x - 1, cur_z))
							{ /* Right turn? */
								linelist.add(new int[]
								{ cur_x, cur_z + 1 }); /* Finish line */
								dir = direction.ZMINUS; /* Change direction */
							}
							else if(!ourblks.getFlag(cur_x - 1, cur_z + 1))
							{ /* Straight? */
								cur_x--;
							}
							else
							{ /* Left turn */
								linelist.add(new int[]
								{ cur_x, cur_z + 1 }); /* Finish line */
								dir = direction.ZPLUS;
								cur_x--;
								cur_z++;
							}
							break;
						case ZMINUS: /* Segment in Z- direction */
							if(!ourblks.getFlag(cur_x, cur_z - 1))
							{ /* Right turn? */
								linelist.add(new int[]
								{ cur_x, cur_z }); /* Finish line */
								dir = direction.XPLUS; /* Change direction */
							}
							else if(!ourblks.getFlag(cur_x - 1, cur_z - 1))
							{ /* Straight? */
								cur_z--;
							}
							else
							{ /* Left turn */
								linelist.add(new int[]
								{ cur_x, cur_z }); /* Finish line */
								dir = direction.XMINUS;
								cur_x--;
								cur_z--;
							}
							break;
					}
				}
				String world;
				if(nation.getThrone() == null)
					world = Bukkit.getWorld("world").getName();
				else
					world = nation.getThrone().getWorld().getName();
				
				/* Build information for specific area */
				String polyid = nation.getTag() + "__"
						+ world + "__"
						+ poly_index;
				int sz = linelist.size();
				x = new double[sz];
				z = new double[sz];
				for(int i = 0; i < sz; i++)
				{
					int[] line = linelist.get(i);
					x[i] = (double)line[0] * (double)16;
					z[i] = (double)line[1] * (double)16;
				}
				/* Find existing one */
				AreaMarker m = nation.dynareas.remove(polyid); /* Existing area? */
				if(m == null)
				{
					m = Dynmap.markerSet.createAreaMarker(polyid, nation.getTag(),
							false, world, x,
							z, false);
					if(m == null)
					{
						ScorpiaCraft.log("ErrorAddingMarker " + polyid);
						return;
					}
				}
				else
				{
					m.setCornerLocations(x, z); /* Replace corner locations */
					m.setLabel(nation.getTag()); /* Update label */
				}
				m.setDescription(desc); /* Set popup */
				
				/* Set line and fill properties */
				/*if(capital)
				{
					m.setLineStyle(2, 0.9,
							Integer.parseInt(nation.getStringColor(), 16));
					m.setFillStyle(0.6,
							Integer.parseInt(nation.getStringColor(), 16));
				}*/
				m.setLineStyle(2, 0.9, Integer.parseInt(nation.getStringColor(), 16));
				m.setFillStyle(0.4, Integer.parseInt(nation.getStringColor(), 16));
				
				/* Add to map */
				newmap.put(polyid, m);
				poly_index++;
			}
		}
	}
	
	enum direction
	{
		XPLUS, ZPLUS, XMINUS, ZMINUS
	};
	
	private static int floodFillTarget(TileFlags src, TileFlags dest, int x,
			int y)
	{
		int cnt = 0;
		ArrayDeque<int[]> stack = new ArrayDeque<int[]>();
		stack.push(new int[]
		{ x, y });
		
		while(stack.isEmpty() == false)
		{
			int[] nxt = stack.pop();
			x = nxt[0];
			y = nxt[1];
			if(src.getFlag(x, y))
			{ /* Set in src */
				src.setFlag(x, y, false); /* Clear source */
				dest.setFlag(x, y, true); /* Set in destination */
				cnt++;
				if(src.getFlag(x + 1, y))
					stack.push(new int[]
					{ x + 1, y });
				if(src.getFlag(x - 1, y))
					stack.push(new int[]
					{ x - 1, y });
				if(src.getFlag(x, y + 1))
					stack.push(new int[]
					{ x, y + 1 });
				if(src.getFlag(x, y - 1))
					stack.push(new int[]
					{ x, y - 1 });
			}
		}
		return cnt;
	}
}
