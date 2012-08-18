package de.scorpiacraft.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.features.Spleef;
import de.scorpiacraft.obj.SArena;
import de.scorpiacraft.obj.SPlayer;
import de.scorpiacraft.util.Helper;

public class CmdSpleef implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player)sender;
			SPlayer sp = ScorpiaCraft.getPlayer(p);
			
			if(args[0] != null)
			{
				if(args[0].equalsIgnoreCase("create") && args[1] != null && p.hasPermission("scorpiacraft.spleef.create"))
				{
					if(Spleef.getArena(args[1]) == null)
					{
						Spleef.createArena(p.getName(), args[1]);
						sp.sendMessage("Arena " + args[1] + " erfolgreich erstellt.");
					}
					else
						sp.sendMessage("Eine Arena mit dem Namen " + args[1] + " existiert bereits.");
				}
				else if(args[0].equalsIgnoreCase("set") && p.hasPermission("scorpiacraft.spleef.set"))
				{
					if(args.length >= 3)
					{
						SArena arena = Spleef.getArena(args[1]);
						if(arena != null)
						{
							if(args.length >= 4)
							{
								if( Helper.isNumeric(args[3]))
								{	
									if(args[2].equalsIgnoreCase("start"))
									{
										arena.setStart(Integer.parseInt(args[3]), p.getLocation());
										sp.sendMessage("Anfangspunkt gesetzt");
									}
									else if(args[2].equalsIgnoreCase("end"))
									{
										arena.setEnd(Integer.parseInt(args[3]), p.getLocation());
										sp.sendMessage("Endpunkt gesetzt");
									}
									else if(args[2].equalsIgnoreCase("mode"))
									{
										if(!arena.isPlaying())
										{
											arena.setGameMode(Integer.parseInt(args[3]));
											sp.sendMessage("Spielmodus wurde geändert");
										}
										else
											sp.sendMessage("Es läuft im moment ein Spiel");
									}
								}
								else
									sp.sendMessage("Keine Ebene angegeben");
							}
							else
							{
								if(args[2].equalsIgnoreCase("lobby"))
								{
									arena.setSpectator(p.getLocation());
									sp.sendMessage("Lobby gesetzt.");
								}
							}
						}
						else
							sp.sendMessage("Es existiert keine Arena mit dem Namen " + args[1]);
					}
				}
				else if(args[0].equalsIgnoreCase("join") && p.hasPermission("scorpiacraft.spleef.join"))
				{
					if(sp.getSpleefArena() == null)
					{
						if(args.length > 1)
						{
							SArena arena = Spleef.getArena(args[1]);
							if(arena != null)
							{
								arena.addPlayer(p.getName());
								sp.setSpleefArena(arena);
								sp.sendMessage("Du bist dem Spiel beigetreten");
								p.teleport(arena.getSpectator());
							}
							else
								sp.sendMessage("Es existiert keine Arena mit dem Namen " + args[1]);
						}
						else
						{
							SArena arena = Spleef.getArena("Scorpia");
							if(arena != null)
							{
								arena.addPlayer(p.getName());
								sp.setSpleefArena(arena);
								ScorpiaCraft.broadcast(p.getName() + " ist dem Spiel beigetreten");
								p.teleport(arena.getSpectator());
							}
						}
					}
					else
						sp.sendMessage("Du bist bereits in einem Spiel!");
				}
				else if(args[0].equalsIgnoreCase("leave") && p.hasPermission("scorpiacraft.spleef.leave"))
				{
					SArena arena = sp.getSpleefArena();
					if(arena != null)
					{
						arena.removePlayer(p.getName());
						if(arena.isPlaying())
						{
							if(arena.checkWinner())
							{
								ScorpiaCraft.broadcast(arena.getWinner() + " ist der Sieger");
								arena.endGame();
							}
							sp.setSpleefArena(null);
							sp.loadGameMode();
							sp.loadInventory();
						}
						ScorpiaCraft.broadcast(p.getName() + " ist dem Spiel ausgetreten");
						p.teleport(arena.getSpectator());
					}
					else
						sp.sendMessage("Du bist an keinem Spiel beteiligt");
				}
				else if(args[0].equalsIgnoreCase("start") && p.hasPermission("scorpiacraft.spleef.start"))
				{
					if(args.length > 1)
					{
						SArena arena = Spleef.getArena(args[1]);
						if(arena != null)
						{
							if(!arena.startGame())
								sp.sendMessage("Nicht genügend Spieler");
						}
						else
							sp.sendMessage("Es existiert keine Arena mit dem Namen " + args[1]);
					}
					else
					{
						SArena arena = Spleef.getArena("Scorpia");
						if(arena != null)
						{
							if(!arena.startGame())
								sp.sendMessage("Nicht genügend Spieler");
						}
					}
				}
				else
					sp.sendMessage("Du hast nicht die nötige Berechtigung!");
				
				return true;
			}
		}
		return false;
	}
}
