package de.scorpiacraft.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class InventoryListener implements Listener
{	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onCraftItem(final CraftItemEvent event)
	{		
		Player p = (Player)event.getWhoClicked();
		SPlayer sp = ScorpiaCraft.getPlayer(p);
		
		if(event.getRecipe().getResult().getTypeId() == 373)
			p.setTotalExperience(p.getTotalExperience() + sp.getExperience("brewPotion"));
		
		if(event.getRecipe() instanceof ShapelessRecipe)
		{
			String trans = getTransmutation((ShapelessRecipe)event.getRecipe());
			if(!trans.equals("None"))
				p.setTotalExperience(p.getTotalExperience() + sp.getExperience("transmute" + trans));
		}
	}
	
	private ItemStack getTransmute(Player p, String type)
	{
		SPlayer sp = ScorpiaCraft.getPlayer(p);
		
		if(sp.hasAbility("transmute" + type))
		{
			int multiplier = 1;
			if(sp.hasAbility("multiDropTransmute"))
			{					
				int val = (int)(Math.random() * 100);
				
				if(val > p.getLevel())
					multiplier = 1;
				else if(val < p.getLevel())
					multiplier = 2;
				else if(val == p.getLevel())
					multiplier = 3;
			}
			
			if(type.equals("Iron"))
			{
				return (new ItemStack(Material.IRON_BLOCK, multiplier));
			}
			else if(type.equals("Gold"))
			{
				return (new ItemStack(Material.GOLD_BLOCK, multiplier));
			}
			else if(type.equals("Diamond"))
			{
				return (new ItemStack(Material.DIAMOND_BLOCK, multiplier));
			}
			else if(type.equals("Emerald"))
			{
				return (new ItemStack(Material.EMERALD_BLOCK));
			}
		}
		
		return new ItemStack(Material.AIR);
	}
	
	private String getTransmutation(ShapelessRecipe recipe)
	{
		Material baseMaterial = null, transmuter = null;
		
		for(ItemStack item : recipe.getIngredientList())
		{
			switch(item.getTypeId())
			{
				case 1: // Stone
					baseMaterial = Material.STONE;
					break;
					
				case 42: // Iron Block
					baseMaterial = Material.IRON_BLOCK;
					break;
					
				case 376:
					transmuter = Material.FERMENTED_SPIDER_EYE;
					break;
					
				case 381:
					transmuter = Material.EYE_OF_ENDER;
					break;
					
				case 377:
					transmuter = Material.BLAZE_POWDER;
					break;
					
				case 370:
					transmuter = Material.GHAST_TEAR;
					break;	
					
				default:
					return "None";
			}
		}
		
		if(baseMaterial == Material.STONE && transmuter == Material.GHAST_TEAR)
			return "Iron";
		else if(baseMaterial == Material.STONE && transmuter == Material.BLAZE_POWDER)
			return "Gold";
		else if(baseMaterial == Material.IRON_BLOCK && transmuter == Material.FERMENTED_SPIDER_EYE)
			return "Diamond";
		else if(baseMaterial == Material.IRON_BLOCK && transmuter == Material.EYE_OF_ENDER)
			return "Emerald";
		else
			return "None";
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPrepareItemCraft(final PrepareItemCraftEvent event)
	{
		if(event.getRecipe() instanceof ShapelessRecipe)
		{
			String trans = getTransmutation((ShapelessRecipe)event.getRecipe());
			
			if(!event.isRepair() && event.getView().getPlayer() instanceof Player && !trans.equals("None"))
			{
				event.getInventory().setResult(getTransmute((Player)event.getView().getPlayer(), trans));
			}
		}
	}
}
