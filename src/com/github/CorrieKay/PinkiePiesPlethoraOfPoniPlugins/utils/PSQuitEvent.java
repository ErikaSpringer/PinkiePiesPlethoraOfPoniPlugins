package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerQuitEvent;

public class PSQuitEvent extends PlayerQuitEvent implements Cancellable{

	private final boolean isReallyQuitting;
	private final String quitMessage2;
	private boolean cancelled = false;
	
	public PSQuitEvent(Player playerJoined, boolean isReallyQuitting) {
		super(playerJoined, ChatColor.RED+playerJoined.getDisplayName()+ChatColor.AQUA+" has left Equestria!");
		quitMessage2 = ChatColor.RED+playerJoined.getDisplayName()+ChatColor.AQUA+" has left Equestria!";
		this.isReallyQuitting = isReallyQuitting;
	}
	public boolean isQuitting() {
		return isReallyQuitting;
	}
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean arg) {
		if(arg){
			setQuitMessage(null);
		} else {
			setQuitMessage(quitMessage2);
		}
		cancelled = arg;
	}
}
