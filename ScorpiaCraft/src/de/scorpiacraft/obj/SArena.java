package de.scorpiacraft.obj;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Spleef;

public class SArena
{
	private String owner, name;
	private transient boolean isPlaying;
	private transient LinkedList<String> team1, team2;
	private transient int gamemode = 0;
	private Location level1start, level1end, level2start, level2end, spectator;
	
	public SArena setOwner(String owner)
	{
		this.owner = owner;
		return this;
	}
	
	public String getOwner()
	{
		return this.owner;
	}
	
	public List<String> getTeam(int team)
	{
		if(team == 1)
			return this.team1;
		else
			return this.team2;
	}
	
	public SArena setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setSpectator(Location loc)
	{
		this.spectator = loc;
	}
	
	public Location getSpectator()
	{
		return this.spectator;
	}
	
	public void setStart(int level, Location loc)
	{
		if(level == 1)
			this.level1start = loc.getBlock().getLocation();
		else if(level == 2)
			this.level2start = loc.getBlock().getLocation();
	}
	
	public void setEnd(int level, Location loc)
	{
		if(level == 1)
			this.level1end = loc.getBlock().getLocation();
		else if(level == 2)
			this.level2end = loc.getBlock().getLocation();
	}
	
	public int getGameMode()
	{
		return this.gamemode;
	}
	
	public void setGameMode(int mode)
	{
		this.gamemode = mode;
	}
	
	public boolean startGame()
	{
		this.renewArea();
		
		int total = 0;
		if(this.team1 != null)
			total += this.team1.size();
		if(this.team2 != null)
			total += this.team2.size();
		if(total < 2)
			return false;
		
		if(this.team1 != null)
		{
			for(String p : this.team1)
			{
				Location loc = this.level1start.clone().add(0, 2, 0);
				loc.setX(Math.min(this.level1start.getX(), this.level1end.getX())+((Math.max(this.level1start.getX(), this.level1end.getX()) - Math.min(this.level1start.getX(), this.level1end.getX())) / 2));
				loc.setZ(Math.min(this.level1start.getZ(), this.level1end.getZ())+((Math.max(this.level1start.getZ(), this.level1end.getZ()) - Math.min(this.level1start.getZ(), this.level1end.getZ())) / 2));
				Bukkit.getPlayer(p).teleport(loc);
				ScorpiaCraft.getPlayer(p).saveInventory(Bukkit.getPlayer(p)).saveGameMode(Bukkit.getPlayer(p));
				Bukkit.getPlayer(p).setGameMode(GameMode.SURVIVAL);
				Bukkit.getPlayer(p).setItemInHand(new ItemStack(Material.DIAMOND_SPADE, 1));
			}
		}
		if(this.team2 != null)
		{
			for(String p : this.team2)
			{
				Location loc = this.level1start.clone().add(0, 2, 0);
				loc.setX(Math.min(this.level1start.getX(), this.level1end.getX())+((Math.max(this.level1start.getX(), this.level1end.getX()) - Math.min(this.level1start.getX(), this.level1end.getX())) / 2));
				loc.setZ(Math.min(this.level1start.getZ(), this.level1end.getZ())+((Math.max(this.level1start.getZ(), this.level1end.getZ()) - Math.min(this.level1start.getZ(), this.level1end.getZ())) / 2));
				Bukkit.getPlayer(p).teleport(loc);
				ScorpiaCraft.getPlayer(p).saveInventory().saveGameMode(Bukkit.getPlayer(p));
				Bukkit.getPlayer(p).setGameMode(GameMode.SURVIVAL);
				Bukkit.getPlayer(p).setItemInHand(new ItemStack(Material.DIAMOND_SPADE, 1));
			}
		}
		
		ScorpiaCraft.broadcast("Das Spiel startet in 5 Sekunden");
		Spleef.start.add(this);
		ScorpiaCraft.p.getServer().getScheduler().scheduleSyncDelayedTask(ScorpiaCraft.p, new Runnable()
		{
			public void run()
			{
				Spleef.start.getFirst().isPlaying = true;
				Spleef.start.remove();
				ScorpiaCraft.broadcast("Das Spiel hat begonnen.");
			}
		}, 100L);
		
		return true;
	}
	
	public boolean isPlaying()
	{
		return this.isPlaying;
	}
	
	public void addPlayer(String name)
	{
		if(this.team1 == null)
			this.team1 = new LinkedList<String>();
		this.team1.add(name);
	}
	
	public void movePlayer(String name, int group)
	{
		if(group > 0 && group <= 2)
		{
			removePlayer(name);
			if(group == 1)
				this.team1.add(name);
			if(group == 2)
				this.team2.add(name);
		}
	}
	
	public boolean checkWinner()
	{
		if(this.gamemode == 0)
		{
			if(team1.size() < 2)
				return true;
			else
				return false;
		}
		else
		{
			if(team1.size() < 2 && team1.size() > 0)
				return true;
			else if(team2.size() < 2 && team2.size() > 0)
				return true;
			else
				return false;
		}
	}
	
	public String getWinner()
	{
		if(this.gamemode == 0)
		{
			if(team1.size() < 2)
				return team1.getLast();
			else
				return null;
		}
		else
		{
			if(team1.size() < 2 && team1.size() > 0)
				return team1.getLast();
			else if(team2.size() < 2 && team2.size() > 0)
				return team2.getLast();
			else
				return null;
		}
	}
	
	public void removePlayer(String name)
	{
		if(this.team1 != null && this.team1.contains(name))
			this.team1.remove(name);
		else if(this.team2 != null && this.team2.contains(name))
			this.team2.remove(name);
	}
	
	public void endGame()
	{
		if(this.team1 != null)
		{
			for(String p : this.team1)
			{
				SPlayer sp = ScorpiaCraft.getPlayer(p);
				Bukkit.getPlayer(p).teleport(this.spectator);
				sp.setSpleefArena(null);
				sp.loadGameMode();
				sp.loadInventory();
			}
		}
		if(this.team2 != null)
		{
			for(String p : this.team2)
			{
				SPlayer sp = ScorpiaCraft.getPlayer(p);
				Bukkit.getPlayer(p).teleport(this.spectator);
				sp.setSpleefArena(null);
				sp.loadGameMode();
				sp.loadInventory();
			}
		}
		this.team1 = new LinkedList<String>();
		this.team2 = new LinkedList<String>();
		this.isPlaying = false;
		this.renewArea();
	}
	
	public void renewArea()
	{
		if(this.level1end != null && this.level1start != null)
		{
			for(int z = Math.min(level1start.getBlockZ(), level1end.getBlockZ()); z <= Math.max(level1start.getBlockZ(), level1end.getBlockZ()); z++)
			{
				for(int x = Math.min(level1start.getBlockX(), level1end.getBlockX()); x <= Math.max(level1start.getBlockX(), level1end.getBlockX()); x++)
				{
					level1start.getWorld().getBlockAt(x, level1start.getBlockY(), z).setType(Material.GRASS);
				}
			}
		}
		
		if(this.level2end != null && this.level2start != null)
		{
			for(int z = (int)Math.min(level2start.getZ(), level2end.getZ()); z <= Math.max(level2start.getZ(), level1end.getZ()); z++)
			{
				for(int x = (int)Math.min(level2start.getX(), level2end.getX()); x <= Math.max(level2start.getX(), level2end.getX()); x++)
				{
					level2start.getWorld().getBlockAt(x, (int)level2start.getY(), z).setType(Material.GRASS);
				}
			}
		}
	}
}
