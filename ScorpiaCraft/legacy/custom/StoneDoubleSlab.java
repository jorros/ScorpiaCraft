package de.scorpiacraft.custom;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class StoneDoubleSlab extends GenericCubeCustomBlock
{
	public StoneDoubleSlab(String type)
	{
		super(ScorpiaCraft.p, type.replace("_", " ") + " Stone Double Slab", 43, new GenericCubeBlockDesign(ScorpiaCraft.p, new Texture(ScorpiaCraft.p, Disk.getTexture("StoneDoubleSlab", type), 256, 128, 128), new int[] { 1, 0, 0, 0, 0, 1 }));
	}
}
