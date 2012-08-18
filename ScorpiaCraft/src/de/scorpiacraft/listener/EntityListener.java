package de.scorpiacraft.listener;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.obj.SPlayer;

public class EntityListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCreatureSpawn(final CreatureSpawnEvent event)
	{
		if(event.getSpawnReason() != SpawnReason.NATURAL)
			return;
		
		// Wenn "Böse" Mobs spawnen
		if((event.getEntity() instanceof Monster || event.getEntity() instanceof Slime || event.getEntity() instanceof Flying) && (event.getSpawnReason() != SpawnReason.SPAWNER || event.getSpawnReason() != SpawnReason.SPAWNER_EGG))
		{
			if(Plot.getPlot(event.getLocation().getChunk()) != null)
				event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeath(final EntityDeathEvent event)
	{
		if(event.getEntity() instanceof Creature)
		{
			Creature m = (Creature)event.getEntity();
			if(m.getKiller() != null)
			{
				SPlayer sp = ScorpiaCraft.getPlayer(m.getKiller());
				
				if((sp.hasAbility("multiDropZombie") && m instanceof Zombie) || 
						(sp.hasAbility("multiDropSkeleton") && m instanceof Skeleton) || 
						(sp.hasAbility("multiDropSpider") && m instanceof Spider) || 
						(sp.hasAbility("multiDropPigZombie") && m instanceof PigZombie) ||
						(sp.hasAbility("multiDropMagmaCube") && m instanceof MagmaCube) ||
						(sp.hasAbility("multiDropGhast") && m instanceof Ghast) ||
						(sp.hasAbility("multiDropEnderman") && m instanceof Enderman) ||
						(sp.hasAbility("multiDropCreeper") && m instanceof Creeper) ||
						(sp.hasAbility("multiDropBlaze") && m instanceof Blaze))
				{
					int multiplier = 1;
					
					int val = (int)(Math.random() * 100);
					
					if(val > m.getKiller().getLevel())
						multiplier = 1;
					else if(val < m.getKiller().getLevel())
						multiplier = 2;
					else if(val == m.getKiller().getLevel())
						multiplier = 3;
					
					for(ItemStack item : event.getDrops())
					{
						item.setAmount(item.getAmount() * multiplier);
					}
				}
				
				if(m instanceof Zombie)
					event.setDroppedExp(sp.getExperience("killZombie"));
				else if(m instanceof Skeleton)
					event.setDroppedExp(sp.getExperience("killSkeleton"));
				else if(m instanceof Spider)
					event.setDroppedExp(sp.getExperience("killSpider"));
				else if(m instanceof PigZombie)
					event.setDroppedExp(sp.getExperience("killPigZombie"));
				else if(m instanceof MagmaCube)
					event.setDroppedExp(sp.getExperience("killMagmaCube"));
				else if(m instanceof Ghast)
					event.setDroppedExp(sp.getExperience("killGhast"));
				else if(m instanceof Enderman)
					event.setDroppedExp(sp.getExperience("killEnderman"));
				else if(m instanceof Creeper)
					event.setDroppedExp(sp.getExperience("killCreeper"));
				else if(m instanceof Blaze)
					event.setDroppedExp(sp.getExperience("killBlaze"));
				else if(m instanceof Chicken)
					event.setDroppedExp(sp.getExperience("killChicken"));
				else if(m instanceof Cow)
					event.setDroppedExp(sp.getExperience("killCow"));
				else if(m instanceof Ocelot)
					event.setDroppedExp(sp.getExperience("killOcelot"));
				else if(m instanceof Pig)
					event.setDroppedExp(sp.getExperience("killPig"));
				else if(m instanceof Sheep)
					event.setDroppedExp(sp.getExperience("killSheep"));
				else if(m instanceof Wolf)
					event.setDroppedExp(sp.getExperience("killWolf"));
				else if(m instanceof Squid)
					event.setDroppedExp(sp.getExperience("killSquid"));
				else if(m instanceof Silverfish)
					event.setDroppedExp(sp.getExperience("killSilverfish"));
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event)
	{		
		// Wenn Opfer von irgendwas verletzt wurde
		if(event.getEntity() instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)event.getEntity());
			
			// Sonderkräfte der Klassen
			if((event.getCause() == DamageCause.DROWNING) && sp.hasAbility("immunityWater"))
			{
				event.setCancelled(true);
				return;
			}
			if((event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) && sp.hasAbility("immunityFire") && sp.getSpleefArena() == null)
			{
				event.setCancelled(true);
				return;
			}
			if((event.getCause() == DamageCause.POISON) && sp.hasAbility("immunityPoison"))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByEntity(final EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)event.getDamager());
			Player p = (Player)event.getDamager();
			
			if(event.getEntity() instanceof Player && sp.getSpleefArena() != null)
				event.setCancelled(true);
			
			if(sp.hasAbility("betterAttack"))
			{
				int multiplier = 1;				
				int val = (int)(Math.random() * 100);
				
				if(val > p.getLevel())
					multiplier = 1;
				else if(val < p.getLevel())
					multiplier = 2;
				else if(val == p.getLevel())
					multiplier = 3;
				
				event.setDamage(event.getDamage() * multiplier);
			}
		}
	}
}
