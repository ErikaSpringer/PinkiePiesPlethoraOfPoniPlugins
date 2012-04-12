package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSElements;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;

public class JoinHandler implements Listener {
	private final ConfigHandler configHandler;
	FileConfiguration config = null;
	
	public JoinHandler(Mane instance){
		configHandler = instance.getConfigHandler();
	}
	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoin(PSJoinEvent event){
		Player player = event.getPlayer();
		config = configHandler.getPlayerConfig(player);
		if(config != null&&!event.isCancelled()){
			event.setJoinMessage(ChatColor.RED+config.getString("nickname")+ChatColor.AQUA+" has returned to Equestria!");
		}
		
		if(event.isJoining()){
			config.addDefault("name", "n/a");
			config.addDefault("online", false);
			config.addDefault("muted", false);
			config.addDefault("god", false);
			config.addDefault("afk", false);
			config.addDefault("invisible", false);
			config.addDefault("viewingInventory", false);
			config.addDefault("nickname", "n/a");
			ArrayList<String> list = new ArrayList<String>();
			list.add("eq");
			config.addDefault("chatChannel", "eq");
			config.addDefault("listenChannels", list);
			config.addDefault("ipAddress", "");
			config.addDefault("horn.left", "");
			config.addDefault("horn.right", "");
			config.addDefault("horn.on", false);
			config.addDefault("ban.banned", false);
			config.addDefault("ban.banReason", "");
			config.addDefault("warnings.warning1", "");
			config.addDefault("warnings.warning2", "");
			config.addDefault("warnings.warning3", "");
			config.addDefault("firstLogon", "n/a");
			config.addDefault("lastLogon", "n/a");
			config.addDefault("lastLogout", "n/a");
			config.addDefault("warps.other.back", "");
			config.addDefault("warps.other.home", "");
			config.addDefault("warps.other.offline", "");
			config.addDefault("inventory", "");
			if(!player.hasPlayedBefore()||(player.hasPlayedBefore()&&config == null)){
			Bukkit.getLogger().info("New Player detected: creating configuration");
			config = configHandler.createNewPlayerConfig(player);
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
		config.options().copyDefaults(true);
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
