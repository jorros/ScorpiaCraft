package de.scorpiacraft.custom;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class Cobblestone extends GenericCubeCustomBlock
{
	public Cobblestone(String type)
	{
		super(ScorpiaCraft.p, type.replace("_", " ") + " Cobblestone", 4, new GenericCubeBlockDesign(ScorpiaCraft.p, Disk.getTexture("Cobblestone", type), 128));
	}
}