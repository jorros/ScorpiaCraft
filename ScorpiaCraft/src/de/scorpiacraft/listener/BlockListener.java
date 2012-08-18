package de.scorpiacraft.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Metadata;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Helper;
import de.scorpiacraft.util.Sponge;

public class BlockListener implements Listener
{	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPaintingBreakByEntity(final PaintingBreakByEntityEvent event)
	{		
		if(event.getRemover() instanceof Player)
		{
			event.setCancelled(Plot.checkPlot(event.getPainting().getLocation().getChunk(), (Player)event.getRemover(), false));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(final SignChangeEvent event)
	{
		if(event.getLine(2).equalsIgnoreCase("Sell") && event.getPlayer().isOp() && Helper.isNumeric(event.getLine(1)))
		{
			Metadata.set(event.getBlock(), "sell", Integer.parseInt(event.getLine(1)));
			event.setLine(1, "Preis: " + event.getLine(1) + "e");
			event.setLine(2, "Schild drücken");
		}
		else if(event.getBlock().getMetadata("sc_sell").size() > 0)
			event.getBlock().removeMetadata("sc_sell", ScorpiaCraft.p);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPaintingPlace(final PaintingPlaceEvent event)
	{		
		if(ScorpiaCraft.getPlayer(event.getPlayer()).getSpleefArena() != null)
			event.setCancelled(true);
		else
		{
			event.setCancelled(Plot.checkPlot(event.getBlock().getLocation().getChunk(), event.getPlayer(), false));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if(ScorpiaCraft.getPlayer(event.getPlayer()).getSpleefArena() != null)
			event.setCancelled(true);
		else
		{
			event.setCancelled(Plot.checkPlot(event.getBlock().getLocation().getChunk(), event.getPlayer(), false));
		}
		
		if(!event.isCancelled())
			Sponge.processPlace(event.getBlockPlaced());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		if(ScorpiaCraft.getPlayer(event.getPlayer()).getSpleefArena() != null)
		{
			if(event.getBlock().getType() != Material.GRASS || !ScorpiaCraft.getPlayer(event.getPlayer()).getSpleefArena().isPlaying())
				event.setCancelled(true);
			else
			{
				event.getBlock().getDrops().clear();
			}
		}
		else
		{
			event.setCancelled(Plot.checkPlot(event.getBlock().getLocation().getChunk(), event.getPlayer(), false));
		}
		
		if(!event.isCancelled())
		{
			SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
			Player p = event.getPlayer();
			
			int multiplier = 0;
			if(sp.hasAbility("multiDropOre"))
			{					
				int val = (int)(Math.random() * 100);
				
				if(val > p.getLevel())
					multiplier = 0;
				else if(val < p.getLevel())
					multiplier = 1;
				else if(val == p.getLevel())
					multiplier = 2;
			}
			
			switch(event.getBlock().getTypeId())
			{
				case 16:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineCoal"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
					
				case 15:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineIron"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
					
				case 14:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineGold"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
					
				case 56:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineDiamond"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
					
				case 73:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineRedstone"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
					
				case 21:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineLapis"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
					
				case 129:
					event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("mineEmerald"));
					for(ItemStack item : event.getBlock().getDrops())
					{
						if(multiplier > 0)
						{
							item.setAmount(multiplier);
							event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
						}
					}
					break;
			}
			Sponge.processRemove(event.getBlock());
		}
	}
}
