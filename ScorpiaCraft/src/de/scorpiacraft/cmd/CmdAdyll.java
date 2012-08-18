package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdAdyll implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer((Player)sender);
			sp.sendMessage("Adyll ist zurzeit nicht verfügbar!");
			
			/*SpoutItemStack item = new SpoutItemStack(((Player)sender).getInventory().getItemInHand());
			
			if((item.getMaterial() instanceof Adyll) || item.getAmount() == 0)
			{
				if(sp.removeCoins(Economy.adyllPrice))
				{
					int amount = item.getAmount() + 1;
					sp.getPlayer().setItemInHand(new SpoutItemStack(Economy.adyll, amount));
					sp.sendMessage("Adyll für " + Economy.adyllPrice + "c gekauft");
				}
			}
			*/
		}
		return true;
	}
}
