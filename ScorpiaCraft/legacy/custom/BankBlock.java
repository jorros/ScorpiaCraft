package de.scorpiacraft.custom;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Dynmap;
import de.scorpiacraft.features.Economy;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Disk;

public class BankBlock extends GenericCubeCustomBlock
{
	public BankBlock()
	{
		super(ScorpiaCraft.p, "Bank", 58, new GenericCubeBlockDesign(ScorpiaCraft.p, new Texture(ScorpiaCraft.p, Disk.getTexture("Nations", "Bank"), 512, 128, 128), new int[] { 0, 1, 1, 1, 1, 2 }));
	}
	
	@Override
	public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player)
	{
		SPlayer sp = ScorpiaCraft.getPlayer(player);
		
		if(sp.checkSpoutCraft())
		{
			SpoutItemStack stack = new SpoutItemStack(player.getItemInHand());
			
			if(stack.getAmount() == 0)
			{
				if(sp.getBalance() >= 64)
				{
					player.setItemInHand(new SpoutItemStack(Economy.coinItem, 64));
					sp.removeCoins(64);
				}
				else
				{
					player.setItemInHand(new SpoutItemStack(Economy.coinItem, sp.getBalance()));
					sp.setBalance(0);
				}
			}
			else if(stack.getMaterial() instanceof CoinItem)
			{
				sp.addCoins(stack.getAmount());
				player.setItemInHand(null);
			}
			else if(stack.getMaterial() instanceof Adyll)
			{
				sp.addCoins(stack.getAmount() * Economy.adyllPrice);
				player.setItemInHand(null);
			}
		}
		return true;
	}
	
	@Override
	public void onBlockPlace(World world, int x, int y, int z, LivingEntity living)
	{
		Dynmap.setMarker("bank", "Bank", world.getBlockAt(x, y, z).getLocation());
	}
	
	@Override
	public void onBlockDestroyed(World world, int x, int y, int z) 
	{
		Dynmap.removeMarker("bank", world.getBlockAt(x, y, z).getLocation());
	}
}
