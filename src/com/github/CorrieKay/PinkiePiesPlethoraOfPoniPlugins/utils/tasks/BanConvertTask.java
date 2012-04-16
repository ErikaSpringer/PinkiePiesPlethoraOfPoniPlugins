package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;

public class BanConvertTask extends TimerTask {

	private final Mane instance;
	private final CommandSender sender;
	
	public BanConvertTask(Mane instance,CommandSender sender){
		this.instance = instance;
		this.sender = sender;
	}
	
	@Override
	public void run() {
		sender.sendMessage("Converting bans...");
		int i = 0;
		for(OfflinePlayer offPlayer : Bukkit.getServer().getBannedPlayers()){
			FileConfiguration config;
			config = instance.getConfigHandler().getPlayerConfig(offPlayer.getName());
			if (config == null) {
				config = instance.getConfigHandler().createOfflinePlayerConfig(offPlayer.getName());
			}
			try {
				config.set("ban.banned", true);
				offPlayer.setBanned(false);
				instance.getConfigHandler().savePlayerConfig(config);
				System.out.print("banning " + offPlayer.getName());
			} catch (Exception e) {
				i++;
			}
		}
		sender.sendMessage("Bans converted!");
		sender.sendMessage(i+" errors.");
	}

}
