package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks;

import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeGuardDebootPlugin extends TimerTask {
	
	private final JavaPlugin plugin;
	public SafeGuardDebootPlugin(JavaPlugin plugin){
		this.plugin = plugin;
	}

	@Override
	public void run() {
		Bukkit.getPluginManager().disablePlugin(plugin);
		Bukkit.getLogger().severe("PPPOPP DEACTIVATING TO PREVENT SYSTEM INSTABILITY AND SAFEGUARD CONFIGURATIONS");
	}

}
