package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;

public abstract class PoniCommandExecutor implements CommandExecutor{

	public void registerCommands(Mane instance, String[] commandsToRegister, PoniCommandExecutor executor){
		for(String cmds : commandsToRegister){
			instance.getCommand(cmds).setExecutor(executor);
		}
	}
	protected boolean cantDo(Player player){
		player.sendMessage(ChatColor.DARK_GRAY+"You can't do this :c");
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
	protected ArrayList<Player> getPlayer(String arg){
		ArrayList<Player> players = new ArrayList<Player>();
		for(Player checkPlayer : Bukkit.getServer().getOnlinePlayers()){
			if(checkPlayer.getName().contains(arg)||ChatColor.stripColor(checkPlayer.getDisplayName()).contains(arg)){
				players.add(checkPlayer);
			}
		}
		return players;
	}
}
