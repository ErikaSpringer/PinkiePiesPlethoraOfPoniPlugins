package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.BanHammer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.ConfigHandler;
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
			reason += " - Banned by: "+banner.getName()+" "+getSystemDate();
		} else {
			reason += " - Banned by: Console "+getSystemDate();
		}
		ArrayList<Player> players = getPlayerOnline(name, true);
		FileConfiguration config;
		List<String> banHistory;
		if(players==null){
			ArrayList<String> offlinePlayers = getPlayerOffline(name);
			if(offlinePlayers == null){
				pinkieSay("I couldn't find him! Lets just create a config and ban him anyways!",sender);
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
			name = players.get(0).getDisplayName();
			config = ch.getPlayerConfig(players.get(0));
		}
		if(config.isString("ban.banReason")){
			banHistory = new ArrayList<String>();
			banHistory.add(config.getString("ban.banReason"));
			config.set("ban.banReason", banHistory);
		}
		if(config.getBoolean("ban.banned")){
			pinkieSay("That user is banned silly! Try again!",sender);
			return;
		}
		banHistory = config.getStringList("ban.banReason");
		banHistory.add(reason);
		if(playerOnline){
			onlinePlayer = Bukkit.getServer().getPlayer(config.getString("name"));
			onlinePlayer.kickPlayer("You have been banned! Visit https://fogelholk.se/banlist/ to appeal!");
		}
		config.set("ban.banReason", banHistory);
		config.set("ban.banned", true);
		ch.savePlayerConfig(config);
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED+config.getString("nickname")+sendToMoon);
		return;
	}
	private void unbanPlayer(CommandSender sender, String name, String reason){
		String unbanner = "Console";
		if(sender instanceof Player){
			Player player = (Player)sender;
			unbanner = player.getName();
		}
		ArrayList<String> offlinePlayers = getPlayerOffline(name);
		if(offlinePlayers == null){
			pinkieSay(playerNotOnline(), sender);
			return;
		} else if (offlinePlayers.size() >1){
			pinkieSay(tooManyMatches(offlinePlayers),sender);
			return;
		}
		name = offlinePlayers.get(0);
		if(!isBanned(name)){
			pinkieSay("Silly willy, that user isnt banned at all! Not one bit!",sender);
			return;
		} else {
			FileConfiguration config = ch.getPlayerConfig(name);
			config.set("ban.banned", false);
			List<String> banHistory = new ArrayList<String>();
			if(config.isString("ban.banReason")){
				banHistory.add(config.getString("ban.banReason"));
				config.set("ban.banReason", banHistory);
			} else {
				banHistory = config.getStringList("ban.banReason");
			}
			banHistory.add(reason+=" - Unbanned by: "+unbanner+" "+getSystemDate());
			config.set("ban.banReason",banHistory);
			ch.savePlayerConfig(config);
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN+unbanner+" Loved and Tolerated "+config.getString("nickname")+"!!");
		}
	}
	private void kickPlayer(String name, CommandSender sender, String reason){
		boolean isCorrie = false;
		if(sender instanceof Player){
			Player areYouCorrie = (Player)sender;
			if(areYouCorrie.getName().equals("TheQueenOfPink")){
				isCorrie = true;
			}
		}
		ArrayList<Player> players = getPlayerOnline(name, isCorrie);
		if(players == null){
			pinkieSay(playerNotOnline(),sender);
			return;
		} else if (players.size()>1){
			pinkieSay(tooManyMatches(players), sender);
			return;
		} else {
			FileConfiguration config = ch.getPlayerConfig(players.get(0));
			List<String> banHistory = config.getStringList("ban.banReason");
			banHistory.add(reason+" - Kicked by: "+sender.getName());
			config.set("ban.banReason", banHistory);
			ch.savePlayerConfig(config);
			players.get(0).kickPlayer(reason);
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED+players.get(0).getDisplayName()+ChatColor.DARK_RED+" was just sent to the everfree forest!");
		}
	}
	public void warnPlayer(String name, String reason, CommandSender sender){
		boolean isOnline = false;
		boolean banned = false;
		FileConfiguration config = null;
		List<Player> players = getPlayerOnline(name,true);
		Player warnee=null;
		if(players == null){
			ArrayList<String> offlinePlayers = getPlayerOffline(name);
			if(offlinePlayers == null){
				pinkieSay(playerNotOnline(),sender);
				return;
			} else if(offlinePlayers.size()>1){
				pinkieSay(tooManyMatches(offlinePlayers),sender);
			} else {
				config = ch.getPlayerConfig(offlinePlayers.get(0));
				name = offlinePlayers.get(0);
			}
		} else if (players.size()>1){
			pinkieSay(tooManyMatches(players),sender);
			return;
		} else {
			config = ch.getPlayerConfig(players.get(0));
			warnee = players.get(0);
			name = warnee.getDisplayName();
		}
		if(config == null){
			pinkieSay("Something... went horribly horribly wrong!",sender);
			return;
		} else {
			banned = config.getBoolean("ban.banned");
			reason += " - Warned by: "+sender.getName();
			Set<String> warningKeys = config.getConfigurationSection("warnings").getKeys(false);
			int size = warningKeys.size();
			if(size==0){
				config.set("warnings.warning1",reason);
				if(isOnline){
					warnee.sendMessage(ChatColor.RED+"You have been warned! Reason: "+reason);
					warnee.sendMessage(ChatColor.RED+"Two warnings left!");
				}
			} else if (size == 1){
				config.set("warnings.warning2",reason);
				if (isOnline) {
					warnee.sendMessage(ChatColor.DARK_RED+ "You have been warned! Reason: " + reason);
					warnee.sendMessage(ChatColor.DARK_RED + "One warning left!");
				}
			} else {
				size++;
				if(!banned){
					reason +=" Note: This triggered a ban";
				}
				config.set("warnings.warning"+size, reason);
				List<String> banHistory = config.getStringList("ban.banReason");
				if (!banned) {
					banHistory.add("Banned: caused by warning - Warned by: "
							+ sender.getName() + " - " + getSystemDate());
				}
				config.set("ban.banReason",banHistory);
				config.set("ban.banned",true);
				if(isOnline){
					warnee.kickPlayer("You have been banned! Visit https://fogelholk.se/banlist/ to appeal!");
					Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED+config.getString("nickname")+sendToMoon);
				}
				if (!banned) {
					sender.sendMessage(ChatColor.DARK_RED+ "Your warning resulted in a ban. Please fill out a ban submission form");
				} else {
					sender.sendMessage(ChatColor.DARK_RED+"That user is already banned. Only fill out a ban submission if you cannot find them on the form");
				}
			}
			sender.sendMessage(ChatColor.DARK_RED+name+" warned!");
			ch.savePlayerConfig(config);
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(cmd.getName().equals("ban")){
			if(args.length == 1){
				banPlayer(sender, args[0], "No Reason Given");
				return true;
			} else if(args.length >1){
				String reason = "";
				for(int i = 1; i <args.length;i++){
					reason += args[i]+" ";
				}
				reason = reason.trim();
				banPlayer(sender, args[0], reason);
				return true;
			}
			return false;
		}
		if(cmd.getName().equals("kick")){
			if(args.length == 1){
				kickPlayer(args[0], sender, "Kicked: No reason given");
				return true;
			} else if(args.length>1){
				String reason = "";
				for(int i = 1; i<args.length;i++){
					reason += args[i]+" ";
				}
				kickPlayer(args[0],sender, "Kicked: "+reason.trim());
				return true;
			}
			return false;
		}
		if(cmd.getName().equals("unban")){
			if(args.length == 1){
				unbanPlayer(sender, args[0], "No Reason Given");
				return true;
			} else if(args.length >1){
				String reason = "";
				for(int i = 1; i <args.length;i++){
					reason += args[i]+" ";
				}
				reason = reason.trim();
				unbanPlayer(sender, args[0], reason);
				return true;
			}
			return false;
			}
		if(cmd.getName().equals("warn")){
			if(args.length<2){
				return pinkieSay(this.noPlayerSpecified(),sender);
			} else {
				String reason = "";
				for(int i =1; i<args.length;i++){
					reason += args[i]+" ";
				}
				List<Player> players = getPlayerOnline(args[0],true);
				if(players == null){
					ArrayList<String> offlinePlayers = getPlayerOffline(args[0]);
					if(offlinePlayers == null){
						return pinkieSay(playerNotOnline(),sender);
					}
					if(offlinePlayers.size()>1){
						return pinkieSay(tooManyMatches(offlinePlayers),sender);
					}
					warnPlayer(offlinePlayers.get(0),reason,sender);
					return true;
				}else if(players.size()>1){
					return pinkieSay(tooManyMatches(players),sender);
				} else {
					warnPlayer(players.get(0).getName(),reason,sender);
					return true;
				}
			}
		}
		if(cmd.getName().equals("convertbans")){
			Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, new BanConvertTask(instance, sender));
			
			return true;
		}
		if(cmd.getName().equals("warnings")){
			if(!(sender instanceof Player)){
				return senderCant(sender);
			} else {
				Player player = (Player)sender;
				player.sendMessage(ChatColor.GRAY+"-----Warnings-----");
				FileConfiguration config = ch.getPlayerConfig(player);
				for(String string : config.getConfigurationSection("warnings").getKeys(false)){
					player.sendMessage(ChatColor.GRAY+config.getString("warnings."+string));
				}
				return true;
			}
		}
		else
			return false;
	}
	public boolean isBanned(String name){
		FileConfiguration config = ch.getPlayerConfig(name);
		if(config == null){
			return false;
		} else return config.getBoolean("ban.banned");
	}
	public String getSystemDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event){
		event.setLeaveMessage(null);
	}
	@EventHandler
	public void loginEvent(PlayerPreLoginEvent event){
		if(isBanned(event.getName())){
			event.disallow(Result.KICK_BANNED, "You are banned! Visit https://fogelholk.se/banlist/ to appeal!");
		}
	}
	@Override
	public void furtherInitialization() {
	}

	@Override
	public PoniCommandExecutor getThis() {
		return this;
	}

}
