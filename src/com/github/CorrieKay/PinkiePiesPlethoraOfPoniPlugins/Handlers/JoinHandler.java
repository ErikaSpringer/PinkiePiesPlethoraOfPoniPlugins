package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Teleportation.TeleUtils;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSElements;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;

public class JoinHandler extends PoniCommandExecutor implements Listener {
	private final ConfigHandler configHandler;
	private FileConfiguration config = null;
	private final Mane instance;
	
	public JoinHandler(Mane instance, String name){
		super(instance, new String[] {},name);
		configHandler = instance.getConfigHandler();
		this.instance = instance;
	}
	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoin(PSJoinEvent event){
		Player player = event.getPlayer();
		config = configHandler.getPlayerConfig(player);
		if(config != null&&!event.isCancelled()){
			event.setJoinMessage(ChatColor.RED+config.getString("nickname")+ChatColor.AQUA+" has returned to Equestria!");
		}
		
		if(event.isJoining()){
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
			player.setDisplayName(config.getString("nickname"));
			MOTD(player);
			if(config.getBoolean("update")){
				player.getInventory().setContents(InventorySee.toInventory(config));
				player.teleport(TeleUtils.getLocationFromString(config.getStringList("warps.other.offline")));
				player.setGameMode(GameMode.getByValue(config.getInt("gamemode")));
				config.set("update",false);
			}
		}
		Configuration stream = YamlConfiguration.loadConfiguration(instance.getResource("config.yml"));
		config.setDefaults((Configuration) stream);
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
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return false;
	}
	@Override
	public void furtherInitialization() {
		
	}
	@Override
	public PoniCommandExecutor getThis(){
		return this;
	}
}
