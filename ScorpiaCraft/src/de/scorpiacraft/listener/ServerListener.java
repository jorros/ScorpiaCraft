package de.scorpiacraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import de.scorpiacraft.features.Dynmap;

public class ServerListener implements Listener
{	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPluginEnable(final PluginEnableEvent event)
	{
		if(event.getPlugin().getName().equalsIgnoreCase("dynmap"))
		{
			Dynmap.load(true);
		}
	}
}
