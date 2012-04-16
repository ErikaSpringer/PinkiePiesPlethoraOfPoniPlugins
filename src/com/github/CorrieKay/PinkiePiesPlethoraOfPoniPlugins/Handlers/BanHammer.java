package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks.BanConvertTask;

public class BanHammer extends PoniCommandExecutor implements Listener {

	private final Mane instance;
	private final ConfigHandler ch;
	private final String sendToMoon = ChatColor.DARK_RED+" was just sent to the MOOOOOOOOOOOOOOOOOOOOONAAAAAAAHHHHH!!!!";

	public BanHammer(Mane plugin, String name) {
		super(plugin, name);
		instance = plugin;
		ch = plugin.getConfigHandler();
	}
	private void banPlayer(CommandSender sender, String name, String reason){
		Player banner;
		boolean playerOnline = false;
		Player onlinePlayer;
		if(sender instanceof Player){
			banner = (Player)sender;
			reason += " - Banned by: "+banner.getName();
		}
		ArrayList<Player> players = getPlayerOnline(name, true);
		FileConfiguration config;
		List<String> banHistory;
		if(players==null){
			ArrayList<String> offlinePlayers = getPlayerOffline(name);
			if(offlinePlayers == null){
				pinkieSay("I couldn't find him! Lets just create a config and ban him anyways! hehe!",sender);
				config = ch.createOfflinePlayerConfig(name);
			} else if(offlinePlayers.size()>1){
				sender.sendMessage(tooManyMatches(offlinePlayers));
				return;
			} else {
				config = ch.getPlayerConfig(offlinePlayers.get(0));
			}
		} else if(players.size() > 1){
			sender.sendMessage(tooManyMatches(players));
			return;
		} else {
			playerOnline = true;
			config = ch.getPlayerConfig(players.get(0));
		}
		if(config.isString("ban.banReason")){
			banHistory = new ArrayList<String>();
			banHistory.add(config.getString("ban.banReason"));
		}
		banHistory = config.getStringList("ban.banReason");
		banHistory.add(reason);
		if(playerOnline){
			onlinePlayer = Bukkit.getServer().getPlayer(config.getString("name"));
			onlinePlayer.kickPlayer(reason);
		}
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED+name+sendToMoon);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(cmd.getName().equals("ban")){
			if(args.length == 1){
				banPlayer(sender, args[0], null);
				return true;
			} else if(args.length >1){
				String reason = "";
				for(int i = 1; i >args.length;i++){
					reason += args[i]+" ";
				}
				reason = reason.trim();
				banPlayer(sender, args[0], reason);
				return true;
			}
			return true;
		}
		if(cmd.getName().equals("kick")){
			return true;
		}
		if(cmd.getName().equals("warn")){
			
			return true;
		}
		if(cmd.getName().equals("convertbans")){
			Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, new BanConvertTask(instance, sender));
			
			return true;
		}
		else
			return false;
	}
	public boolean isBanned(String name){
		boolean banned = false;
		
		return banned;
	}
	@Override
	public void furtherInitialization() {
	}

	@Override
	public PoniCommandExecutor getThis() {
		return this;
	}

}
