package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.AFK.AFKhandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.*;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.*;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks.BootTask;

public class Mane extends JavaPlugin implements Listener{
	private final ArrayList<PoniCommandExecutor> cmdList = new ArrayList<PoniCommandExecutor>();
	
	public void onEnable(){
		cmdList.add(new ConfigHandler(this,"Config Handler"));
		cmdList.add(new AFKhandler(this, new String[] {"afkdebug","afk"},"AFK Handler"));
		cmdList.add(new InvisibilityHandler(this, new String[] {"hide","fakehide","nopickup"},"Invisibility Handler"));
		cmdList.add(new JoinHandler(this,"Join Handler"));
		cmdList.add(new QuitHandler(this,"Quit Handler"));
		cmdList.add(new ProtectionHandler(this, new String[] {"watertoggle","lavatoggle"},"Protection Handler"));
		cmdList.add(new InventorySee(this, new String[] {"invsee","commitinventorychange"},"Inventory See Handler"));
		for(PoniCmdExeInterface handler : cmdList){
			handler.initialize();
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new BootTask());
	}
	
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		if (!(event instanceof PSJoinEvent)) {
			PSJoinEvent psjoin = new PSJoinEvent(event.getPlayer(), true);
			Bukkit.getPluginManager().callEvent(psjoin);
			event.setJoinMessage(psjoin.getJoinMessage());
		}
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event){
		if (!(event instanceof PSQuitEvent)) {
			PSQuitEvent psquit = new PSQuitEvent(event.getPlayer(), true);
			Bukkit.getPluginManager().callEvent(psquit);
			event.setQuitMessage(psquit.getQuitMessage());
		}
	}
	public ConfigHandler getConfigHandler() {
		return (ConfigHandler)cmdList.get(0);
	}
	public AFKhandler getAfkHandler(){
		return (AFKhandler) cmdList.get(1);
	}
	public InvisibilityHandler getInvisHandler(){
		return (InvisibilityHandler) cmdList.get(2);
	}
}
