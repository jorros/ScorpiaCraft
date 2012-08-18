package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Classes;
import de.scorpiacraft.obj.SClass;
import de.scorpiacraft.obj.SPlayer;

public class CmdClass implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			
			if(args.length > 1)
			{
				SClass sc = Classes.getSClass(args[0]);
				if(sc != null)
				{
					if(args[1].equalsIgnoreCase("choice") && (p.hasPermission("scorpiacraft.class.choice")))
					{
						sc.setChoiceLocation(p.getLocation().getBlock().getLocation());
						sp.sendMessage("Wahlplatz für " + sc.getName() + " gesetzt.");
					}
					else
						sp.sendMessage("Du hast nicht die nötigen Rechte.");
				}
				else
					sp.sendMessage("Klasse existiert nicht");
				
				return true;
			}
		}
		return false;
	}
}
