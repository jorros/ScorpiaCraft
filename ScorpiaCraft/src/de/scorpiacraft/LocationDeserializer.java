package de.scorpiacraft;

import java.lang.reflect.Type;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LocationDeserializer implements JsonDeserializer<Location>
{

	@Override
	public Location deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		JsonArray arr = json.getAsJsonArray();
		if(arr.size() > 4)
			return new Location(Bukkit.getWorld(arr.get(0).getAsJsonPrimitive().getAsString()), arr.get(1).getAsJsonPrimitive().getAsDouble(), arr.get(2).getAsJsonPrimitive().getAsDouble(), arr.get(3).getAsJsonPrimitive().getAsDouble(), arr.get(4).getAsJsonPrimitive().getAsFloat(), 0);
		else
			return new Location(Bukkit.getWorld(arr.get(0).getAsJsonPrimitive().getAsString()), arr.get(1).getAsJsonPrimitive().getAsDouble(), arr.get(2).getAsJsonPrimitive().getAsDouble(), arr.get(3).getAsJsonPrimitive().getAsDouble());
	}
	
}
