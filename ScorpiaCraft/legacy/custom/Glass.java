package de.scorpiacraft.custom;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class Glass extends GenericCubeCustomBlock
{
	public Glass(String type)
	{
		super(ScorpiaCraft.p, type.replace("_", " ") + " Glass", 20, (GenericCubeBlockDesign)new GenericCubeBlockDesign(ScorpiaCraft.p, Disk.getTexture("Glass", type), 128).setRenderPass(1));
	}
}

