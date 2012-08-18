package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdBack implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			
			if(sp.isVIP())
				p.teleport(sp.getLastLocation());
			else
				sp.sendMessage("NoRight");

			
			return true;
		}
		
		return false;
	}
}
