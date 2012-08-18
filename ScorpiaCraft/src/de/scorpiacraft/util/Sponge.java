package de.scorpiacraft.util;

import org.bukkit.World;
import org.bukkit.block.Block;

public class Sponge
{
	private static final int radius = 5;
	
	public static void processPlace(Block block)
	{
		if(block.getTypeId() != 19)
			return;
		for(int cx = -radius; cx <= radius; cx++)
		{
			for(int cy = -radius; cy <= radius; cy++)
			{
				for(int cz = -radius; cz <= radius; cz++)
				{
					if(isBlockWater(block.getWorld(), block.getX() + cx, block.getY() + cy, block.getZ() + cz))
                        block.getWorld().getBlockAt(block.getX() + cx, block.getY() + cy, block.getZ() + cz).setTypeId(0);
                }
            }
        }
	}
	
	private static boolean isBlockWater(World world, int ox, int oy, int oz)
	{
		Block block = world.getBlockAt(ox, oy, oz);
		int id = block.getTypeId();
		if(id == 8 || id == 9)
			return true;
		else
			return false;
	}
	
	private static void setBlockToWater(World world, int ox, int oy, int oz)
	{
		Block block = world.getBlockAt(ox, oy, oz);
		int id = block.getTypeId();
		if (id == 0)
			block.setTypeId(8);
	}
	
	public static void processRemove(Block block)
	{
		if(block.getTypeId() != 19)
			return;
		// The negative x edge
        int cx = block.getX() - radius - 1;
        for(int cy = block.getY() - radius - 1; cy <= block.getY() + radius + 1; cy++)
        {
        	for(int cz = block.getZ() - radius - 1; cz <= block.getZ() + radius + 1; cz++)
        	{
        		if(isBlockWater(block.getWorld(), cx, cy, cz))
        			setBlockToWater(block.getWorld(), cx + 1, cy, cz);
        		}
        }

        // The positive x edge
        cx = block.getX() + radius + 1;
        for(int cy = block.getY() - radius - 1; cy <= block.getY() + radius + 1; cy++)
        {
        	for(int cz = block.getZ() - radius - 1; cz <= block.getZ() + radius + 1; cz++)
        	{
        		if(isBlockWater(block.getWorld(), cx, cy, cz))
        			setBlockToWater(block.getWorld(), cx - 1, cy, cz);
        	}
        }

        // The negative y edge
        int cy = block.getY() - radius - 1;
        for(cx = block.getX() - radius - 1; cx <= block.getX() + radius + 1; cx++)
        {
        	for(int cz = block.getZ() - radius - 1; cz <= block.getZ() + radius + 1; cz++)
        	{
        		if(isBlockWater(block.getWorld(), cx, cy, cz))
        			setBlockToWater(block.getWorld(), cx, cy + 1, cz);
        	}
        }

        // The positive y edge
        cy = block.getY() + radius + 1;
        for(cx = block.getX() - radius - 1; cx <= block.getX() + radius + 1; cx++)
        {
        	for(int cz = block.getZ() - radius - 1; cz <= block.getZ() + radius + 1; cz++)
        	{
        		if(isBlockWater(block.getWorld(), cx, cy, cz))
        			setBlockToWater(block.getWorld(), cx, cy - 1, cz);
        	}
        }

        // The negative z edge
        int cz = block.getZ() - radius - 1;
        for(cx = block.getX() - radius - 1; cx <= block.getX() + radius + 1; cx++)
        {
        	for(cy = block.getY() - radius - 1; cy <= block.getY() + radius + 1; cy++)
        	{
        		if(isBlockWater(block.getWorld(), cx, cy, cz))
        			setBlockToWater(block.getWorld(), cx, cy, cz + 1);
        	}
        }

        // The positive z edge
        cz = block.getZ() + radius + 1;
        for(cx = block.getX() - radius - 1; cx <= block.getX() + radius + 1; cx++)
        {
        	for(cy = block.getY() - radius - 1; cy <= block.getY() + radius + 1; cy++)
        	{
        		if(isBlockWater(block.getWorld(), cx, cy, cz))
        			setBlockToWater(block.getWorld(), cx, cy, cz - 1);
        	}
        }
	}
}
