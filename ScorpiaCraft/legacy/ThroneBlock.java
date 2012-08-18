package de.scorpiacraft.custom;


import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericRadioButton;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.material.block.GenericCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.ScorpiaCraft.Rank;
import de.scorpiacraft.features.Diplomacy;
import de.scorpiacraft.features.Nations;
import de.scorpiacraft.obj.SButton;
import de.scorpiacraft.obj.SNation;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.obj.STextField;

public class ThroneBlock extends GenericCustomBlock
{
	ThroneDesign des;
	
	public ThroneBlock(Plugin plugin)
	{
		super(plugin, "Throne", 7, new ThroneDesign());
		this.setHardness(16000);
	}
	
	@Override
	public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player)
    {
		//Arrow arr = world.spawnArrow(new Location(world, x, y, z), new Vector(0, -1, 0), 0, 0);
		//arr.setPassenger(player);
		//arr.remove();
		
		SNation nation = Nations.getNation(world.getBlockAt(x, y, z).getChunk());
		SPlayer sp = ScorpiaCraft.getPlayer(player);
		
		if(!ScorpiaCraft.getPlayer(player).checkSpoutCraft())
			return true;
		
		if(nation != null)
		{			
			if(nation.getTag().equals("Delphi") && sp.getRank() == Rank.Leader || sp.getRank() == Rank.Admin)
			{
				GenericPopup popup = new GenericPopup();
				
				GenericLabel name = new GenericLabel("Diplomatie");
				name.setAuto(true).setScale(2);
				name.setHeight(GenericLabel.getStringHeight(name.getText(), 2)).setWidth(GenericLabel.getStringWidth(name.getText(), 2));
				name.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(5).shiftYPos(5);
				int i = 0;
				
				for(SNation n : Nations.Nation.values())
				{
					if(!n.getTag().equals(sp.getNation().getTag()))
					{
						GenericLabel faction = new GenericLabel(n.getColor() + n.getName());
						faction.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(5).shiftYPos(30 + i*20);
						faction.setScale(1);
						faction.setHeight(GenericLabel.getStringHeight(faction.getText())).setWidth(GenericLabel.getStringWidth(faction.getText()));
						popup.attachWidget(this.getPlugin(), faction);
						
						Color diplcol = null;
						switch(Diplomacy.get(sp.getNation(), n))
						{
							case 0:
								diplcol = new Color("FF0000");
								break;
								
							case 2:
								diplcol = new Color("00FF00");
								break;
								
							case 3:
								diplcol = new Color("0000FF");
								break;
								
							default:
								diplcol = new Color("FFFFFF");
								break;
						}
						
						GenericLabel status = new GenericLabel(Diplomacy.getString(Diplomacy.get(sp.getNation(), n)));
						status.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(150).shiftYPos(30 + i*20);
						status.setScale(1);
						status.setHeight(GenericLabel.getStringHeight(status.getText())).setWidth(GenericLabel.getStringWidth(status.getText()));
						status.setTextColor(diplcol);
						popup.attachWidget(this.getPlugin(), status);
							
						GenericRadioButton enemybtn = new GenericRadioButton("Feind");
						enemybtn.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(200).shiftYPos(30 + i*20 - 5);
						enemybtn.setGroup(i);
						enemybtn.setHeight(18).setWidth(18);
						popup.attachWidget(this.getPlugin(), enemybtn);
						
						GenericRadioButton neutralbtn = new GenericRadioButton("Neutral");
						neutralbtn.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(250).shiftYPos(30 + i*20 - 5);
						neutralbtn.setGroup(i);
						neutralbtn.setHeight(18).setWidth(18);
						popup.attachWidget(this.getPlugin(), neutralbtn);
						
						GenericRadioButton tradebtn = new GenericRadioButton("Frieden");
						tradebtn.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(310).shiftYPos(30 + i*20 - 5);
						tradebtn.setGroup(i);
						tradebtn.setHeight(18).setWidth(18);
						popup.attachWidget(this.getPlugin(), tradebtn);
						
						GenericRadioButton allybtn = new GenericRadioButton("Allianz");
						allybtn.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(370).shiftYPos(30 + i*20 - 5);
						allybtn.setGroup(i);
						allybtn.setHeight(18).setWidth(18);
						popup.attachWidget(this.getPlugin(), allybtn);
						
						if(Diplomacy.getRequest(sp.getNation(), n) == 0)
							enemybtn.setSelected(true);
						else if(Diplomacy.getRequest(sp.getNation(), n) == 1)
							neutralbtn.setSelected(true);
						else if(Diplomacy.getRequest(sp.getNation(), n) == 2)
							tradebtn.setSelected(true);
						else if(Diplomacy.getRequest(sp.getNation(), n) == 3)
							allybtn.setSelected(true);
						else if(Diplomacy.get(sp.getNation(), n) == 0)
							enemybtn.setSelected(true);
						else if(Diplomacy.get(sp.getNation(), n) == 1)
							neutralbtn.setSelected(true);
						else if(Diplomacy.get(sp.getNation(), n) == 2)
							tradebtn.setSelected(true);
						else if(Diplomacy.get(sp.getNation(), n) == 3)
							allybtn.setSelected(true);
					}
					i++;
				}
				
				popup.attachWidget(this.getPlugin(), name);
				
				player.getMainScreen().attachPopupScreen(popup);
			}
			
			if(!nation.equals(sp.getNation()))
				return false;
		}
		else
			return false;
		
		if(sp.getRank() == Rank.Leader || sp.getRank() == Rank.CoLeader)
		{
			GenericPopup popup = new GenericPopup();
			
			// Titel
			GenericLabel name = new GenericLabel(nation.getColor() + nation.getTag());
			name.setAuto(true).setScale(2);
			name.setHeight(GenericLabel.getStringHeight(name.getText(), 2)).setWidth(GenericLabel.getStringWidth(name.getText(), 2));
			name.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(5).shiftYPos(5);
			
			// Untertitel
			GenericLabel tag = new GenericLabel(nation.getColor() + nation.getName());
			tag.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(5).shiftYPos(GenericLabel.getStringHeight(name.getText(), 2)+10);
			tag.setHeight(GenericLabel.getStringHeight(tag.getText())).setWidth(GenericLabel.getStringWidth(tag.getText()));
			
			// Informationen König/Rechte Hand
			ArrayList<SPlayer> lead = ScorpiaCraft.getPlayers(nation, Rank.Leader);
			
			String king = "Leer";
			try
			{
				king = lead.get(0).getName();
			}
			catch(Exception ex)
			{
			}
			lead = ScorpiaCraft.getPlayers(nation, Rank.CoLeader);
			String coking = "Leer";
			try
			{
				coking = lead.get(0).getName();
			}
			catch(Exception ex)
			{
			}
			GenericLabel leaders = new GenericLabel(king + " (König)\n" + coking + " (Rechte Hand)");
			leaders.setAnchor(WidgetAnchor.TOP_LEFT).shiftXPos(5).shiftYPos(50);
			leaders.setHeight(GenericLabel.getStringHeight(leaders.getText())).setWidth(GenericLabel.getStringWidth(leaders.getText()));
			
			// Schatzkammer
			GenericLabel treasury = new GenericLabel("Schatzkammer: " + nation.getBalance() + "c");
			treasury.setAnchor(WidgetAnchor.CENTER_LEFT).shiftXPos(5);
			treasury.setHeight(GenericLabel.getStringHeight(treasury.getText())).setWidth(GenericLabel.getStringWidth(treasury.getText()));
			
			// Grundstücke
			GenericLabel cityhead = new GenericLabel("Grundstückspreis in der Hauptstadt:");
			cityhead.setAnchor(WidgetAnchor.CENTER_LEFT).shiftXPos(5).shiftYPos(30);
			cityhead.setHeight(GenericLabel.getStringHeight(cityhead.getText())).setWidth(GenericLabel.getStringWidth(cityhead.getText()));
			STextField city = new STextField("capitalprice");
			city.setAnchor(WidgetAnchor.CENTER_LEFT).shiftXPos(5).shiftYPos(45);
			city.setWidth(60).setHeight(15);
			city.setText(String.valueOf(nation.getPrice("capital")));
			
			GenericLabel regionhead = new GenericLabel("Grundstückspreis in den Regionen:");
			regionhead.setAnchor(WidgetAnchor.CENTER_LEFT).shiftXPos(5).shiftYPos(70);
			regionhead.setHeight(GenericLabel.getStringHeight(cityhead.getText())).setWidth(GenericLabel.getStringWidth(cityhead.getText()));
			STextField region = new STextField("regionprice");
			region.setAnchor(WidgetAnchor.CENTER_LEFT).shiftXPos(5).shiftYPos(85);
			region.setWidth(60).setHeight(15);
			region.setText(String.valueOf(nation.getPrice("region")));
			
			// Spieler Liste
			GenericListWidget playerlist = new GenericListWidget();
			playerlist.setAnchor(WidgetAnchor.TOP_CENTER).shiftYPos(5);
			playerlist.setHeight(200).setWidth(200);
			
			ArrayList<SPlayer> plist = ScorpiaCraft.getPlayers(nation, Rank.None);
			for(SPlayer p : plist)
			{
				playerlist.addItem(new ListWidgetItem(p.getName(), ScorpiaCraft.rankName(p.getRank())));
			}
			
			// Rechten Hand erheben
			SButton promote = new SButton("Rechte Hand", "promote");
			promote.setAnchor(WidgetAnchor.TOP_CENTER).shiftYPos(210);
			promote.setHeight(20).setWidth(100);
			
			// Rechten Hand erheben
			SButton delete = new SButton("Entfernen", "remove");
			delete.setAnchor(WidgetAnchor.TOP_CENTER).shiftYPos(210).shiftXPos(105);
			delete.setHeight(20).setWidth(100);
			
			popup.attachWidget(this.getPlugin(), name);
			popup.attachWidget(this.getPlugin(), tag);
			popup.attachWidget(this.getPlugin(), leaders);
			popup.attachWidget(this.getPlugin(), treasury);
			popup.attachWidget(this.getPlugin(), cityhead);
			popup.attachWidget(this.getPlugin(), city);
			popup.attachWidget(this.getPlugin(), regionhead);
			popup.attachWidget(this.getPlugin(), region);
			popup.attachWidget(this.getPlugin(), playerlist);
			popup.attachWidget(this.getPlugin(), promote);
			popup.attachWidget(this.getPlugin(), delete);
			
			player.getMainScreen().attachPopupScreen(popup);
		}
		
		return true;
    }
	
	public void onBlockPlace(World world, int x, int y, int z, LivingEntity living)
	{
		if(living instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)living);
			if(sp.getRank() == Rank.Admin || sp.getRank() == Rank.Leader)
			{
				sp.getNation().setThrone(world.getBlockAt(x, y, z).getLocation());
				Nations.calculateThrone(sp.getNation());
				Nations.setMarker("throne", "Hauptstadt von " + sp.getNation().getTag(), world.getBlockAt(x, y, z).getLocation());
			}
		}
	}
	
	public void onBlockDestroyed(World world, int x, int y, int z) 
	{
		SNation n = Nations.getNation(world.getBlockAt(x, y, z).getChunk());
		if(n != null)
		{
			n.removeThrone();
			Nations.calculateThrone(n);
			Nations.removeMarker("throne", world.getBlockAt(x, y, z).getLocation());
		}
	}
}
