package de.scorpiacraft.custom;

import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class NekroiBlock extends GenericCubeCustomBlock
{
	public NekroiBlock(String name, String type)
	{
		super(ScorpiaCraft.p, "Nekroi " + name.replace("_", " "), 7, new GenericCubeBlockDesign(ScorpiaCraft.p, Disk.getTexture("Nekroi", name), 128));
		
		if(type.equals("Light"))
		{
			this.setLightLevel(15);
		}
		else if(type.equals("Glass"))
			this.setOpaque(false);
		
		this.setHardness(16000);
	}
}
