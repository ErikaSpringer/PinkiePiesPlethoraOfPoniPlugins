package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;

public abstract class PoniCommandExecutor implements CommandExecutor{
	
	protected final Mane instance;
	protected final String pinkieSays = ChatColor.LIGHT_PURPLE+"Pinkie Pie: ";
	protected final ChatColor pinkieColor = ChatColor.LIGHT_PURPLE;
	
	public PoniCommandExecutor(Mane plugin){
		instance = plugin;
	}

	public void registerCommands(String[] commandsToRegister, PoniCommandExecutor executor){
		for(String cmds : commandsToRegister){
			instance.getCommand(cmds).setExecutor(executor);
		}
	}
	protected boolean pinkieSay(String message, Player player){
		player.sendMessage(pinkieSays+message);
		return true;
	}
	protected boolean senderCant(CommandSender sender){
		sender.sendMessage("You aren't a player");
		return true;
	}
	protected boolean checkPerm(String permission, Player player){
		if(player.hasPermission(permission)){
			return true;
		} else return false;
	}
	/**
	 * @Function: these functions return lists of players.
	 */
	protected ArrayList<Player> getPlayerOnline(String arg, boolean isCorrie){
		arg = arg.toLowerCase();
		ArrayList<Player> players = new ArrayList<Player>();
		for(Player checkPlayer : Bukkit.getServer().getOnlinePlayers()){
			if(checkPlayer.getName().toLowerCase().contains(arg)||ChatColor.stripColor(checkPlayer.getDisplayName()).toLowerCase().contains(arg)){
				if(!instance.getInvisHandler().isHidden(checkPlayer)||isCorrie){
					players.add(checkPlayer);
				}
			}
		}
		if(players.size()==0){
			return null;
		}
		return players;
	}
	protected ArrayList<String> getPlayerOffline(String arg){
		arg = arg.toLowerCase();
		ArrayList<String> playerNames = new ArrayList<String>();
		for(File file : (new File(instance.getDataFolder()+File.separator+"players")).listFiles()){
			String fileName = file.getName();
			fileName = fileName.substring(0, fileName.length()-4);
			if(fileName.toLowerCase().contains(arg)){
				playerNames.add(fileName);
			}
		}
		if(playerNames.size()==0){
			return null;
		} else return playerNames;
	}
	/**
	 * @Function: these methods return error messages
	 */
	protected String tooManyMatches(List<Player> list){
		String stringList = "";
		for(Player player : list){
			stringList+=ChatColor.RED+player.getDisplayName()+ChatColor.GRAY+", ";
		}
		stringList=stringList.substring(0,stringList.length()-2);
		return "Too many matches silly! : "+stringList;
	}
	protected String tooManyMatches(ArrayList<String> list){
		String stringList = "";
		for(String string : list){
			stringList+=ChatColor.RED+string+ChatColor.GRAY+", ";
		}
		stringList = stringList.substring(0, stringList.length()-2);
		return "Too many matches silly! : "+ stringList;
	}
	protected String noPlayerSpecified(){
		return "Oh my.. It looks like you made a mistake... Theres no player selected, silly!";
	}
	protected String playerNotOnline(){
		return "I looked high and low, but I couldnt find that pony! :C";
	}
	protected String inventoryCorrupt(){
		return "That players config inventory doesnt seem to want to co-operate, the silly filly!";
	}
}
