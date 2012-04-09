/**
 * @Name: AFK handler 
 * @Author: CorrieKay
 * 
 * @Function: Handles afk (away from keyboard) Statuses of players on the server
 * 
 * @Details: There are two types of afk status. Natural and Manual. Natural afk stems from a user sitting still,
 * not breaking blocks, or chatting, for ninty seconds. Manual afk happens when a player uses the /afk command.
 * The user is no longer being listened to by the run() loop. They will not be toggled afk when they move,
 * chat, or break blocks. the afk status will remain true until they type /afk again, the server reloads, or they relog into the server
 * 
 *@More_Details: The plugin suite listens to custom join events. Most are thrown by the main plugin and the invisibility handler. 
 *This plugin will use those to desync (read: stop listening/start listening)
 * specific players.
 */

package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSQuitEvent;

public class AFKhandler extends TimerTask implements Listener, CommandExecutor{
	
	private final Map<Player,Boolean> playerAfk = new HashMap<Player,Boolean>();
	private final Map<Player,Integer> playerTick = new HashMap<Player,Integer>();
	private final ArrayList<Player> manuAFK = new ArrayList<Player>();
	
	public void initialize(Mane instance){
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, this, 0, 20);
	}
	public boolean isAfk(Player player){
		if(playerAfk.containsKey(player)){
			return playerAfk.get(player);
		}
		if(manuAFK.contains(player)){
			return true;
		} else return false;
	}
	public void setAfk(Player player, boolean afk){
		if(afk){
			playerAfk.put(player, afk);
			Bukkit.getServer().broadcastMessage(ChatColor.GRAY+player.getDisplayName()+ChatColor.GRAY+ " is now afk.");
		} else {
			playerAfk.remove(player);
			Bukkit.getServer().broadcastMessage(ChatColor.GRAY+player.getDisplayName()+ChatColor.GRAY+" is no longer afk.");
		}
	}
	public void run() {
		for(Player player : playerTick.keySet()){
			if (!manuAFK.contains(player)) {
				int i = playerTick.get(player) + 1;
				playerTick.put(player, i);
				if (i == 5) {
					setAfk(player, true);
				}
			}
		}
	}
	private void playerActivity(Player player){
		if(playerTick.containsKey(player)){
			if(!manuAFK.contains(player)){
				if(isAfk(player)){
					setAfk(player, false);
				}
				playerTick.put(player, 0);
			}
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Player Command Only");
			return true;
		}
		Player player = (Player)sender;
		if (!manuAFK.contains(player)) {
			if (!isAfk(player)) {
				player.sendMessage(ChatColor.GRAY + player.getDisplayName()+ ChatColor.GRAY + " is now afk.");
			}
			playerAfk.put(player, false);
			playerTick.put(player, 0);
			manuAFK.add(player);
			return true;
		} else {
			player.sendMessage(ChatColor.GRAY+player.getDisplayName()+ChatColor.GRAY+" is no longer afk.");
			playerAfk.put(player, false);
			playerTick.put(player, 0);
			manuAFK.remove(player);
			return true;
		}
	}
	@EventHandler
	public void onJoin(PSJoinEvent event){
		if (!event.isCancelled()) {
			playerAfk.put(event.getPlayer(), false);
			playerTick.put(event.getPlayer(), 0);
		}
	}
	@EventHandler
	public void onQuit(PSQuitEvent event){
		playerAfk.remove(event.getPlayer());
		playerTick.remove(event.getPlayer());
		manuAFK.remove(event.getPlayer());
	}
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		playerActivity(event.getPlayer());
	}
	@EventHandler
	public void onChat(PlayerChatEvent event){
		if(!event.getMessage().substring(0,4).equals("/afk")){
			playerActivity(event.getPlayer());
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		playerActivity(event.getPlayer());
	}
}