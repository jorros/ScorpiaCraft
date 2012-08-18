package de.scorpiacraft.custom;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class StoneBlock extends GenericCubeCustomBlock
{
	public StoneBlock(String type)
	{
		super(ScorpiaCraft.p, type.replace("_", " ") + " Stone", 1, new GenericCubeBlockDesign(ScorpiaCraft.p, Disk.getTexture("Stone", type), 128));
	}
}
