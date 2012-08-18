package de.scorpiacraft;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.scorpiacraft.obj.SItem;

public class SItemDeserializer implements JsonDeserializer<SItem>
{

	@Override
	public SItem deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject arr = json.getAsJsonObject();
		return new SItem(arr.get("name").getAsString(), arr.get("amount").getAsInt(), arr.get("price").getAsInt());
	}
	
}