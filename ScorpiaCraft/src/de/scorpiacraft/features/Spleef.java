package de.scorpiacraft.features;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SArena;
import de.scorpiacraft.util.Disk;

public class Spleef
{
	private static Map<String, SArena> arenas = new HashMap<String, SArena>();
	public static LinkedList<SArena> start = new LinkedList<SArena>();
	
	public static SArena getArena(String name)
	{
		return arenas.get(name);
	}
	
	public static void createArena(String owner, String name)
	{
		arenas.put(name, (new SArena()).setName(name).setOwner(owner));
	}
		
	public static void removeArena(String name)
	{
		arenas.remove(name);
	}
	
	public static void load(boolean reload)
	{
		if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/spleef.json"))
		{
			arenas = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/spleef.json"), new TypeToken<Map<String, SArena>>() {}.getType());
		}
		else
		{
			ScorpiaCraft.log("CreateSpleefFile");
			ScorpiaCraft.save("spleef");
		}
	}
	
	public static void save()
	{
		Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/spleef.json", ScorpiaCraft.gson.toJson(arenas, new TypeToken<Map<String, SArena>>() {}.getType()));
	}
}
