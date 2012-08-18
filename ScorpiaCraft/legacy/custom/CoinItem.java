package de.scorpiacraft.custom;


import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.material.item.GenericCustomItem;

import de.scorpiacraft.util.Disk;

public class CoinItem extends GenericCustomItem
{
	public CoinItem(Plugin plugin)
	{
        super(plugin, "Coin", Disk.getTexture("Nations", "Coin"));
    }
}
