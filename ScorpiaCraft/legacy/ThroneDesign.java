package de.scorpiacraft.custom;

import java.util.ArrayList;

import org.getspout.spoutapi.block.design.GenericBlockDesign;
import org.getspout.spoutapi.block.design.Quad;
import org.getspout.spoutapi.block.design.Texture;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.util.Disk;

public class ThroneDesign extends GenericBlockDesign
{
	ArrayList<Quad> tempNewQuads  = new ArrayList<Quad>();
	
	public ThroneDesign()
	{
		Texture tex = new Texture(ScorpiaCraft.p, Disk.getTexture("Nations", "Throne"), 256, 128, 128);
		this.setTexture(ScorpiaCraft.p, tex);
		this.setQuadNumber(10);
		this.setBoundingBox(0, 0, 0, 1, 2, 1);
		
		Quad right1 = new Quad(0, tex.getSubTexture(0));
		right1.addVertex(0, 0.5f, 2f, 0f);
		right1.addVertex(1, 0f, 2f, 0f);
		right1.addVertex(2, 0f, 0f, 0f);
		right1.addVertex(3, 0.5f, 0f, 0f);

		Quad right2 = new Quad(1, tex.getSubTexture(0));
		right2.addVertex(0, 1f, 0.5f, 0f);
		right2.addVertex(1, 0.5f, 0.5f, 0f);
		right2.addVertex(2, 0.5f, 0f, 0f);
		right2.addVertex(3, 1f, 0f, 0f);

		Quad left1 = new Quad(2, tex.getSubTexture(0));
		left1.addVertex(0, 0.5f, 0.5f, 1f);
		left1.addVertex(1, 1f, 0.5f, 1f);
		left1.addVertex(2, 1f, 0f, 1f);
		left1.addVertex(3, 0.5f, 0f, 1f);

		Quad left2 = new Quad(3, tex.getSubTexture(0));
		left2.addVertex(0, 0f, 2f, 1f);
		left2.addVertex(1, 0.5f, 2f, 1f);
		left2.addVertex(2, 0.5f, 0f, 1f);
		left2.addVertex(3, 0f, 0f, 1f);

		Quad up1 = new Quad(4, tex.getSubTexture(0));
		up1.addVertex(0, 0.5f, 2f, 1f);
		up1.addVertex(1, 0f, 2f, 1f);
		up1.addVertex(2, 0f, 2f, 0f);
		up1.addVertex(3, 0.5f, 2f, 0f);

		Quad up2 = new Quad(5, tex.getSubTexture(1));
		up2.addVertex(0, 1f, 0.5f, 1f);
		up2.addVertex(1, 0.5f, 0.5f, 1f);
		up2.addVertex(2, 0.5f, 0.5f, 0f);
		up2.addVertex(3, 1f, 0.5f, 0f);

		Quad front1 = new Quad(6, tex.getSubTexture(1));
		front1.addVertex(0, 0.5f, 2f, 1f);
		front1.addVertex(1, 0.5f, 2f, 0f);
		front1.addVertex(2, 0.5f, 0.5f, 0f);
		front1.addVertex(3, 0.5f, 0.5f, 1f);

		Quad front2 = new Quad(7, tex.getSubTexture(0));
		front2.addVertex(0, 1f, 0.5f, 1f);
		front2.addVertex(1, 1f, 0.5f, 0f);
		front2.addVertex(2, 1f, 0f, 0f);
		front2.addVertex(3, 1f, 0f, 1f);

		Quad down = new Quad(8, tex.getSubTexture(0));
		down.addVertex(0, 1f, 0f, 0f);
		down.addVertex(1, 1f, 0f, 1f);
		down.addVertex(2, 0f, 0f, 1f);
		down.addVertex(3, 0f, 0f, 0f);

		Quad back = new Quad(9, tex.getSubTexture(0));
		back.addVertex(0, 0f, 2f, 0f);
		back.addVertex(1, 0f, 2f, 1f);
		back.addVertex(2, 0f, 0f, 1f);
		back.addVertex(3, 0f, 0f, 0f);
		
		ArrayList<Quad> list = new ArrayList<Quad>();
		list.add(right1);
		list.add(right2);
		list.add(left1);
		list.add(left2);
		list.add(up1);
		list.add(up2);
		list.add(front1);
		list.add(front2);
		list.add(down);
		list.add(back);
		
		for(int i = 0; i < list.size(); i++)
		{
			Quad q = list.get(i);
			this.setQuad(q);
		}

		this.setRenderPass(1);
	}
}
