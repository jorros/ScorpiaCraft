package de.scorpiacraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Metadata;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.obj.SArena;
import de.scorpiacraft.obj.SPlayer;

public class PlayerListener implements Listener
{	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(final PlayerMoveEvent event)
	{
		try
		{			
			// Spieler Update ausführen
			SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
			sp.onMove(event.getPlayer().getLocation());
		}
		catch(Exception e)
		{
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerBucketEmpty(final PlayerBucketEmptyEvent event)
	{		
		event.setCancelled(Plot.checkPlot(event.getBlockClicked().getLocation().getChunk(), event.getPlayer(), false));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerBucketFill(final PlayerBucketFillEvent event)
	{		
		event.setCancelled(Plot.checkPlot(event.getBlockClicked().getLocation().getChunk(), event.getPlayer(), false));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
		Material mat = event.getClickedBlock().getType();
		
		if(mat == Material.FENCE_GATE || mat == Material.STONE_BUTTON || mat == Material.LEVER || mat == Material.IRON_DOOR || mat == Material.WOODEN_DOOR || mat == Material.TRAP_DOOR || mat == Material.WOOD_DOOR || mat == Material.IRON_DOOR_BLOCK)
			event.setCancelled(Plot.checkPlot(event.getClickedBlock().getLocation().getChunk(), event.getPlayer(), true));
		
		if(mat == Material.WALL_SIGN && Metadata.contains(event.getClickedBlock(), "sell") && event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Sign sign = (Sign)event.getClickedBlock().getState();
			Dispenser dispenser = null;
			
			for(int x = -1; x <= 1; x++)
			{
				if(dispenser != null)
					break;
				for(int y = -1; y <= 1; y++)
				{
					if(dispenser != null)
						break;
					for(int z = -1; z <= 1; z++)
					{
						if(event.getClickedBlock().getRelative(x, y, z).getState() instanceof Dispenser)
							dispenser = (Dispenser)event.getClickedBlock().getRelative(x, y, z).getState();
						if(dispenser != null)
							break;
					}
				}
			}

			if(dispenser != null)
			{
				int price = Metadata.getInt(event.getClickedBlock(), "sell");
				Material item = Material.getMaterial(sign.getLine(0));
				
				if(item != null)
				{
					ItemStack emerald = new ItemStack(event.getPlayer().getInventory().getItemInHand());
					boolean payed = false;
					
					if(!(emerald.getTypeId() == 388) || emerald.getAmount() < price)
					{
						if(sp.getBalance() >= price)
						{
							sp.removeEmerald(price);
							payed = true;
						}
						else
						{
							sp.sendMessage("NotEnoughMoney");
						}
					}
					else
					{
						emerald.setAmount(emerald.getAmount() - price);
						event.getPlayer().getInventory().setItemInHand(emerald);
						payed = true;
					}
					
					if(payed)
					{
						dispenser.getInventory().addItem(new ItemStack(item, 1));
						dispenser.dispense();
						sp.sendMessage("Du hast " + price + "e gezahlt.");
					}
				}
			}
		}
		
		if(event.hasItem())
		{
			if(event.getItem().getTypeId() == 373)
			{
				event.getPlayer().setTotalExperience(event.getPlayer().getTotalExperience() + sp.getExperience("usePotion"));
			}
		}
	}
	
	@EventHandler(priority  = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent event)
	{
		try
		{
			SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
			
			// Erstlogin
			if(sp == null)
			{
				sp = new SPlayer(event.getPlayer());
				sp.addEmerald(5);
				sp.sendMessage("Willkommen auf ScorpiaCraft. Mit " + ChatColor.LIGHT_PURPLE + "/help" + ChatColor.WHITE + " siehst du alle möglichen Befehle.");
			}
			
			
			// Wenn Server überfüllt ist
			if(Bukkit.getServer().getMaxPlayers() <= Bukkit.getServer().getOnlinePlayers().length)
			{
				if(sp.isVIP())
				{
					for(Player p : Bukkit.getOnlinePlayers())
					{
						SPlayer p2 = ScorpiaCraft.getPlayer(p);
						if(!p2.isVIP())
						{
							p.kickPlayer("Der Server ist voll - VIPs werden bevorzugt!");
							return;
						}
					}
					event.allow();
				}
				else
					event.disallow(Result.KICK_FULL, "Der Server ist voll - VIPs werden bevorzugt!");
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
		sp.onLogin();
		
		String text = ScorpiaCraft.getRankColor(event.getPlayer()) + ScorpiaCraft.getRankName(event.getPlayer()) + ChatColor.WHITE + " " + sp.getName();
		
		text += " hat Scorpia betreten.";
		
		event.setJoinMessage(text);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
		String text = ScorpiaCraft.getRankColor(event.getPlayer()) + ScorpiaCraft.getRankName(event.getPlayer()) + ChatColor.WHITE + " " + sp.getName();
		
		if(sp.getSpleefArena() != null)
		{
			try
			{
				event.getPlayer().teleport(sp.getSpleefArena().getSpectator());
			}
			catch(Exception ex)
			{
				
			}
			sp.setSpleefArena(null);
			sp.loadGameMode(event.getPlayer());
			sp.loadInventory(event.getPlayer());
			
			text += " ist ausgeschieden und";
		}
		
		sp.onLogout();
		
		text += " hat Scorpia verlassen.";
		
		event.setQuitMessage(text);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(final AsyncPlayerChatEvent event)
	{		
		SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
		
		ChatColor nameprefix = ChatColor.WHITE;
		if(sp.isVIP())
			nameprefix = ChatColor.YELLOW;
		
		String rank = "";
		try
		{
			rank = ScorpiaCraft.getRankColor(event.getPlayer()) + ScorpiaCraft.getRankName(event.getPlayer());
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
		}
		event.setFormat(rank + nameprefix + " <%1$s>" + ChatColor.WHITE + " %2$s" + ChatColor.WHITE);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleport(final PlayerTeleportEvent event)
	{
		if(event.getCause() == TeleportCause.UNKNOWN)
			return;
		
		// Setzte letzte bekannte Position
		SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
		sp.setLastLocation(event.getFrom());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
		
		if(sp.getSpleefArena() != null)
		{
			try
			{
				event.setRespawnLocation(sp.getSpleefArena().getSpectator());
			}
			catch(Exception ex)
			{
				
			}
			sp.setSpleefArena(null);
			sp.loadGameMode(event.getPlayer());
			sp.loadInventory(event.getPlayer());
			return;
		}
		
		if(sp.isVIP())
			sp.loadInventory(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(final PlayerDeathEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)event.getEntity());
			
			if(sp.getSpleefArena() != null)
			{
				SArena sa = sp.getSpleefArena();
				sa.removePlayer(sp.getName());
				event.setDeathMessage(sp.getName() + " ist ausgeschieden.");
				
				if(sa.checkWinner())
				{
					ScorpiaCraft.broadcast(sa.getWinner() + " ist der Sieger");
					sa.endGame();
				}
				return;
			}
			
			sp.setLastLocation(event.getEntity().getLocation());
			event.setKeepLevel(true);
			event.setDroppedExp(0);
			
			if(sp.isVIP())
			{
				event.getDrops().clear();
				sp.saveInventory((Player)event.getEntity());
			}
		}
	}
}
