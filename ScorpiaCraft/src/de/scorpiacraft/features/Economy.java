package de.scorpiacraft.features;

public class Economy
{
	//private static Map<String, SShop> shops = new HashMap<String, SShop>();
	
	public static int payedIn = 0;
	public static int todayDate = 0;
	public static int adyllPrice = 200;
	
	public static void load(boolean reload)
	{
		/*if(Disk.exists(ScorpiaCraft.p.getDataFolder().getPath() + "/shops.json"))
		{
			shops = ScorpiaCraft.gson.fromJson(Disk.readCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/shops.json"), new TypeToken<Map<String, SShop>>() {}.getType());
		}
		else
		{
			ScorpiaCraft.log("CreateShopsFile");
			ScorpiaCraft.save("economy");
		}*/
	}
	
	public static void save()
	{
		//Disk.writeCatch(ScorpiaCraft.p.getDataFolder().getPath() + "/shops.json", ScorpiaCraft.gson.toJson(shops, new TypeToken<Map<String, SShop>>() {}.getType()));
	}
}
