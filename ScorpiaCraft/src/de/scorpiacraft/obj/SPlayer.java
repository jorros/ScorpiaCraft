package de.scorpiacraft.obj;

import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SClass;
import de.scorpiacraft.util.Helper;
import de.scorpiacraft.features.Classes;
import de.scorpiacraft.features.Economy;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.features.Spleef;

public class SPlayer
{
	private String name, selectedShop, selectedArena, sclass;
	private boolean vip;
	private Location lastLocation, home;
	private int lastonline, balance;
	private long expiration;
	public boolean instinct = true;
	
	private transient String lastPlot = "", spleefarena = null;
	public transient int scheduler = -1, tick = 0;
	private transient LinkedList<InventoryEntry> inventory;
	private transient boolean afk = false;
	private transient int detectTicks = 0;
	private transient GameMode gamemode;
	
	public SPlayer(String name)
	{
		this.name = name;
		this.vip = false;
		this.lastLocation = null;
		this.expiration = 0;
		this.lastonline = 0;
		this.lastPlot = "";
		this.instinct = true;
		
		ScorpiaCraft.Players.put(this.name, this);
	}
	
	public SPlayer(Player p)
	{
		this(p.getName());
	}
	
	public void addEmerald(int amount)
	{
		this.balance += amount;
		Economy.payedIn += amount;
	}
	
	public void addVIPMonth()
	{
		if(!this.vip)
			this.expiration = (System.currentTimeMillis()/1000) + 2629743;
		else
			this.expiration += 2629743;
		
		this.vip = true;
	}
	
	public int getBalance()
	{
		return this.balance;
	}
	
	public boolean isInstinctEnabled()
	{
		return this.instinct;
	}
	
	public void toggleInstinct()
	{
		this.instinct = !this.instinct;
	}
	
	public Date getExpiration()
	{
		if(!this.vip)
			return null;
		else
			return new Date(this.expiration * 1000);
	}
	
	public Location getHome()
	{
		if(this.home == null)
			return getPlayer().getLocation().getWorld().getSpawnLocation();
		else
			return this.home;
	}
	
	public Location getLastLocation()
	{
		return lastLocation;
	}
	
	public int getLastOnline()
	{
		return this.lastonline;
	}
	
