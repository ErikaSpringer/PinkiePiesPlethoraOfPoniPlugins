package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.AFK.AFKhandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.ConfigHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.InventorySee;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.InvisibilityHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.JoinHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.ProtectionHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.QuitHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSQuitEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks.BootTask;

public class Mane extends JavaPlugin implements Listener{
	private final HashMap<String,PoniCommandExecutor> cmdList = new HashMap<String,PoniCommandExecutor>();
	
	public void onEnable(){
		cmdList.put("Config Handler",new ConfigHandler(this,"Config Handler"));
		cmdList.put("Afk Handler",new AFKhandler(this, new String[] {"afk","afkdebug"}, "Afk Handler"));
		cmdList.put("Invisibility Handler",new InvisibilityHandler(this, new String[] {"hide","fakehide","nopickup"},"Invisibility Handler"));
		cmdList.put("Join Handler",new JoinHandler(this,"Join Handler"));
		cmdList.put("Quit Handler",new QuitHandler(this,"Quit Handler"));
		cmdList.put("Protection Handler",new ProtectionHandler(this, new String[] {"watertoggle","lavatoggle"},"Protection Handler"));
		cmdList.put("Inventory See Handler",new InventorySee(this, new String[] {"invsee","commitinventorychange"},"Inventory See Handler"));
		System.out.print("-----PPPoPP initializing Handlers-----");
		for(String string : cmdList.keySet()){
			cmdList.get(string).initialize();
		}
		System.out.print("PPPoPP Registering Commands");
		FileConfiguration config = YamlConfiguration.loadConfiguration(this.getResource("plugin.yml"));
		for(String commandName : config.getConfigurationSection("commands").getKeys(false)){
			String executorName = config.getString("commands."+commandName+".executor");
			PoniCommandExecutor executor = cmdList.get(executorName);
			executor.registerCommand(commandName, executor);
		}
		System.out.print("--------------------------------------");
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
		return (ConfigHandler)cmdList.get("Config Handler");
	}
	public AFKhandler getAfkHandler(){
		return (AFKhandler) cmdList.get("Afk Handler");
	}
	public InvisibilityHandler getInvisHandler(){
		return (InvisibilityHandler) cmdList.get("Invisibility Handler");
	}
}
