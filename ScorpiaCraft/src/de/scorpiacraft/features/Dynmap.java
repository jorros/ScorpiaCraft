package de.scorpiacraft.features;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import de.scorpiacraft.ScorpiaCraft;

public class Dynmap
{
	public static DynmapAPI dynmap = null;
	public static MarkerAPI markerAPI = null;
	public static MarkerSet markerSet = null;
	public static MarkerSet defaultSet = null;
	
	public static Plugin pDynmap;
	
	public static void load(boolean reload)
	{
		pDynmap = ScorpiaCraft.p.getServer().getPluginManager().getPlugin("dynmap");
		if(pDynmap == null)
		{
			ScorpiaCraft.log("DynmapNotFound");
		}
		else if(pDynmap.isEnabled())
		{
			dynmap = (DynmapAPI)pDynmap;
		
			markerAPI = dynmap.getMarkerAPI();
			if(markerAPI == null)
			{
				ScorpiaCraft.log("MarkerAPIError");
				return;
			}
			
			markerSet = markerAPI.getMarkerSet("scorpiacraft.markerset");
			if(markerSet == null)
				markerSet = markerAPI.createMarkerSet("scorpiacraft.markerset", "Nationen", null, false);
			else
				markerSet.setMarkerSetLabel("Nationen");
			if(markerSet == null)
			{
				ScorpiaCraft.log("MarkerSetError");
				return;
			}
			
			defaultSet = markerAPI.getMarkerSet("markers");
			
			markerSet.setLayerPriority(10);
		}
	}
	
	public static void save()
	{
		if(Dynmap.markerSet != null)
		{
			Dynmap.markerSet.deleteMarkerSet();
			Dynmap.markerSet = null;
		}
	}
	
	public static void setMarker(String type, String label, Location loc)
	{
		setMarker(type, label, "", loc);
	}
	
	public static void setMarker(String type, String label, String desc, Location loc)
	{
		MarkerIcon icon = markerAPI.getMarkerIcon("pin");
		String id = type + "_" + loc.getX() + loc.getZ();
		if(type.equalsIgnoreCase("flag"))
			icon = markerAPI.getMarkerIcon("tower");
		else if(type.equalsIgnoreCase("throne"))
			icon = markerAPI.getMarkerIcon("star");
		else if(type.equalsIgnoreCase("bank"))
			icon = markerAPI.getMarkerIcon("bank");
		else if(type.equalsIgnoreCase("arena"))
			icon = markerAPI.getMarkerIcon("theater");
		
		Marker mark = defaultSet.findMarker(id);
		if(mark == null)
		{
			mark = defaultSet.createMarker(id, label, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), icon, true);
		}
		else
		{
			mark.setLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
            mark.setLabel(desc);
            mark.setMarkerIcon(icon);
		}
		mark.setDescription(desc);
	}
	
	public static void removeMarker(String type, Location loc)
	{
		String id = type + "_" + loc.getX() + loc.getZ();
		Marker mark = defaultSet.findMarker(id);
		if(mark != null)
			mark.deleteMarker();
	}
	
	public static void setMarkerDescription(String type, Location loc, String label, String desc)
	{
		String id = type + "_" + loc.getX() + loc.getZ();
		Marker mark = defaultSet.findMarker(id);
		if(mark != null)
		{
			mark.setLabel(label);
			mark.setDescription(desc);
		}
	}
}
