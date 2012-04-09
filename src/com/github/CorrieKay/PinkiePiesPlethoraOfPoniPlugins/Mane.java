package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.AFKhandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSQuitEvent;

public class Mane extends JavaPlugin implements Listener{
	AFKhandler afk = new AFKhandler();
	
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		afk.initialize(this);
		pm.registerEvents(this, this);
		pm.registerEvents(afk, this);
		getCommand("afk").setExecutor(afk);
	}
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		PSJoinEvent psjoin = new PSJoinEvent(event.getPlayer(), event.getJoinMessage(), true);
		Bukkit.getPluginManager().callEvent(psjoin);
		event.setJoinMessage(psjoin.getJoinMessage());
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event){
		PSQuitEvent psquit = new PSQuitEvent(event.getPlayer(), event.getQuitMessage(), true);
		Bukkit.getPluginManager().callEvent(psquit);
		event.setQuitMessage(psquit.getQuitMessage());
	}
}
