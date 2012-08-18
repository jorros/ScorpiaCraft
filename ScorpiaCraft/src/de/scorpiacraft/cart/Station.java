package de.scorpiacraft.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import de.scorpiacraft.util.Helper;

public class Station implements Serializable
{
	private static final long serialVersionUID = 544972241837889618L;
	private int x, y, z;
	private float yaw;
	private String name;
	private UUID world;
	private UUID uuid;
	private int face;
	
	public Station(int x, int y, int z, float yaw, String name, World world)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.name = name;
		this.world = world.getUID();
		this.uuid = UUID.randomUUID();
		
		this.refreshDir();
	}
	
	public Station(int x, int y, int z, float yaw, String name, World world, UUID uuid)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.name = name;
		this.world = world.getUID();
		this.uuid = uuid;
		
		this.refreshDir();
	}
	
	private void refreshDir()
	{	
		switch(Helper.yawToFace(this.yaw, false))
		{
		case SOUTH: // SOUTH
			this.face = 0;
			break;
			
		case WEST: // WEST
			this.face = 1;
			break;
			
		case EAST: // EAST
			this.face = 2;
			break;
			
		case NORTH: // NORTH
			this.face = 3;
			break;
		}
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int getZ()
	{
		return this.z;
	}
	
	public float getYaw()
	{
		return this.yaw;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public BlockFace getFace()
	{
		switch(this.face)
		{
		case 0: // SOUTH
			return BlockFace.SOUTH;
			
		case 1: // WEST
			return BlockFace.WEST;
			
		case 2: // EAST
			return BlockFace.EAST;
			
		case 3: // NORTH
			return BlockFace.NORTH;
		}
		return BlockFace.NORTH;
	}
	
	public Vector getDirection()
	{
		Vector dir = new Vector();
		switch(this.face)
		{
		case 0: // SOUTH
			dir.setZ(1);
			break;
			
		case 1: // WEST
			dir.setX(-1);
			break;
			
		case 2: // EAST
			dir.setX(1);
			break;
			
		case 3: // NORTH
			dir.setZ(-1);
			break;
		}
		return dir;
	}
	
	public ArrayList<Line> getLines(Cart pCart)
	{
		ArrayList<Line> temp = new ArrayList<Line>();
		for(Iterator<Line> i = pCart.lines.values().iterator(); i.hasNext();)
		{
			Line pLine = i.next();
			if(pLine.getName().split("-")[0].equals(this.name))
				temp.add(pLine);
		}
		
		return temp;
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(this.world);
	}
	
	public Location getLocation()
	{
		return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, 0);
	}
	
	public UUID getUUID()
	{
		return this.uuid;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setZ(int z)
	{
		this.z = z;
	}
	
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
		this.refreshDir();
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}
