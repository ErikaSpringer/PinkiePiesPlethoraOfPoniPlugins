package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSQuitEvent;

public class QuitHandler implements Listener {

	private final ConfigHandler ch;
	
	public QuitHandler(Mane instance){
		ch = instance.getConfigHandler();
	}
	
	@EventHandler
	public void onQuit(PSQuitEvent event){
		FileConfiguration config = ch.getPlayerConfig(event.getPlayer());
		config.set("lastLogout", getSystemDate());
		config.set("online", false);
		if(config.getBoolean("viewingInventory")){
			//TODO: invsee stuff
		} else {
			//TODO: more invsee stuff
		}
		//TODO: final invsee stuff
		event.setQuitMessage(null);
		ch.savePlayerConfig(config);
	}
	public String getSystemDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}
