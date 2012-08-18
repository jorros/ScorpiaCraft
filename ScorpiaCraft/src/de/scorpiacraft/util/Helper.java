package de.scorpiacraft.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Helper
{
	public static int findBlock(Location loc, Material mat, int radius)
	{
		Block block;
		int counter = 0;
		for(int z = radius * -1; z <= radius; z++)
		 {
			 for(int x = radius * -1; x <= radius; x++)
			 {
				 for(int y = radius * -1; y <= radius; y++)
				 {
					 block = loc.getWorld().getBlockAt((int) loc.getX()+x, (int) loc.getY()+y, (int) loc.getZ()+z);
					 if(block.getTypeId() == mat.getId())
					 {
						 counter++;
					 }
				 }
			 }
		 }
		return counter;
	}
	
	public static boolean isNumeric(String str)
	{
		try
		{
			Integer.parseInt(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
	
	public static String ChatColorToString(ChatColor col)
	{
		switch(col)
		{				
			case BLACK:
				return "000000";
				
			case DARK_BLUE:
				return "0000AA";
				
			case DARK_GREEN:
				return "00AA00";
				
			case DARK_AQUA:
				return "00AAAA";
				
			case DARK_RED:
				return "AA0000";
				
			case DARK_PURPLE:
				return "AA00AA";
				
			case GOLD:
				return "FFAA00";
				
			case GRAY:
				return "AAAAAA";
				
			case DARK_GRAY:
				return "555555";
				
			case BLUE:
				return "5555FF";
				
			case GREEN:
				return "55FF55";
				
			case AQUA:
				return "55FFFF";
				
			case RED:
				return "FF5555";
				
			case LIGHT_PURPLE:
				return "FF55FF";
				
			case YELLOW:
				return "FFFF55";
				
			default:
				return "FFFFFF";
		}
	}
	
	public static float normalAngle(float angle) {
        while (angle <= -180) angle += 360;
        while (angle > 180) angle -= 360;
        return angle;
    }
	
	public static BlockFace yawToFace (float yaw) {
        return yawToFace(yaw, true);
    }
	
    public static BlockFace yawToFace(float yaw, boolean useSubCardinalDirections)
    {
        yaw = normalAngle(yaw);
        if (useSubCardinalDirections) {
            switch ((int) yaw) {
            case 0 : return BlockFace.NORTH;
            case 45 : return BlockFace.NORTH_EAST;
            case 90 : return BlockFace.EAST;
            case 135 : return BlockFace.SOUTH_EAST;
            case 180 : return BlockFace.SOUTH;
            case 225 : return BlockFace.SOUTH_WEST;
            case 270 : return BlockFace.WEST;
            case 315 : return BlockFace.NORTH_WEST;
            }
            //Let's apply angle differences
            if (yaw >= -22.5 && yaw < 22.5) {
                return BlockFace.NORTH;
            } else if (yaw >= 22.5 && yaw < 67.5) {
                return BlockFace.NORTH_EAST;
            } else if (yaw >= 67.5 && yaw < 112.5) {
                return BlockFace.EAST;
            } else if (yaw >= 112.5 && yaw < 157.5) {
                return BlockFace.SOUTH_EAST;
            } else if (yaw >= -67.5 && yaw < -22.5) {
                return BlockFace.NORTH_WEST;
            } else if (yaw >= -112.5 && yaw < -67.5) {
                return BlockFace.WEST;
            } else if (yaw >= -157.5 && yaw < -112.5) {
                return BlockFace.SOUTH_WEST;
            } else {
                return BlockFace.SOUTH;
            }
        } else {
            switch ((int) yaw) {
            case 0 : return BlockFace.SOUTH;
            case 90 : return BlockFace.WEST;
            case 180 : return BlockFace.NORTH;
            case 270 : return BlockFace.EAST;
            }
            //Let's apply angle differences
            if (yaw >= -45 && yaw < 45) {
                return BlockFace.SOUTH;
            } else if (yaw >= 45 && yaw < 135) {
                return BlockFace.WEST;
            } else if (yaw >= -135 && yaw < -45) {
                return BlockFace.EAST;
            } else {
                return BlockFace.NORTH;
            }
        }
    }
}
