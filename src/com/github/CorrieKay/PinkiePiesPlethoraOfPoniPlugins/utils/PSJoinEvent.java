package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerJoinEvent;

public class PSJoinEvent extends PlayerJoinEvent implements Cancellable{

	private final boolean isReallyJoining;
	private final String joinMessage2;
	private boolean cancelled = false;
	
	public PSJoinEvent(Player playerJoined, boolean isReallyJoining) {
		super(playerJoined, ChatColor.RED+playerJoined.getDisplayName()+ChatColor.AQUA+" has returned to Equestria!");
		joinMessage2 = ChatColor.RED+playerJoined.getDisplayName()+ChatColor.AQUA+" has returned to Equestria!";
		this.isReallyJoining = isReallyJoining;
	}
	public boolean isJoining() {
		return isReallyJoining;
	}
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean arg) {
		if(arg){
			setJoinMessage(null);
		} else {
			setJoinMessage(joinMessage2);
		}
		cancelled = arg;
	}
}
