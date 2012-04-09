package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;

public abstract class PoniCommandExecutor implements CommandExecutor{

	public PoniCommandExecutor(Mane instance, String[] commandsToRegister){
		for(String cmds : commandsToRegister){
			instance.getCommand(cmds).setExecutor(this);
		}
	}
	protected boolean cantDo(Player player){
		player.sendMessage(ChatColor.DARK_GRAY+"You can't do this :c");
		return true;
	}
}