	public Location getLocation()
	{
		return lastLocation;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public SClass getSClass()
	{
		if(this.sclass != null)
			return Classes.getSClass(this.sclass);
		else
			return Classes.getSClass("None");
	}
	
	public int getExperience(String type)
	{
		return getSClass().getExperience(type, getPlayer());
	}
	
	public boolean hasAbility(String ability)
	{
		return getSClass().hasAbility(ability, getPlayer());
	}
	
	public SPlayer setSClass(SClass sc)
	{
		this.sclass = sc.getName();
		return this;
	}
	
	public SArena getSpleefArena()
	{
		return Spleef.getArena(this.spleefarena);
	}
	
	public SPlayer setSpleefArena(SArena arena)
	{
		if(arena != null)
			this.spleefarena = arena.getName();
		else
			this.spleefarena = null;
		return this;
	}
	
	public OfflinePlayer getOfflinePlayer()
	{
		return Bukkit.getOfflinePlayer(this.name);
	}
	
	public Player getPlayer()
	{
		Player p = Bukkit.getPlayerExact(this.name);
		if(p != null)
			return p;
		else
			return null;
	}
	
	public Location getSelectedShop()
	{
		if(this.selectedShop != null)
		{
			String[] pos = this.selectedShop.split(",");
			Location loc = new Location(Bukkit.getWorld("world"), Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
			return loc;
		}
		else
			return null;
	}
	
	public Location getSelectedArena()
	{
		if(this.selectedArena != null)
		{
			String[] pos = this.selectedArena.split(",");
			Location loc = new Location(Bukkit.getWorld("world"), Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
			return loc;
		}
		else
			return null;
	}
	
	public boolean isAFK()
	{
		return this.afk;
	}
	
	public boolean isOnline()
	{
		if(Bukkit.getPlayer(name) == null)
			return false;
		else
			return true;
	}
	
	public boolean isVIP()
	{
		return this.vip;
	}
	
	public void loadInventory()
	{
		this.loadInventory(getPlayer());
	}
	
	public void loadInventory(Player player)
	{
		if(this.inventory == null)
			return;
		
		Inventory inventory = player.getInventory();
		inventory.clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		
		for (InventoryEntry inventoryEntry : this.inventory)
		{
			if(inventoryEntry.index >= 0)
				inventory.setItem(inventoryEntry.index, inventoryEntry.item.clone());
			else
			{
				switch(inventoryEntry.index)
				{
				case HELMET:
					player.getInventory().setHelmet(inventoryEntry.item.clone());
					break;
				case CHESTPLATE:
					player.getInventory().setChestplate(inventoryEntry.item.clone());
					break;
				case LEGGINGS:
					player.getInventory().setLeggings(inventoryEntry.item.clone());
					break;
				case BOOTS:
					player.getInventory().setBoots(inventoryEntry.item.clone());
					break;
				}
			}
		}
	}
	
	public SPlayer saveGameMode(Player p)
	{
		this.gamemode = p.getGameMode();
		return this;
	}
	
	public void loadGameMode()
	{
		loadGameMode(getPlayer());
	}
	
	public void loadGameMode(Player p)
	{
		p.setGameMode(this.gamemode);
	}
	
	public void logOff()
	{
		this.lastonline = (int)System.currentTimeMillis()/1000;
	}
	
	public void onLogin()
	{
	}
	
	public void onLogout()
	{
		if(scheduler != -1)
		{
			ScorpiaCraft.p.getServer().getScheduler().cancelTask(scheduler);
			scheduler = -1;
		}
		
		if(this.afk)
			this.afk = false;
		
		if(this.spleefarena != null)
		{
			getSpleefArena().removePlayer(this.name);
			getPlayer().teleport(getSpleefArena().getSpectator());
			this.spleefarena = null;
		}
	}
	
	public void onMove(Location loc)
	{
		this.setAFK(false);
		
		// VIP Check
		if(this.vip)
		{
			long timestamp = System.currentTimeMillis()/1000;
			if(timestamp > this.expiration)
				this.vip = false;
		}
		// Label
		String plotname = "";
			
		SPlot plot = Plot.getPlot(loc.getChunk());
		if(plot == null)
			plotname = "Frei";
		else
		{
			switch(plot.getType())
			{
				case Server:
					plotname = "Server";
					break;
					
				case Sale:
					plotname = "Zum Verkauf für " + Plot.getPlot(loc.getChunk()).getPrice() + "e";
					break;
						
				case Purchased:
					plotname = plot.getHolder() + "'s";
					break;
						
				case NPC:
					plotname = "NPC";
					break;
			}
		}
		
		if(this.lastPlot == null || !plotname.equals(this.lastPlot))
		{
			this.lastPlot = plotname;
			sendMessage("Grundstück: " + plotname);
		}
		
		this.detectTicks++;
		if(this.detectTicks >= 30)
		{
			this.detectTicks = 0;
			
			HashMap<String, Integer> detectedMobs = new HashMap<String, Integer>();
			List<Entity> entities = getPlayer().getNearbyEntities(80, 10, 80);
			
			if(hasAbility("heal"))
			{
				if(getPlayer().getHealth() < getPlayer().getMaxHealth())
				{
					getPlayer().setHealth(getPlayer().getHealth() + 1);
				}
			}
			
			if(this.instinct)
			{
				if(hasAbility("detectCreeper"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof Creeper)
							i++;
					}
					if(i > 0)
						detectedMobs.put("Creeper", i);
				}
				if(hasAbility("detectMagmaCube"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof MagmaCube)
							i++;
					}
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Lavaslimes", i);
						else
							detectedMobs.put("Lavaslime", i);
					}
				}
				if(hasAbility("detectSilverfish"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof Silverfish)
							i++;
					}
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Silberfische", i);
						else
							detectedMobs.put("Silberfisch", i);
					}
				}
				if(hasAbility("detectSkeleton"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof Skeleton)
							i++;
					}
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Skelette", i);
						else
							detectedMobs.put("Skelett", i);
					}
				}
				if(hasAbility("detectSlime"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof Slime)
							i++;
					}
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Slimes", i);
						else
							detectedMobs.put("Slime", i);
					}
				}
				if(hasAbility("detectSpider"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof Spider)
							i++;
					}
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Spinnen", i);
						else
							detectedMobs.put("Spinne", i);
					}
				}
				if(hasAbility("detectZombie"))
				{
					int i = 0;
					for(Entity entity : entities)
					{
						if(entity instanceof Zombie)
							i++;
					}
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Zombies", i);
						else
							detectedMobs.put("Zombie", i);
					}
				}
				if(hasAbility("detectDiamond"))
				{
					int i = Helper.findBlock(loc, Material.DIAMOND_ORE, 20);
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Diamanterze", i);
						else
							detectedMobs.put("Diamanterz", i);
					}
				}
				if(hasAbility("detectGold"))
				{
					int i = Helper.findBlock(loc, Material.GOLD_ORE, 20);
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Golderze", i);
						else
							detectedMobs.put("Golderz", i);
					}
				}
				if(hasAbility("detectIron"))
				{
					int i = Helper.findBlock(loc, Material.IRON_ORE, 20);
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Eisenerze", i);
						else
							detectedMobs.put("Eisenerz", i);
					}
				}
				if(hasAbility("detectRedstone"))
				{
					int i = Helper.findBlock(loc, Material.REDSTONE_ORE, 20);
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Redstoneerze", i);
						else
							detectedMobs.put("Redstoneerz", i);
					}
				}
				if(hasAbility("detectLapisLazuli"))
				{
					int i = Helper.findBlock(loc, Material.LAPIS_ORE, 20);
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Lapis Erze", i);
						else
							detectedMobs.put("Lapis Erz", i);
					}
				}
				if(hasAbility("detectEmerald"))
				{
					int i = Helper.findBlock(loc, Material.EMERALD_ORE, 20);
					if(i > 0)
					{
						if(i > 1)
							detectedMobs.put("Smaragderze", i);
						else
							detectedMobs.put("Smaragderz", i);
					}
				}
				
				String mobs = "";
				for(String type : detectedMobs.keySet())
					mobs += " " + detectedMobs.get(type) + " " + type + ",";
				mobs = mobs.substring(0, mobs.length() - 1);
				
				if(!mobs.isEmpty())
					getPlayer().sendMessage("<Spürsinn>" + ChatColor.YELLOW + mobs + " in unmittelbarer Nähe.");
			}
		}
		
		// Klasse auswählen
		if(this.sclass == null || this.sclass.equalsIgnoreCase("None"))
		{
			for(Iterator<String> it = Classes.getSClasses().keySet().iterator(); it.hasNext(); )
			{
				String cs = it.next();
				SClass cl = Classes.getSClass(cs);
				Location choice = cl.getChoiceLocation();
				if(choice != null)
				{
					if(choice.getBlockX() == loc.getBlockX() && choice.getBlockY() == loc.getBlockY() && choice.getBlockZ() == loc.getBlockZ())
					{
						this.sclass = cs;
						sendMessage("Du hast die Profession des " + cl.getName() + "s angenommen");
					}
				}
			}
		}
	}
	
	public void onTick()
	{
		if(!this.isAFK())
		{
			this.tick++;
			if(this.tick >= 6000)
				this.setAFK(true);
		}
	}
	
	public boolean removeEmerald(int amount)
	{
		if(this.balance >= amount)
		{
			this.balance-=amount;
			Economy.payedIn += amount;
			return true;
		}
		else
			return false;
	}
	
	public SPlayer saveInventory()
	{
		return this.saveInventory(getPlayer());
	}
	
	public SPlayer saveInventory(Player player)
	{
		this.inventory = new LinkedList<InventoryEntry>();
		
		int i = 0;
		for(ItemStack stack : player.getInventory().getContents())
		{
			if (stack != null)
				this.inventory.add(new InventoryEntry(i, stack.clone()));
			i++;
		}
		
		ItemStack stack = player.getInventory().getHelmet();
		if(stack != null)
			this.inventory.add(new InventoryEntry(HELMET, stack.clone()));
		stack = player.getInventory().getChestplate();
		if(stack != null)
			this.inventory.add(new InventoryEntry(CHESTPLATE, stack.clone()));
		stack = player.getInventory().getLeggings();
		if(stack != null)
			this.inventory.add(new InventoryEntry(LEGGINGS, stack.clone()));
		stack = player.getInventory().getBoots();
		if(stack != null)
			this.inventory.add(new InventoryEntry(BOOTS, stack.clone()));
		
		player.getInventory().clear();
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		
		return this;
	}
	
	public void sendMessage(String message)
	{
		message = ScorpiaCraft.lang.get(message);
		if(Bukkit.getPlayer(this.name) != null)
			Bukkit.getPlayer(this.name).sendMessage(ChatColor.GOLD + "[ScorpiaCraft] " + ChatColor.WHITE + message);
	}
	
	public void setAFK(boolean afk)
	{		
		if(afk)
		{
			String afkname = getName() + " [AFK]";
			this.getPlayer().setDisplayName(afkname);
			if(afkname.length() > 16)
				afkname = afkname.substring(0, 16);
			this.getPlayer().setPlayerListName(afkname);
			ScorpiaCraft.broadcast(getName() + " ist AFK");
		}
		else if(this.afk)
		{
			this.getPlayer().setDisplayName(getName());
			this.getPlayer().setPlayerListName(getName());
			ScorpiaCraft.broadcast(getName() + " ist nicht mehr AFK");
		}
		
		this.tick = 0;
		this.afk = afk;
	}
	
	public void setBalance(int balance)
	{
		this.balance = balance;
	}
	
	public void setExpiration(long date)
	{
		this.expiration = date;
	}
	
	public SPlayer setHome(Location home)
	{
		this.home = new Location(home.getWorld(), home.getX(), home.getY(), home.getZ(), home.getYaw(), 0);
		return this;
	}
	
	public SPlayer setLastLocation(Location l)
	{
		lastLocation = l;
		return this;
	}
	
	public SPlayer setSelectedShop(String shop)
	{
		this.selectedShop = shop;
		return this;
	}
	
	public SPlayer setSelectedArena(String arena)
	{
		this.selectedArena = arena;
		return this;
	}
	
	private class InventoryEntry
	{
		private int index;
		private ItemStack item;

		/**
		 * Constructor
		 * @param index
		 * @param item
		 */
		public InventoryEntry(int index, ItemStack item)
		{
			this.index = index;
			this.item = item;
		}
	}
	
	private static final int HELMET = -10;
	private static final int CHESTPLATE = -11;
	private static final int LEGGINGS = -12;
	private static final int BOOTS = -13;
}
