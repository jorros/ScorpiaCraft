package de.scorpiacraft.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdTp implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)sender);
			
			if(args.length == 1)
			{
				if(Bukkit.getPlayer(args[0]) != null)
				{
					sp.getPlayer().teleport(Bukkit.getPlayer(args[0]));
				}
			}
			else if(args.length > 1)
			{
				if(Bukkit.getPlayer(args[0]) != null)
				{
					if(Bukkit.getPlayer(args[1]) != null)
					{
						Bukkit.getPlayer(args[0]).teleport(Bukkit.getPlayer(args[1]));
					}
				}
			}
			
			return true;
		}
		return false;
	}
	
}
