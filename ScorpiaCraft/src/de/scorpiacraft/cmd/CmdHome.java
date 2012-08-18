package de.scorpiacraft.cmd;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Plot;
import de.scorpiacraft.obj.SPlayer;

public class CmdHome implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			Location loc = p.getLocation();
	
			if(args.length > 0 && args[0].equalsIgnoreCase("set"))
			{
				if(Plot.isPlayersPlot(loc.getChunk(), sp.getName()))
				{
					if(sp.removeEmerald(1))
					{
						sp.setHome(loc);
						sp.sendMessage("HomeMoved");
					}
					else
					{
						sp.sendMessage("NotEnoughMoney");
					}
				}
				else
				{
					sp.sendMessage("NotInPlot");
				}
			}
			else
				p.teleport(sp.getHome());
			return true;
		}
		
		return false;
	}
	
}
