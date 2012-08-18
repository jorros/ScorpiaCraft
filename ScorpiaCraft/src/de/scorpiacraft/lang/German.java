package de.scorpiacraft.lang;

import java.util.HashMap;

public class German extends Base
{
	public HashMap<String, String> lang = new HashMap<String, String>();
	
	public German()
	{
		lang.put("CreatePlayerFile", "Spielerdatei wird angelegt");
		lang.put("CreateDiplomacyFile", "Diplomatiedatei wird angelegt");
		lang.put("CreateNationsFile", "Nationendatei wird angelegt");
		lang.put("Disabled", "Deaktiviert");
		lang.put("ConfigLoaded", "Einstellungen geladen");
		lang.put("Saved", "Erfolgreich gespeichert");
		lang.put("Loaded", "Erfolgreich geladen");
		lang.put("Enemy", "Feind");
		lang.put("Peace", "Frieden");
		lang.put("Alliance", "Allianz");
		lang.put("Neutral", "Neutral");
		lang.put("FreeRegion", "Freies Gebiet");
		lang.put("NotEnoughMoney", "Nicht genügend Geld vorhanden");
		lang.put("NoBuyRights", "Das Gebiet ist nicht von dir kaufbar");
		lang.put("NoMemberOfNation", "Du gehörst keiner Nation an");
		lang.put("NoRight", "Du hast keine Berechtigung");
		lang.put("EnemyTerritory", "Du befindest dich auf feindlichem Gebiet");
		lang.put("FarAway", "Du bist zu weit ausserhalb des Reiches");
		lang.put("NoCoinsInHand", "Du hälst keine Coins in der Hand");
		lang.put("SpawnMoved", "Spawnpunkt wurde versetzt");
		lang.put("HomeMoved", "Heimpunkt wurde versetzt");
		lang.put("NotInCapital", "Du befindest dich nicht in der Hauptstadt");
		lang.put("NotInPlot", "Du befindest dich nicht auf deinem Grundstück");
		lang.put("NotInNation", "Du befindest dich nicht im Gebiet des Reiches");
	}

	@Override
	public String get(String key)
	{
		if(lang.containsKey(key))
			return lang.get(key);
		else
			return key;
	}
}
