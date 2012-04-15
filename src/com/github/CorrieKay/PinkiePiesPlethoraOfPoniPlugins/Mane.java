package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.AFKhandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.ConfigHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.InventorySee;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.InvisibilityHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.JoinHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.ProtectionHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.QuitHandler;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSQuitEvent;

public class Mane extends JavaPlugin implements Listener{
	private final AFKhandler afk = new AFKhandler(this);
	private final ConfigHandler ch = new ConfigHandler(this);
	private final JoinHandler jh = new JoinHandler(this);
	private final QuitHandler qh = new QuitHandler(this);
	private final ProtectionHandler ph = new ProtectionHandler(this, new String[] {"watertoggle","lavatoggle"});
	private final InvisibilityHandler ih = new InvisibilityHandler(this, new String[] {"hide","fakehide","nopickup"});
	private final InventorySee is = new InventorySee(this, new String[] {"invsee","commitinventorychange"});
	
	public void onEnable(){
		PluginManager pm = Bukkit.getPluginManager();
		for(String string : YamlConfiguration.loadConfiguration(this.getResource("plugin.yml")).getConfigurationSection("commands").getKeys(false)){
			this.getCommand(string).setPermissionMessage(ChatColor.LIGHT_PURPLE+"Pinkie Pie: Oh no! You cant do this :c");
		}
		afk.initialize();
		ih.initialize();
		is.initialize();
		ph.initialize();
		pm.registerEvents(this, this);
		pm.registerEvents(ih, this);
		pm.registerEvents(jh, this);
		pm.registerEvents(qh, this);
		pm.registerEvents(afk, this);
		pm.registerEvents(is, this);
		pm.registerEvents(ph,this);
		getCommand("afk").setExecutor(afk);
	}
	public void onDisable(){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			Bukkit.getPluginManager().callEvent(new PSQuitEvent(player,true));
		}
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
		return ch;
	}
	public AFKhandler getAfkHandler(){
		return afk;
	}
	public InvisibilityHandler getInvisHandler(){
		return ih;
	}
}
