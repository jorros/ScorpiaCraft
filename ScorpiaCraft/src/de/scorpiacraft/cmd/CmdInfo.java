package de.scorpiacraft.cmd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdInfo implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			
			if(args.length > 0)
			{
				SPlayer ssp = ScorpiaCraft.getPlayer(args[0]);
				
				if(ssp != null)
				{
					DateFormat formatter;
					formatter = new SimpleDateFormat("dd.MM.yy HH:mm z");
					
					sp.sendMessage("Info zu " + ssp.getName());
					String vip = "Nein";
					if(ssp.isVIP())
						vip = "Ja";
					sp.sendMessage("VIP: " + vip);
					sp.sendMessage("Zuletzt online: " +  formatter.format(ssp.getOfflinePlayer().getLastPlayed()));
					sp.sendMessage("Klasse: " + ssp.getSClass().getName());
					sp.sendMessage("Kontostand: " + ssp.getBalance() + "e");
				}
				else
					sp.sendMessage("Spieler existiert nicht");
			}
			else
				sp.sendMessage("Bitte Spieler angeben");
			return true;
		}
		return false;
	}
}
