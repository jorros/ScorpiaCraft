package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class CmdShop implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			SPlayer sp = ScorpiaCraft.getPlayer(((Player)sender));
			/*if(sp.getSelectedShop() == null)
			{
				sp.sendMessage("Wähle oder platziere einen Shop");
				return true;
			}
			
			SShop shop = Economy.getShop(sp.getSelectedShop());
			
			// Ändere Namen
			if(args.length > 0)
			{
				if(Helper.isNumeric(args[0]))
				{
					ItemStack item = new ItemStack(((Player)sender).getItemInHand());
					if(item != null)
					{
						SItem it = shop.addItem(item, Integer.parseInt(args[0]));
						if(sp.getRank() == Rank.Admin)
							it.setAmount();
						else
							((Player)sender).setItemInHand(null);
					}
				}
				else
				{
					if(args[0].equalsIgnoreCase("remove"))
					{
						ItemStack item = new ItemStack(((Player)sender).getItemInHand());
						shop.removeItem(item.getTypeId());
					}
					else
					{
						shop.setName(args[0]);
						sp.sendMessage("Shop wurde in " + args[0] + " umbenannt");
					}
				}
				String label = "Shop";
				if(shop.getName() != null && !shop.getName().equals(""))
					label = shop.getName() + " (Shop)";
				
				String products = "";
				for(SItem it : shop.getItems())
				{
					products += it.getName() + ", ";
				}
				if(!products.equals(""))
					products = products.substring(0, products.length()-2);
				
				Dynmap.setMarkerDescription("shop", shop.getLocation(), label, "<div class=\"infowindow\"><span style=\"font-size:120%;\"> " + label + "</span><br /> Angebotene Produkte: <br /> <span style=\"font-weight:bold;\">" + products + "</span></div>");
				
				return true;
			}
			
			return false;
			*/
			sp.sendMessage("Shop ist zurzeit nicht verfügbar!");
		}
		return false;
	}
}
