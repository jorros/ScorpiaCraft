package de.scorpiacraft.features;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import com.google.gson.reflect.TypeToken;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SClass;
import de.scorpiacraft.util.Disk;

public class Classes
{
	private static Map<String, SClass> Classes = new HashMap<String, SClass>();
	
	public static void load(boolean reload)
	{
		if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/classes.json"))
		{
			Classes = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/classes.json"), new TypeToken<Map<String, SClass>>() {}.getType());
		}
		else
		{
			ScorpiaCraft.log("CreateClassFile");
			Classes.put("None", (new SClass()).setName("Keine Klasse"));
			ScorpiaCraft.save("classes");
		}
		
		if(!reload)
		{
			// Transmute
			ShapelessRecipe trans = new ShapelessRecipe(new ItemStack(Material.IRON_BLOCK));
			trans.addIngredient(Material.STONE);
			trans.addIngredient(Material.GHAST_TEAR);
			Bukkit.getServer().addRecipe(trans);
			
			trans = new ShapelessRecipe(new ItemStack(Material.GOLD_BLOCK));
			trans.addIngredient(Material.STONE);
			trans.addIngredient(Material.BLAZE_POWDER);
			Bukkit.getServer().addRecipe(trans);
			
			trans = new ShapelessRecipe(new ItemStack(Material.DIAMOND_BLOCK));
			trans.addIngredient(Material.IRON_BLOCK);
			trans.addIngredient(Material.FERMENTED_SPIDER_EYE);
			Bukkit.getServer().addRecipe(trans);
			
			/*trans = new ShapelessRecipe(new ItemStack(Material.EMERALD_BLOCK));
			trans.addIngredient(Material.IRON_BLOCK);
			trans.addIngredient(Material.EYE_OF_ENDER);
			Bukkit.getServer().addRecipe(trans);*/
		}
	}
	
	public static void save()
	{
		Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/classes.json", ScorpiaCraft.gson.toJson(Classes, new TypeToken<Map<String, SClass>>() {}.getType()));
	}
	
	public static Map<String, SClass> getSClasses()
	{
		return Classes;
	}
	
	public static SClass getSClass(String name)
	{
		if(Classes.containsKey(name))
			return Classes.get(name);
		else
			return Classes.get("None");
	}
}
