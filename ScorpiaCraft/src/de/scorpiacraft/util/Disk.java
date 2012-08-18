package de.scorpiacraft.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

public class Disk
{
	
	public static void write(String file, String content) throws IOException
	{
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
		out.write(content);
		out.close();
	}

	public static String read(String file) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
		String ret = new String(new byte[0], "UTF-8");

		String line;
		while ((line = in.readLine()) != null)
		{
			ret += line;
		}

		in.close();
		return ret;
	}

	public static boolean writeCatch(String file, String content)
	{
		try
		{
			write(file, content);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static String readCatch(String file)
	{
		try
		{
			return read(file);
		}
		catch (IOException e)
		{
			return "";
		}
	}
	
	public static void write(String path, Object  content) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		oos.writeObject(content);
		oos.flush();
		oos.close();
	}
	
	public static Object readObject(String file) throws IOException
	{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Object result = null;
		try
		{
			result = ois.readObject();
		}
		catch(ClassNotFoundException e)
		{
			return null;
		}
		ois.close();
		return result;
	}
	
	public static boolean writeCatch(String file, Object content)
	{
		try
		{
			write(file, content);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static Object readObjectCatch(String file)
	{
		try
		{
			return readObject(file);
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	public static boolean exists(String file)
	{
		return (new File(file)).exists();
	}
}
