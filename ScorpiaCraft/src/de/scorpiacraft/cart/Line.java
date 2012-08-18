package de.scorpiacraft.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Line implements Serializable
{
	private static final long serialVersionUID = -8243024513258351688L;
	private Station start, end;
	private String name;
	private ArrayList<UUID> nodesWorld;
	private ArrayList<Integer> nodesX;
	private ArrayList<Integer> nodesY;
	private ArrayList<Integer> nodesZ;
	private ArrayList<Float> nodesYaw;
	
	public Line(Station start, Station end, String name)
	{
		this.start = start;
		this.end = end;
		this.name = name;
		this.nodesWorld = new ArrayList<UUID>();
		this.nodesX = new ArrayList<Integer>();
		this.nodesY = new ArrayList<Integer>();
		this.nodesZ = new ArrayList<Integer>();
		this.nodesYaw = new ArrayList<Float>();
	}
	
	public Station getStart()
	{
		return this.start;
	}
	
	public Station getEnd()
	{
		return this.end;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Location getNode(int index)
	{
		return new Location(Bukkit.getWorld(this.nodesWorld.get(index)), this.nodesX.get(index), this.nodesY.get(index), this.nodesZ.get(index), this.nodesYaw.get(index), 0);
	}
	
	public boolean isEndStation(Location loc)
	{
		Block pBlock = loc.getBlock();
		if(pBlock.getX() == this.end.getX() && pBlock.getY() == this.end.getY() && pBlock.getZ() == this.end.getZ())
			return true;
		return false;
	}
	
	public int getNode(Location loc)
	{
		Block pBlock = loc.getBlock();
		for(int i = 0; i < this.nodesWorld.size(); i++)
		{
			Location pLoc = getNode(i);
			if(pBlock.getX() == pLoc.getX() && pBlock.getY() == pLoc.getY() && pBlock.getZ() == pLoc.getZ())
			{
				return i;
			}
		}	
		return -1;
	}
	
	public void clearNodes()
	{
		this.nodesWorld.clear();
		this.nodesX.clear();
		this.nodesY.clear();
		this.nodesZ.clear();
		this.nodesYaw.clear();
	}
	
	public void setStart(Station start)
	{
		this.start = start;
	}
	
	public void setEnd(Station end)
	{
		this.end = end;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void addNode(Location node)
	{
		this.nodesWorld.add(node.getWorld().getUID());
		this.nodesX.add((int)node.getX());
		this.nodesY.add((int)node.getY());
		this.nodesZ.add((int)node.getZ());
		this.nodesYaw.add(node.getYaw());
	}
	
	public void deleteNode(Location node)
	{
		deleteNode(this.getNode(node));
	}
	
	public void deleteNode(int index)
	{
		this.nodesWorld.remove(index);
		this.nodesX.remove(index);
		this.nodesY.remove(index);
		this.nodesZ.remove(index);
		this.nodesYaw.remove(index);
	}
}
