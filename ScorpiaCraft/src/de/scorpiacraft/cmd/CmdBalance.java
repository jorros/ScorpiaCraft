package de.scorpiacraft.cmd;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Helper;

public class CmdBalance implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			
			if(args.length < 1)
				sp.sendMessage("Dein Kontostand beträgt: " + sp.getBalance() + "e");
			else
			{
				if(Helper.isNumeric(args[0]) && p.hasPermission("scorpiacraft.balance.withdraw"))
				{
					int amount = Integer.parseInt(args[0]);
					if(sp.getBalance() < amount)
						amount = sp.getBalance();
					
					if(amount == 0)
						sp.sendMessage("Du hast kein Geld zum Abheben.");
					else
					{
						sp.removeEmerald(amount);
						ItemStack emerald = new ItemStack(388, amount);
						int retamount = 0;
						HashMap<Integer, ItemStack> ret = p.getInventory().addItem(emerald);
						if(!ret.isEmpty())
						{
							for(ItemStack it : ret.values())
							{
								if(it.getTypeId() == 388)
								{
									retamount += it.getAmount();
								}
							}
						}
						if(retamount > 0)
						{
							sp.addEmerald(retamount);
							sp.sendMessage(retamount + "e wurden zurückgewiesen, weil dein Inventar überfüllt ist");
						}
						else
							sp.sendMessage(amount + "e wurden ausgezahlt");
					}
				}
				else if(args[0].equalsIgnoreCase("deposit") && p.hasPermission("scorpiacraft.balance.deposit"))
				{
					ItemStack hand = p.getItemInHand();
					if(hand.getTypeId() == 388)
					{
						sp.addEmerald(hand.getAmount());
						sp.sendMessage(hand.getAmount() + "e wurden eingezahlt");
						p.setItemInHand(null);
					}
					else
						sp.sendMessage("Keine Emeralds auf der Hand");
				}
				else
					sp.sendMessage("Nicht die nötige Berechtigung oder falsche Parameter");
			}
			//sp.sendMessage("Wert von Adyll beträgt: " + Economy.adyllPrice + "c");
		}
		return true;
	}
}
