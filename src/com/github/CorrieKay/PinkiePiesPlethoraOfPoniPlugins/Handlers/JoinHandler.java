package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSElements;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;

public class JoinHandler implements Listener {
	private final ConfigHandler configHandler;
	
	public JoinHandler(Mane instance){
		configHandler = instance.getConfigHandler();
	}
	@EventHandler 
	public void onJoin(PSJoinEvent event){
		Player player = event.getPlayer();
		FileConfiguration config = configHandler.getPlayerConfig(player);
		if(config != null&&!event.isSilent()){
			event.setJoinMessage(ChatColor.RED+config.getString("nickname")+ChatColor.AQUA+" has returned to Equestria!");
		}
		if(event.isJoining()){
			if(!player.hasPlayedBefore()||(player.hasPlayedBefore()&&config == null)){
			Bukkit.getLogger().info("New Player detected: creating configuration");
			config = configHandler.createNewPlayerConfig(player);
			if (!event.isSilent()) {
				event.setJoinMessage(ChatColor.AQUA + "Please welcome "+ ChatColor.RED + player.getName() + ChatColor.AQUA+ " to Equestria!");
			}
			List<String> ip = config.getStringList("ipAddress");
			if (!ip.contains(player.getAddress().toString().substring(1,player.getAddress().toString().indexOf(":")))){
				ip.add(player.getAddress().toString().substring(1,player.getAddress().toString().indexOf(":")));
			}
			config.set("ipAddress", ip);
			config.set("lastLogon", getSystemDate());
		}
			if(config.getBoolean("viewingInventory")){
				//TODO: code for invsee here
			} else {
				//TODO: more code for invsee
			}
			player.setDisplayName(config.getString("nickname"));
			MOTD(player);
		}
		config.set("online",true);
		configHandler.savePlayerConfig(config);
	}
	private void MOTD(Player player){
		for(String string : configHandler.getServerConfig(PSElements.MOTD).getStringList("motd")){
			player.sendMessage(format(string,player));
		}
	}
	private String getSystemDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	private static String format(String string, Player player) {
	    String s = string;
	    for (ChatColor color : ChatColor.values()) {
	        s = s.replaceAll("(?i)<" + color.name() + ">", "" + color);
	    }
	    s = s.replaceAll("<player>", player.getDisplayName());
	    return s;
	}
}
