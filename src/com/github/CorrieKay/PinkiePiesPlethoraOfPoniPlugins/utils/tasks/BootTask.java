package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;

public class BootTask extends TimerTask {

	@Override
	public void run() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			Bukkit.getPluginManager().callEvent(new PSJoinEvent(player, true));
		}
	}

}
