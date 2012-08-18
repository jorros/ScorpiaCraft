package de.scorpiacraft.features;

import java.util.HashMap;

import org.bukkit.block.Block;

import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class Metadata
{
	private static HashMap<String, HashMap<String, Object>> metadata = new HashMap<String, HashMap<String, Object>>();
	
	public static void load(boolean reload)
	{
		if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/metadata.json"))
		{
			metadata = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/metadata.json"), new TypeToken<HashMap<String, HashMap<String, Object>>>() {}.getType());
		}
		else
		{
			ScorpiaCraft.log("CreateMetadataFile");
			ScorpiaCraft.save("metadata");
		}
	}
	
	public static void save()
	{
		Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/metadata.json", ScorpiaCraft.gson.toJson(metadata, new TypeToken<HashMap<String, HashMap<String, Object>>>() {}.getType()));
	}
	
	public static Object get(Block block, String key)
	{
		if(metadata.containsKey(block.getX() + "," + block.getY() + "," + block.getZ()))
		{
			return metadata.get(block.getX() + "," + block.getY() + "," + block.getZ()).get(key);
		}
		else
			return null;
	}
	
	public static int getInt(Block block, String key)
	{
		return ((Number)get(block, key)).intValue();
	}
	
	public static String getString(Block block, String key)
	{
		return (String)get(block, key);
	}
	
	public static void set(Block block, String key, Object value)
	{
		if(!metadata.containsKey(block.getX() + "," + block.getY() + "," + block.getZ()))
			metadata.put(block.getX() + "," + block.getY() + "," + block.getZ(), (new HashMap<String, Object>()));
		
		metadata.get(block.getX() + "," + block.getY() + "," + block.getZ()).put(key, value);
	}
	
	public static void remove(Block block, String key)
	{
		if(metadata.containsKey(block.getX() + "," + block.getY() + "," + block.getZ()))
		{
			metadata.get(block.getX() + "," + block.getY() + "," + block.getZ()).remove(key);
			if(metadata.get(block.getX() + "," + block.getY() + "," + block.getZ()).size() < 1)
				metadata.remove(block.getX() + "," + block.getY() + "," + block.getZ());
		}
	}
	
	public static boolean contains(Block block, String key)
	{
		if(metadata.containsKey(block.getX() + "," + block.getY() + "," + block.getZ()))
		{
			return metadata.get(block.getX() + "," + block.getY() + "," + block.getZ()).containsKey(key);
		}
		else
			return false;
	}
}
