package de.scorpiacraft;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.scorpiacraft.obj.SItem;

public class SItemSerializer implements JsonSerializer<SItem>
{
	@Override
	public JsonElement serialize(SItem src, Type typeOfSrc,
			JsonSerializationContext context)
	{
		JsonObject arr = new JsonObject();
		arr.add("name", new JsonPrimitive(src.getName()));
		arr.add("price", new JsonPrimitive(src.getPrice()));
		arr.add("amount", new JsonPrimitive(src.getAmount()));
		return arr;
	}
	
}