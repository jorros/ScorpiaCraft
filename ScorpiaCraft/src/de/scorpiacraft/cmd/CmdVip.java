package de.scorpiacraft.cmd;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdVip implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)sender);
			if(sp.isVIP())
			{
				Date date = sp.getExpiration();
				if(date != null)
				{
					DateFormat formatter;
					formatter = new SimpleDateFormat("dd.MM.yy HH:mm z");
					sp.sendMessage(ChatColor.GOLD + "VIP bis " + formatter.format(date));
					return true;
				}
			}
			sp.sendMessage("Kein VIP");
			return true;
		}
		return false;
	}
}
