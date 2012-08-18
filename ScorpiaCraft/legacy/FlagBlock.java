package de.scorpiacraft.custom;


import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.ScorpiaCraft.Rank;
import de.scorpiacraft.features.Nations;
import de.scorpiacraft.obj.SNation;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Disk;

public class FlagBlock extends GenericCubeCustomBlock
{
	public FlagBlock(Plugin plugin)
	{		
		super(plugin, "Flag", 4, new GenericCubeBlockDesign(plugin, Disk.getTexture("Nations", "Flag"), 128));
		this.setHardness(30);
	}
	
	public void onBlockPlace(World world, int x, int y, int z, LivingEntity living)
	{
		if(living instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)living);
			if(sp.getRank() == Rank.Admin || sp.getRank() == Rank.Leader || sp.getRank() == Rank.CoLeader)
			{
				sp.getNation().getFlags().add(world.getBlockAt(x, y, z).getLocation());
				Nations.calculate(sp.getNation());
				Nations.setMarker("flag", "Auﬂenposten von " + sp.getNation().getTag(), world.getBlockAt(x, y, z).getLocation());
			}
		}
	}
	
	public void onBlockDestroyed(World world, int x, int y, int z) 
	{
		SNation n = Nations.getNation(world.getBlockAt(x, y, z).getChunk());
		n.getFlags().remove(world.getBlockAt(x, y, z).getLocation());
		Nations.calculate(n);
		Nations.removeMarker("flag", world.getBlockAt(x, y, z).getLocation());
	}
}
