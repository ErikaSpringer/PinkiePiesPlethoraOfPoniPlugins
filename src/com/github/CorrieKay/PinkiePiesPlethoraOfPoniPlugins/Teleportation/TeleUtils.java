package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Teleportation;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class TeleUtils {

	/**
	 * make sure to save the configuration afterwards
	 * @param location
	 * @param path
	 * @param config
	 */
	public static void setWarp(Location location, String path, FileConfiguration config){
		config.set(path, getLocationString(location));
	}
	/**
	 * returns a location from a string list made by getLocationFromString
	 * 
	 * @param path
	 * @param config
	 * @return a location that you can use to warp to
	 */
	public static Location getWarp(String path, FileConfiguration config){
		Location location = getLocationFromString(config.getStringList(path));
		return location;
	}
	public static List<String> getLocationString(Location location){
		ArrayList<String> stringLocation = new ArrayList<String>();
		stringLocation.add(location.getWorld().getName());
		stringLocation.add(location.getX()+"");
		stringLocation.add(location.getY()+"");
		stringLocation.add(location.getZ()+"");
		stringLocation.add(location.getPitch()+"");
		stringLocation.add(location.getYaw()+"");
		return stringLocation;
	}
	public static Location getLocationFromString(List<String> string){
		Location location = new Location(Bukkit.getServer().getWorld(string.get(0)), Double.parseDouble(string.get(1)), Double.parseDouble(string.get(2)), Double.parseDouble(string.get(3)), Long.parseLong(string.get(4)),Long.parseLong(string.get(5)));
		return location;
	}
}
