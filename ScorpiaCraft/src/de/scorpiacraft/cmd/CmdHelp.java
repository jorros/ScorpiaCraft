package de.scorpiacraft.cmd;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHelp implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player)sender;
		
		ArrayList<String> text = new ArrayList<String>();
		
		text.add("--------- " + ChatColor.GOLD + "[ScorpiaCraft Hilfe]" + ChatColor.WHITE + " ---------");
		//text.add(ChatColor.LIGHT_PURPLE + "/adyll: " + need + "Kauft einen Barren Adyll für den aktuellen Kurspreis" + nee);
		
		if(p.hasPermission("scorpiacraft.afk"))
			text.add(ChatColor.LIGHT_PURPLE + "/afk: " + ChatColor.WHITE + "Setzt dich in den AFK Zustand");
		
		if(p.hasPermission("scorpiacraft.back"))
			text.add(ChatColor.LIGHT_PURPLE + "/back: " + ChatColor.WHITE + "Teleportiert dich zur letzten bekannten Position");
		
		if(p.hasPermission("scorpiacraft.balance"))
			text.add(ChatColor.LIGHT_PURPLE + "/balance: " + ChatColor.WHITE + "Zeigt deinen aktuellen Kontostand");
		
		if(p.hasPermission("scorpiacraft.buy"))
			text.add(ChatColor.LIGHT_PURPLE + "/buy: " + ChatColor.WHITE + "Kauft das Grundstück, auf dem du dich befindest");
		
		if(p.hasPermission("scorpiacraft.home"))
			text.add(ChatColor.LIGHT_PURPLE + "/home [set]: " + ChatColor.WHITE + "Teleportiert dich zu deinem Homepunkt oder setzt es für 1e");
		
		if(p.hasPermission("scorpiacraft.info"))
			text.add(ChatColor.LIGHT_PURPLE + "/info [player]: " + ChatColor.WHITE + "Ruft Informationen zum Spieler ab");
		
		if(p.hasPermission("scorpiacraft.plot"))
			text.add(ChatColor.LIGHT_PURPLE + "/plot [None/Server/Sale/Npc]: " + ChatColor.WHITE + "Setzt das Grundstück");
		
		if(p.hasPermission("scorpiacraft.instinct"))
			text.add(ChatColor.LIGHT_PURPLE + "/instinct: " + ChatColor.WHITE + "Aktiviert oder deaktiviert deinen klassenspezifischen Instinkt");
		
		//text.add(ChatColor.LIGHT_PURPLE + "/shop [preis oder name für shop]: " + needSp + "Setzt den Artikel in der Hand für den gegebenen Preis" + needSpout);
		
		if(p.hasPermission("scorpiacraft.vipstatus"))
			text.add(ChatColor.LIGHT_PURPLE + "/vip: " + ChatColor.WHITE + "Zeigt deinen VIP Status an");
		
		sender.sendMessage((String[])text.toArray(new String[text.size()]));
		return true;
	}
}
