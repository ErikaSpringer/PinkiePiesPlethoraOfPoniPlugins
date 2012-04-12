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

@SuppressWarnings("unused")
public class ConfigHandler {
	Mane instance;
	public ConfigHandler(Mane instance){
		this.instance = instance;
	}
	/**
	 * Player configuration functions
	 */
	public FileConfiguration getPlayerConfig(Player player){
		return getPlayerConfig(player.getName());
	}
	public FileConfiguration getPlayerConfig(String name){
		FileConfiguration config = null;
		File file = new File(instance.getDataFolder()+File.separator+"players"+File.separator+name+".yml");
		if(file.exists()){
			config = YamlConfiguration.loadConfiguration(file);
		}
		return config;
	}
	public void savePlayerConfig(FileConfiguration config){
		File file = new File(instance.getDataFolder()+File.separator+"players"+File.separator+config.getString("name")+".yml");
		try {
			config.save(file);
			return;
		} catch (IOException e) {
			Bukkit.getLogger().severe("[PPPOPP] IO EXCEPTION: unable to save Player Configuration!");
			e.printStackTrace();
			return;
		}
	}
	/**
	 * Server configuration functions
	 */
	public FileConfiguration getServerConfig(PSElements element){
		FileConfiguration config = null;
		File file = new File(instance.getDataFolder()+File.separator+element.name().toLowerCase()+".yml");
		if(file.exists()){
			config = YamlConfiguration.loadConfiguration(file);
		}
		return config;
	}
	public void saveServerConfig(FileConfiguration config, PSElements element){
		File file = new File(instance.getDataFolder()+File.separator+element.name().toLowerCase()+".yml");
		try {
			config.save(file);
		} catch (IOException e) {
			Bukkit.getLogger().severe("[PPPOPP] IO EXCEPTION: unable to save Server Configuration!");
			e.printStackTrace();
		}
	}
	/**
	 * Create new Player Config
	 */
	public FileConfiguration createNewPlayerConfig(Player player){
		File file = new File(instance.getDataFolder()+File.separator+"players"+File.separator+player.getName()+".yml");
		File folders = new File(instance.getDataFolder()+File.separator+"players");
		if(!folders.exists()){
			folders.mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			Bukkit.getLogger().severe("[PPPOPP] IO EXCEPTION: unable to create Player Configuration!");
			e.printStackTrace();
			return null;
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(instance.getResource("config.yml"));
		config.set("name", player.getName());
		config.set("nickname", player.getDisplayName());
        if (!player.hasPlayedBefore()) {
            config.set("firstLogon", getSystemDate());
        } else {
            config.set("firstLogon",getDateFromLong(player.getFirstPlayed()));
        }
        ArrayList<String> ip = new ArrayList<String>();
        ip.add(player.getAddress().toString().substring(1,player.getAddress().toString().indexOf(":")));
        config.set("ipAddress", ip);
		savePlayerConfig(config);
		return config;
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