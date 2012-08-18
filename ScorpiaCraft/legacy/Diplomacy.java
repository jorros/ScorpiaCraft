package de.scorpiacraft.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;

import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SNation;
import de.scorpiacraft.util.Disk;

public class Diplomacy
{
	private static ArrayList<Map<String, Short>> diplomatic = new ArrayList<Map<String, Short>>(); // 0 - Status, 1 - Request
	
	public static int get(SNation one, SNation two)
	{
		if(one == null || two == null)
			return 1;
		if(one.equals(two))
			return 2;
		if(diplomatic.size() == 0)
			return 1;
		if(diplomatic.get(0).get(one.getTag() + "-" + two.getTag()) != null)
			return diplomatic.get(0).get(one.getTag() + "-" + two.getTag()).intValue();
		else if(diplomatic.get(0).get(two.getTag() + "-" + one.getTag()) != null)
			return diplomatic.get(0).get(two.getTag() + "-" + one.getTag()).intValue();
		else
			return 1;
	}
	
	public static void set(SNation one, SNation two, int dipl)
	{
		if(diplomatic.get(0).get(one.getTag() + "-" + two.getTag()) != null)
			diplomatic.get(0).put(one.getTag() + "-" + two.getTag(), (short)dipl);
		else if(diplomatic.get(0).get(two.getTag() + "-" + one.getTag()) != null)
			diplomatic.get(0).put(two.getTag() + "-" + one.getTag(), (short)dipl);
		else
			diplomatic.get(0).put(one.getTag() + "-" + two.getTag(), (short)dipl);
		
		switch(dipl)
		{
			case 0:
				ScorpiaCraft.broadcast(one.getColor() + one.getName() + ChatColor.DARK_RED + " hat " + two.getColor() + two.getName() + ChatColor.DARK_RED + " den Krieg erklärt!");
				break;
				
			case 1:
				ScorpiaCraft.broadcast(one.getColor() + one.getName() + ChatColor.DARK_GRAY + " hat mit " + two.getColor() + two.getName() + ChatColor.DARK_GRAY + " ein Neutralitätsabkommen abgeschlossen!");
				break;
				
			case 2:
				ScorpiaCraft.broadcast(one.getColor() + one.getName() + ChatColor.DARK_GREEN + " hat mit " + two.getColor() + two.getName() + ChatColor.DARK_GREEN + " einen Friedensvertrag unterzeichnet!");
				break;
				
			case 3:
				ScorpiaCraft.broadcast(one.getColor() + one.getName() + ChatColor.DARK_BLUE + " hat mit " + two.getColor() + two.getName() + ChatColor.DARK_BLUE + " ein Bündnis abgeschlossen!");
				break;
		}
	}
	
	public static int getRequest(SNation one, SNation two)
	{
		if(diplomatic.size() == 0)
			return 1;
		if(diplomatic.get(1).get(one.getTag() + "-" + two.getTag()) == null)
			return -1;
		else
			return diplomatic.get(1).get(one.getTag() + "-" + two.getTag()).intValue();
	}
	
	public static void setRequest(SNation one, SNation two, int dipl)
	{
		if(getRequest(two, one) == dipl)
		{
			set(two, one, dipl);
			diplomatic.get(1).remove(two.getTag() + "-" + one.getTag());
		}
		else
			diplomatic.get(1).put(one.getTag() + "-" + two.getTag(), (short)dipl);
	}
	
	public static ArrayList<SNation> getNations(SNation nation, int status)
	{
		ArrayList<SNation> list = new ArrayList<SNation>();
		
		for(SNation n : Nations.Nation.values())
		{
			if(get(nation, n) == status)
				list.add(n);
		}
		
		return list;
	}
	
	public static String getString(int dipl)
	{
		switch(dipl)
		{
			case 0:
				return ScorpiaCraft.lang.get("Enemy");
				
			case 2:
				return ScorpiaCraft.lang.get("Peace");
				
			case 3:
				return ScorpiaCraft.lang.get("Alliance");
		}
		return ScorpiaCraft.lang.get("Neutral");
	}
	
	public static void load(boolean reload)
	{
		if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/diplomacy.json"))
			diplomatic = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/diplomacy.json"), new TypeToken<ArrayList<Map<String, Short>>>() {}.getType());
		else
		{
			ScorpiaCraft.log("CreateDiplomacyFile");
			diplomatic.add(new HashMap<String, Short>());
			diplomatic.add(new HashMap<String, Short>());
			ScorpiaCraft.save("diplomacy");
		}
	}
	
	public static void save()
	{
		Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/diplomacy.json", ScorpiaCraft.gson.toJson(diplomatic, new TypeToken<ArrayList<Map<String, Short>>>() {}.getType()));
	}
}
