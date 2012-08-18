package de.scorpiacraft.lang;

import java.util.HashMap;

public class Lang
{
	private HashMap<String, Base> languages = new HashMap<String, Base>();
	private String lang = "german";
	
	public Lang()
	{
		languages.put("german", new German());
	}
	
	public void setLanguage(String lang)
	{
		if(languages.containsKey(lang))
			this.lang = lang;
	}
	
	public String get(String key)
	{
		return this.languages.get(this.lang).get(key);
	}
}
