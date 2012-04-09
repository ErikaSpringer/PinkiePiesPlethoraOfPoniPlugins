package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public abstract class PoniCommandExecutor implements CommandExecutor{

	protected boolean cantDo(Player player){
		player.sendMessage(ChatColor.DARK_GRAY+"You can't do this :c");
		return true;
	}
}
