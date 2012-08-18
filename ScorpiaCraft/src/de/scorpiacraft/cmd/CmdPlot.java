package de.scorpiacraft.cmd;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.features.Plot.PlotType;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.obj.SPlot;

public class CmdPlot implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			Location loc = p.getLocation();
			SPlot plot = Plot.getPlot(loc.getChunk());
			
			if(args.length < 1)
			{
				sp.sendMessage("Daten zum Grundstück:");
				if(plot == null)
					sp.sendMessage("Besitzer: Wildnis");
				else if(plot.getType() == PlotType.Server)
					sp.sendMessage("Besitzer: Server");
				else if(plot.getType() == PlotType.Sale)
					sp.sendMessage("Besitzer: Zum Verkauf");
				else if(plot.getType() == PlotType.NPC)
					sp.sendMessage("Besitzer: NPC");
				else
					sp.sendMessage("Besitzer: " + plot.getHolder());
				
				if(plot != null)
					sp.sendMessage("Zuletzt festgelegter Preis: " + plot.getPrice());
			}
			else if(args[0].equalsIgnoreCase("set") && p.hasPermission("scorpiacraft.plot.set"))
			{
				PlotType type = PlotType.None;
				SPlayer pl = null;
				
				if(args.length < 2)
					return false;
				else if(args[1].equalsIgnoreCase("Server"))
					type = PlotType.Server;
				else if(args[1].equalsIgnoreCase("Sale"))
					type = PlotType.Sale;
				else if(args[1].equalsIgnoreCase("NPC"))
					type = PlotType.NPC;
				else if(args[1].equalsIgnoreCase("None"))
				{
					Plot.removePlot(loc.getChunk());
					sp.sendMessage("Grundstück entfernt");
					return true;
				}
				else if(args[1].equalsIgnoreCase("player") && args.length > 2)
				{
					pl = ScorpiaCraft.getPlayer(args[2]);
					type = PlotType.Purchased;
					if(pl == null)
						sp.sendMessage("Spieler " + args[2] + " existiert nicht.");
				}
				
				if(type != PlotType.None)
				{
					if(pl != null)
						Plot.setPlot(loc.getChunk(), pl.getName());
					else
						Plot.setPlot(loc.getChunk(), type);
					Plot.calculatePrice(loc.getChunk());
					sp.sendMessage("Grundstück gesetzt");
				}
			}
			else if(args[0].equalsIgnoreCase("buy") && p.hasPermission("scorpiacraft.plot.buy"))
			{
				if(plot.getType() == PlotType.Sale)
				{
					ItemStack item = new ItemStack(p.getInventory().getItemInHand());
					boolean drawBank = false;
					
					int price = plot.getPrice();
					
					if(!(item.getTypeId() == 388) || item.getAmount() < price)
					{
						if(sp.getBalance() >= price)
							drawBank = true;
						else
						{
							sp.sendMessage("NotEnoughMoney");
							return true;
						}
					}
					
					if(drawBank)
					{
						sp.removeEmerald(price);
					}
					else
					{
						item.setAmount(item.getAmount() - price);
						p.getInventory().setItemInHand(item);
					}
					
					Plot.setPlot(loc.getChunk(), sp.getName());
					sp.sendMessage("Du hast das Grundstück für " + Plot.getPlot(loc.getChunk()).getPrice() + "e gekauft");
				}
				else
					sp.sendMessage("Das Grundstück steht nicht zum Verkauf");
			}
			else if(args[0].equalsIgnoreCase("sell") && p.hasPermission("scorpiacraft.plot.sell"))
			{
				if(plot.getHolder().equals(p.getName()) && plot.getType() == PlotType.Purchased)
				{
					Plot.calculatePrice(loc.getChunk());
					sp.addEmerald(plot.getPrice());
					plot.setType(PlotType.Sale);
					sp.sendMessage("Du hast das Grundstück für " + plot.getPrice() + "e verkauft");
				}
				else
					sp.sendMessage("Das Grundstück gehört nicht dir");
			}
			
			return true;
		}
		return false;
	}
	
}
