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
	protected final String pinkieSays = ChatColor.LIGHT_PURPLE+"[ServerGuardian]Pinkie Pie : ";
	protected final ChatColor pinkieColor = ChatColor.LIGHT_PURPLE;
	
	public PoniCommandExecutor(Mane plugin){
		instance = plugin;
	}

	public void registerCommands(String[] commandsToRegister, PoniCommandExecutor executor){
		for(String cmds : commandsToRegister){
			instance.getCommand(cmds).setExecutor(executor);
		}
	}
	protected boolean cantDo(Player player){
		player.sendMessage(pinkieSays+"Oh no! Im sorry, but you dont have permission to do this! :c");
		return true;
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
	protected ArrayList<Player> getPlayer(String arg, boolean isCorrie){
		ArrayList<Player> players = new ArrayList<Player>();
		for(Player checkPlayer : Bukkit.getServer().getOnlinePlayers()){
			System.out.print(checkPlayer.getName());
			if(checkPlayer.getName().contains(arg)||ChatColor.stripColor(checkPlayer.getDisplayName()).contains(arg)){
				System.out.print("name matches...");
				if(!instance.getInvisHandler().isHidden(checkPlayer)||isCorrie){
					System.out.print("adding player");
					players.add(checkPlayer);
				}
			}
		}
		if(players.size()==0){
			System.out.print("size is zero, returning null.");
			return null;
		}
		System.out.print("there are players in here!");
		return players;
	}
	protected ArrayList<String> getPlayerName(String arg){
		ArrayList<String> playerNames = new ArrayList<String>();
		for(File file : (new File(instance.getDataFolder()+File.separator+"players")).listFiles()){
			String fileName = file.getName();
			fileName = fileName.substring(0, fileName.length()-4);
			if(fileName.contains(arg)){
				playerNames.add(fileName);
			}
		}
		if(playerNames.size()==0){
			return null;
		} else return playerNames;
	}
	protected String tooManyMatches(List<Player> list){
		String stringList = "";
		for(Player player : list){
			stringList+=ChatColor.RED+player.getDisplayName()+ChatColor.GRAY+", ";
		}
		stringList=stringList.substring(0,stringList.length()-2);
		return pinkieSays+"Too many matches silly! : "+stringList;
	}
	protected String tooManyMatches(String derp){
		String stringList = "";
		//for(String string : list){
			//stringList+=ChatColor.RED+string+ChatColor.GRAY+", ";
		//}
		stringList = stringList.substring(0, stringList.length()-2);
		return pinkieSays+"Too many matches silly! : "+ stringList;
	}
}
