package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSJoinEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PSQuitEvent;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;

public class InvisibilityHandler extends PoniCommandExecutor implements Listener{
	
	private final Mane instance;
	private final ArrayList<Player> invisiblePlayers = new ArrayList<Player>();
	private final ArrayList<Player> noPickup = new ArrayList<Player>();
	private final String[] cmds;
	public InvisibilityHandler(Mane instance, String[] cmds){
		super(instance);
		this.instance = instance;
		this.cmds = cmds;
	}
	public void initialize(){
		registerCommands(instance, cmds, this);
	}
	public boolean isHidden(Player player){
		if(invisiblePlayers.contains(player)){
			return true;
		} else return false;
	}
	public void toggleInvisibility(Player player, boolean silent){
		if(isHidden(player)){
			turnOff(player,silent);
		} else turnOn(player,silent);
	}
	private void turnOn(Player player, boolean silent){
		invisiblePlayers.add(player);
		noPickup.add(player);
		player.sendMessage(ChatColor.GRAY+"You are now hidden! Pickup off!");
		//hides the player from everyone but TheQueenOfPink
		for(Player player2 : Bukkit.getServer().getOnlinePlayers()){
			if(!player2.getName().equals("TheQueenOfPink")&&!player.equals(player2)){
				player2.hidePlayer(player);
			}
		}
		//if youre fakequitting
		PSQuitEvent event = new PSQuitEvent(player, false);
		if(silent){
			event.setCancelled(true);
		}
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			Bukkit.getServer().broadcastMessage(event.getQuitMessage());
		}
		//cancels targets on player going invisible
		List<Entity> nearby = player.getNearbyEntities(100, 100, 100);
		for(Entity entity : nearby){
			if(entity instanceof Creature){
				Creature creature = (Creature)entity;
				Entity entity2 = creature.getTarget();
				if(entity2 instanceof Player){
					Player player2 = (Player)entity2;
					if(player.equals(player2)){
						creature.setTarget(null);
					}
				}
			}
		}
		FileConfiguration config = instance.getConfigHandler().getPlayerConfig(player);
		config.set("invisible", true);
		instance.getConfigHandler().savePlayerConfig(config);
	}
	private void turnOff(Player player, boolean silent){
		invisiblePlayers.remove(player);
		noPickup.remove(player);
		player.sendMessage(ChatColor.GRAY+"You are now visible! Pickup on!");
		for(Player player2 : Bukkit.getServer().getOnlinePlayers()){
			player2.showPlayer(player);
		}
		PSJoinEvent event = new PSJoinEvent(player, false);
		if(silent){
			event.setCancelled(true);
		}
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()){
			Bukkit.getServer().broadcastMessage(event.getJoinMessage());
		}
		FileConfiguration config = instance.getConfigHandler().getPlayerConfig(player);
		config.set("invisible", false);
		instance.getConfigHandler().savePlayerConfig(config);
	}
	public boolean isPickingUp(Player player){
		if(noPickup.contains(player)){
			return false;
		} else return true;
	}
	public void togglePickup(Player player){
		if(isPickingUp(player)){
			pickupOff(player);
		} else pickupOn(player);
	}
	private void pickupOff(Player player){
		noPickup.add(player);
		player.sendMessage(ChatColor.GRAY+"Pickup off!");
	}
	private void pickupOn(Player player){
		noPickup.remove(player);
		player.sendMessage(ChatColor.GRAY+"Pickup on!");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(!(sender instanceof Player)){
			return senderCant(sender);
		} else {
			Player player = (Player)sender;
			if(checkPerm("pppopp.hide",player)){
				if(cmd.getName().equals("hide")){
					toggleInvisibility(player,true);
				}
				if(cmd.getName().equals("fakehide")){
					toggleInvisibility(player,false);
				} return true;
			} else return cantDo(player);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTarget(EntityTargetEvent event){
		if(event.getTarget() instanceof Player){
			Player target = (Player)event.getTarget();
			if(invisiblePlayers.contains(target)){
				event.setCancelled(true);
			}
		}
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onJoin(PSJoinEvent event){
		if(event.isJoining()){
			FileConfiguration config = instance.getConfigHandler().getPlayerConfig(event.getPlayer());
			if(config.getBoolean("invisible")){
				turnOn(event.getPlayer(), true);
				event.setCancelled(true);
				Player corrie = Bukkit.getServer().getPlayer("TheQueenOfPink");
				if(corrie!=null){
					corrie.sendMessage(ChatColor.DARK_GRAY+event.getPlayer().getDisplayName()+ChatColor.DARK_GRAY+" has logged in silently");
				}
			}
			for(Player player : Bukkit.getServer().getOnlinePlayers()){
				if(!invisiblePlayers.contains(player)){
					event.getPlayer().showPlayer(player);
				}
			}
		}
	}
	@EventHandler
	public void onQuit(PSQuitEvent event){
		if(invisiblePlayers.contains(event.getPlayer())&&event.isQuitting()){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event){
		if(noPickup.contains(event.getPlayer())){
			event.setCancelled(true);
		}
	}
}
