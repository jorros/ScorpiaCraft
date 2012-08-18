package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;

public class CmdLoad implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		ScorpiaCraft.load("all", true);
		if(sender instanceof ConsoleCommandSender)
			ScorpiaCraft.log("Loaded");
		else if(((Player)sender).isOp())
			ScorpiaCraft.getPlayer((Player)sender).sendMessage("Loaded");
		return true;
	}
	
}
