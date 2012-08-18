package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdInstinct implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)sender);
			sp.toggleInstinct();
			if(sp.isInstinctEnabled())
				sp.sendMessage("Instinkt eingeschaltet");
			else
				sp.sendMessage("Instinkt ausgeschaltet");
			return true;
		}
		return false;
	}
}
