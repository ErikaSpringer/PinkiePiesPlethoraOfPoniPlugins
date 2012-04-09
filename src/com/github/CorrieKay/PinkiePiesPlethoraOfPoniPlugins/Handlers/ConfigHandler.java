package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSElements;

/**
 * This is the configuration handler. YES it will be an object. Bite me andrew.
 * 
 * anyways, this configuration handler is to be used for getting PLAYER CONFIGURATIONS ONLY
 * 
 * also, im using it as an object just in case other plugins need to access it.
 * @author Corrie
 *
 */
public class ConfigHandler {
	Mane instance;
	public ConfigHandler(Mane instance){
		this.instance = instance;
	}
	public FileConfiguration getConfig(Player player){ // returns a players configuration file. returns null if it doesnt exist.
		FileConfiguration config;
		File file = new File(instance.getDataFolder()+"\\players\\"+player.getName()+".yml");
		if(file.exists()){
			config = YamlConfiguration.loadConfiguration(file);
		} else {
			config = null;
		}
		return config;
	}
	public FileConfiguration getConfig(String player){ // returns a configuration by string (player name) used for grabbing offline player configs
		FileConfiguration config;
		File file = new File(instance.getDataFolder()+"\\players\\"+player+".yml");
		if(file.exists()){
			config = YamlConfiguration.loadConfiguration(file);
		} else {
			config = null;
		}
		return config;
	}
	public FileConfiguration getElement(PSElements element){
			return YamlConfiguration.loadConfiguration(new File(instance.getDataFolder()+"\\"+element.name().toLowerCase()+".yml"));
	}
	
	public void saveElement(FileConfiguration config, PSElements element){
		try {
			config.save(new File(instance.getDataFolder()+"\\"+element.toString().toLowerCase()+".yml"));
		} catch (IOException e) {
			Bukkit.getLogger().severe("[PonySentials] could not save the configuration file");
		}
	}
	
	public void saveConfig(FileConfiguration config){ // saves a player configuration file.
		File file = new File(instance.getDataFolder()+"\\players\\"+config.getString("name")+".yml");
		try {
			config.save(file);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[PonySentials] could not save the configuration file to "+file);
		}
	}
	public boolean createNewPlayerConfig(Player player){//creates a new configuration file for a player
		boolean hasPlayed = false;
		File file = new File(instance.getDataFolder()+"\\players\\"+player.getName()+".yml");
		try {
			file.createNewFile();
		} catch (IOException e) {}
		FileConfiguration config = getConfig(player);
		List<String> ipAddys = new ArrayList<String>();
		config.set("name",player.getName());
		config.set("online", true);
		config.set("muted",false);
		config.set("god",false);
		config.set("afk",false);
		config.set("invisible",false);
		config.set("viewingInventory", false);
		config.set("nickname", player.getName());
		ipAddys.add(player.getAddress().toString().substring(1,player.getAddress().toString().indexOf(":")));
		config.set("ipAddress",ipAddys);
		config.set("ban.banned", false);
		config.createSection("ban.banReason");
		if (!player.hasPlayedBefore()) {
			Bukkit.getLogger().info("they havnt played before!");
			config.set("firstLogon", getSystemDate());
		} else {
			Bukkit.getLogger().info("they have played before!");
			config.set("firstLogon",getDateFromLong(player.getFirstPlayed()));
			hasPlayed = true;
		}
		config.set("lastLogon",getSystemDate());
		config.set("lastLogout", "N/A");
		config.createSection("inventory");
		saveConfig(config);
		return hasPlayed;
	}	
	public String getSystemDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	public String getDateFromLong(long time){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(time);
		return dateFormat.format(date);
	}
}
