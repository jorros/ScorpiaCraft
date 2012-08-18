package de.scorpiacraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.cmd.*;
import de.scorpiacraft.features.*;
import de.scorpiacraft.lang.Lang;
import de.scorpiacraft.listener.*;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Disk;

public class ScorpiaCraft extends JavaPlugin
{
	public static ScorpiaCraft p;
	public static Gson gson;
	public static Map<String, SPlayer> Players = new ConcurrentHashMap<String, SPlayer>();
	public static Lang lang;
	public static QueryServer query = null;
	public static Map<String, Object> Settings = new ConcurrentHashMap<String, Object>();

	public static Calendar cal;
	
	public static String getRankName(Player p)
	{
		if(p.hasPermission("de.scorpiacraft.admin"))
			return "[Admin]";
		if(p.hasPermission("de.scorpiacraft.moderator"))
			return "[Moderator]";
		else
			return "";
	}
	
	public static ChatColor getRankColor(Player p)
	{
		if(p.hasPermission("de.scorpiacraft.admin"))
			return ChatColor.DARK_PURPLE;
		if(p.hasPermission("de.scorpiacraft.moderator"))
			return ChatColor.BLUE;
		else
			return ChatColor.WHITE;
	}
	
	@Override
	public void onDisable()
	{
		save("all");
		
		if(query != null)
		{
			try
			{
				query.getListener().close();
			}
			catch(IOException e)
			{
				log("Server konnte nicht abgeschaltet werden");
			}
		}
	}

	@Override
	public void onEnable()
	{
		p = this;
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Location.class, new LocationSerializer());
		builder.registerTypeAdapter(Location.class, new LocationDeserializer());
		gson = builder.create();
		
		// Registriere Sprachen
		lang = new Lang();
		
		// Registriere Listener
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new BlockListener(), this);
		pm.registerEvents(new EntityListener(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new ServerListener(), this);
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new VehicleListener(), this);
		
		// Registriere Befehle
		getCommand("save").setExecutor(new CmdSave());
		getCommand("load").setExecutor(new CmdLoad());
		getCommand("plot").setExecutor(new CmdPlot());
		getCommand("shop").setExecutor(new CmdShop());
		getCommand("home").setExecutor(new CmdHome());
		getCommand("back").setExecutor(new CmdBack());
		getCommand("tp").setExecutor(new CmdTp());
		getCommand("balance").setExecutor(new CmdBalance());
		getCommand("adyll").setExecutor(new CmdAdyll());
		getCommand("help").setExecutor(new CmdHelp());
		getCommand("afk").setExecutor(new CmdAfk());
		getCommand("info").setExecutor(new CmdInfo());
		getCommand("spleef").setExecutor(new CmdSpleef());
		getCommand("instinct").setExecutor(new CmdInstinct());
		getCommand("vip").setExecutor(new CmdVip());
		getCommand("class").setExecutor(new CmdClass());
		
		// Lade Spielerdaten
		load("all", false);
		log("ConfigLoaded");
		
		cal = Calendar.getInstance();
		Economy.todayDate = cal.get(Calendar.DAY_OF_YEAR);
		
		// Starte Request Server
		try
		{
			query = new QueryServer(this.getServer().getIp(), 26026);
			query.start();
		}
		catch(IOException ex)
		{
			ScorpiaCraft.log("Konnte Abfrage Server nicht starten");
		}
		
		// Starte Scheduler
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				for(SPlayer sp : ScorpiaCraft.getPlayers())
				{
					sp.onTick();
				}
			}
		}, 0L, 1L);
		
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run()
			{					
				if(Economy.todayDate != Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
				{
					Economy.todayDate = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
					Economy.payedIn = 0;
				}
					
				Economy.adyllPrice = (int)(300 + (Math.random()*100) * Math.pow(Math.E, Economy.payedIn / (ScorpiaCraft.cal.get(Calendar.HOUR_OF_DAY)*3600 + ScorpiaCraft.cal.get(Calendar.MINUTE) * 60 + ScorpiaCraft.cal.get(Calendar.SECOND))));
				
				ScorpiaCraft.save("all");
			}
		}, 0L, 36000L);
	}
	
	public static SPlayer getPlayer(Player player)
	{
		if(Players.containsKey(player.getName()))
			return Players.get(player.getName());
		else
			return null;
	}
	
	public static SPlayer getPlayer(String player)
	{
		for(String p : Players.keySet())
		{
			if(p.equalsIgnoreCase(player))
				return Players.get(p);
		}
		
		return null;
	}
	
	public static ArrayList<SPlayer> getPlayers()
	{
		return getPlayers(false);
	}
	
	public static ArrayList<SPlayer> getPlayers(boolean offline)
	{
		ArrayList<SPlayer> sps = new ArrayList<SPlayer>();
		for(SPlayer sp : Players.values())
		{
			if(!offline)
			{
				if(sp.isOnline())
					sps.add(sp);
			}
			else
				sps.add(sp);
		}
		
		return sps;
	}
	
	public static void load(String type, boolean reload)
	{
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("settings"))
		{
			if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/settings.json"))
				Settings = gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/settings.json"), new TypeToken<Map<String, Object>>() {}.getType());
			else
			{
				log("CreateSettingsFile");
				save("settings");
			}
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("player"))
		{
			if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/players.json"))
				Players = gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/players.json"), new TypeToken<Map<String, SPlayer>>() {}.getType());
			else
			{
				log("CreatePlayerFile");
				save("player");
			}
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("dynmap"))
		{
			Dynmap.load(reload);
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("economy"))
		{
			Economy.load(reload);
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("plots"))
		{
			Plot.load(reload);
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("spleef"))
		{
			Spleef.load(reload);
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("classes"))
		{
			Classes.load(reload);
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("metadata"))
		{
			Metadata.load(reload);
		}
	}
	
	public static void save(String type)
	{
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("settings"))
		{
			Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/settings.json", gson.toJson(Settings, new TypeToken<Map<String, Object>>() {}.getType()));
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("player"))
		{
			Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/players.json", gson.toJson(Players, new TypeToken<Map<String, SPlayer>>() {}.getType()));
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("dynmap"))
		{
			Dynmap.save();
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("economy"))
		{
			Economy.save();
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("plots"))
		{
			Plot.save();
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("spleef"))
		{
			Spleef.save();
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("classes"))
		{
			Classes.save();
		}
		
		if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("metadata"))
		{
			Metadata.save();
		}
	}
	
	public static void log(String info)
	{
		info = lang.get(info);
		p.getLogger().info(info);
	}
	
	public static void broadcast(String info)
	{
		info = lang.get(info);
		Bukkit.broadcastMessage(ChatColor.GOLD + "[ScorpiaCraft] " + ChatColor.WHITE + info);
	}
}
