package de.scorpiacraft.cmd;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.ScorpiaCraft.Rank;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.features.Plot.PlotType;
import de.scorpiacraft.obj.SPlayer;

public class CmdSet implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)sender);
			Location loc = ((Player)sender).getLocation();
			
			if(args[0].equalsIgnoreCase("home"))
			{
				if(Plot.isPlayersPlot(loc.getChunk(), sp.getName()))
				{
					if(sp.removeCoins(10))
					{
						sp.setHome(loc);
						sp.sendMessage("HomeMoved");
					}
					else
					{
						sp.sendMessage("NotEnoughMoney");
						return true;
					}
				}
				else
				{
					sp.sendMessage("NotInPlot");
					return true;
				}
			}
			
			if(sp.getRank().getLevel() <= Rank.Moderator.getLevel())
			{
			
				if(args.length < 1)
					return false;
				
				if(args[0].equalsIgnoreCase("plot"))
				{					
					if(args.length < 2)
						return false;
					
					PlotType type = PlotType.None;
					
					if(args[1].equalsIgnoreCase("Server"))
						type = PlotType.Server;
					else if(args[1].equalsIgnoreCase("Sale"))
						type = PlotType.Sale;
					else if(args[1].equalsIgnoreCase("NPC"))
						type = PlotType.NPC;
					else if(args[1].equalsIgnoreCase("None"))
						Plot.removePlot(loc.getChunk());
					
					Plot.setPlot(loc.getChunk(), type);
				}
			}
		}
		return true;
	}
	
}
