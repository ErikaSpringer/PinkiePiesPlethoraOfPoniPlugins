package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerJoinEvent;

public class PSJoinEvent extends PlayerJoinEvent implements Cancellable{
	
	private boolean cancelled = false;
	private boolean silent = false;
	private final boolean isReallyJoining;
	
	public PSJoinEvent(Player playerJoined, String joinMessage, boolean isReallyJoining) {
		super(playerJoined, joinMessage);
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
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
	}
	public boolean isSilent() {
		return silent;
	}
	public void setSilent(boolean silent) {
		this.silent = silent;
	}
}
