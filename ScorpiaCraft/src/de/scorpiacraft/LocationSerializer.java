package de.scorpiacraft;

import java.lang.reflect.Type;

import org.bukkit.Location;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocationSerializer implements JsonSerializer<Location>
{
	@Override
	public JsonElement serialize(Location src, Type typeOfSrc,
			JsonSerializationContext context)
	{
		JsonArray arr = new JsonArray();
		arr.add(new JsonPrimitive(src.getWorld().getName()));
		arr.add(new JsonPrimitive(src.getX()));
		arr.add(new JsonPrimitive(src.getY()));
		arr.add(new JsonPrimitive(src.getZ()));
		arr.add(new JsonPrimitive((int)src.getYaw()));
		return arr;
	}
	
}
