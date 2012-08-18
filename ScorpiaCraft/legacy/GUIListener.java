package de.scorpiacraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenCloseEvent;
import org.getspout.spoutapi.event.screen.TextFieldChangeEvent;
import org.getspout.spoutapi.gui.ListWidget;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.inventory.SpoutItemStack;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SButton;
import de.scorpiacraft.obj.SItem;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.obj.STextField;

public class GUIListener implements Listener
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onButtonClick(final ButtonClickEvent event)
	{
		if(event.isCancelled())
			return;
		
		if(event.getButton() instanceof SButton)
		{			
			final String name = ((SButton)event.getButton()).getName();
			
			SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
			
			if(name.equals("buy"))
			{
				for(Widget w : event.getPlayer().getMainScreen().getActivePopup().getAttachedWidgets())
				{
					if(w instanceof ListWidget)
					{
						ListWidget l = (ListWidget)w;
						
						if(l.getSelectedItem() == null)
							return;					
						
						SItem item = (SItem)l.getSelectedItem();

						int balance = sp.getBalance();
						
						if(item.getShop().getOwner().equals(sp.getName()) && (item.getAmount() > 0 || item.getAmount() == -1)) // Wenn Besitzer
						{
							event.getPlayer().getInventory().addItem(item.getItemStack());
							if((item.getAmount() > 0))
								item.setAmount(item.getAmount() - 1);
						}
						else if(balance >= item.getPrice(sp) && (item.getAmount() > 0 || item.getAmount() == -1)) // Wenn Käufer
						{
							sp.removeCoins(item.getPrice(sp));
							
							event.getPlayer().getInventory().addItem(item.getItemStack());
							if(item.getAmount() > 0)
							{
								item.setAmount(item.getAmount() - 1);
								ScorpiaCraft.getPlayer(item.getShop().getOwner()).addCoins(item.getPrice(sp));
							}
						}
					}
				}
			}
			else if(name.equals("buystack"))
			{
				for(Widget w : event.getPlayer().getMainScreen().getActivePopup().getAttachedWidgets())
				{
					if(w instanceof ListWidget)
					{
						ListWidget l = (ListWidget)w;
						
						if(l.getSelectedItem() == null)
							return;
						
						SItem item = (SItem)l.getSelectedItem();
						
						int balance = sp.getBalance();
						
						// Wenn eigener Besitzer -> nichts bezahlen
						if(item.getShop().getOwner().equals(sp.getName())) // Wenn Besitzer
						{
							if(item.getAmount() >= 64)
							{
								item.setAmount(item.getAmount() - 64);
								SpoutItemStack its = item.getItemStack();
								its.setAmount(64);
								event.getPlayer().getInventory().addItem(its);
							}
							else if(item.getAmount() > 0)
							{
								SpoutItemStack its = item.getItemStack();
								its.setAmount(item.getAmount());
								event.getPlayer().getInventory().addItem(its);
								item.setAmount(0);
							}
							else if(item.getAmount() == -1) // Unendlich
							{
								SpoutItemStack its = item.getItemStack();
								its.setAmount(64);
								event.getPlayer().getInventory().addItem(its);
							}
						}
						else // Wenn Käufer
						{
							if(balance >= item.getPrice(sp)*64 && (item.getAmount() >= 64 || item.getAmount() == -1))
							{
								sp.removeCoins(item.getPrice(sp) * 64);
								
								SpoutItemStack its = item.getItemStack();
								its.setAmount(64);
								event.getPlayer().getInventory().addItem(its);
								if(item.getAmount() > 0)
									item.setAmount(item.getAmount() - 64);
								
								try
								{
									if(item.getAmount() > 0)
										ScorpiaCraft.getPlayer(item.getShop().getOwner()).addCoins(item.getPrice(sp)*64);
								}
								catch(Exception ex)
								{
									
								}
							}
							else if(item.getAmount() >= 64 || item.getAmount() == -1) // Wenn genug Items, aber nicht genug Geld da ist
							{
								int amount = balance / item.getPrice(sp);
								if(amount > 0)
								{
									sp.removeCoins(item.getPrice(sp) * amount);
									
									SpoutItemStack its = item.getItemStack();
									its.setAmount(amount);
									event.getPlayer().getInventory().addItem(its);
									if(item.getAmount() > 0)
										item.setAmount(item.getAmount() - amount);
									
									if(item.getAmount() > 0)
										ScorpiaCraft.getPlayer(item.getShop().getOwner()).addCoins(item.getPrice(sp)*amount);
								}
							}
							else // Wenn entweder nicht genug Geld für ganzen Stack oder kein ganzer Stack Items verfügbar
							{
								int posamount = balance / item.getPrice(sp);
								if(posamount >= item.getAmount()) // Wenn man mehr Geld hat als Items verfügbar sind
								{
									sp.removeCoins(item.getPrice(sp) * item.getAmount());
									SpoutItemStack its = item.getItemStack();
									its.setAmount(item.getAmount());
									event.getPlayer().getInventory().addItem(its);
									item.setAmount(0);
									
									if(item.getAmount() > 0)
										ScorpiaCraft.getPlayer(item.getShop().getOwner()).addCoins(item.getPrice(sp)*item.getAmount());
								}
								else // Wenn man weniger Geld als Items hat
								{
									int amount = balance / item.getPrice(sp);
									if(amount > 0)
									{
										sp.removeCoins(item.getPrice(sp) * amount);
										SpoutItemStack its = item.getItemStack();
										its.setAmount(amount);
										event.getPlayer().getInventory().addItem(its);
										item.setAmount(item.getAmount() - amount);
										
										if(item.getAmount() > 0)
											ScorpiaCraft.getPlayer(item.getShop().getOwner()).addCoins(item.getPrice(sp)*amount);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onScreenClose(final ScreenCloseEvent event)
	{
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onTextFieldChange(TextFieldChangeEvent event)
	{
		if(event.getTextField() instanceof STextField)
		{
			final String type = ((STextField)event.getTextField()).getName();
			
			if(type.equals("arena_name"))
			{
				SPlayer sp = ScorpiaCraft.getPlayer(event.getPlayer());
				sp.getSpleefArena().setName(event.getNewText());
			}
		}
	}
}
