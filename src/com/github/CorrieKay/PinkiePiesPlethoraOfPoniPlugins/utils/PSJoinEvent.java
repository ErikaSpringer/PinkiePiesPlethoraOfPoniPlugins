package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PSJoinEvent extends PlayerJoinEvent {
	
	private final boolean isReallyJoining;
	
	public PSJoinEvent(Player playerJoined, String joinMessage, boolean isReallyJoining) {
		super(playerJoined, joinMessage);
		this.isReallyJoining = isReallyJoining;
	}

	public boolean isJoining() {
		return isReallyJoining;
	}
}
