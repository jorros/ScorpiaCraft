package de.scorpiacraft.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import de.scorpiacraft.ScorpiaCraft;

public class Lib
{
	private static URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
	
	public static void loadLib(String filename)
	{
		File file = getFile(filename);
		if(!file.exists())
		{
			ScorpiaCraft.log("Bibliothek " + filename + " nicht gefunden");
		}
		
		URL url;
		try
		{
			url = file.toURI().toURL();
		} 
		catch (MalformedURLException e)
		{
			ScorpiaCraft.log("Bibliothek " + filename + " fehlerhaft #1");
			return;
		}
		
		for (URL otherUrl : sysloader.getURLs())
		{
			if(otherUrl.sameFile(url))
				return;
		}

		try
		{
			Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{ URL.class });
			addURLMethod.setAccessible(true);
			addURLMethod.invoke(sysloader, new Object[]{ url });
			return;
		}
		catch (Exception e)
		{
			ScorpiaCraft.log("Bibliothek " + filename + " fehlerhaft #1");
			return;
		}
	}
	
	private static File getFile(String filename)
	{
		return new File("./lib/"+filename);
	}
}
