package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PSQuitEvent extends PlayerQuitEvent{

	private final boolean isReallyQuitting;
	private boolean silent = false;
	
	public PSQuitEvent(Player who, String quitMessage, boolean isReallyQuitting) {
		super(who, quitMessage);
		this.isReallyQuitting = isReallyQuitting;
	}
	public boolean getQuitting() {
		return isReallyQuitting;
	}
	public boolean isSilent(){
		return silent;
	}
	public void setSilent(Boolean arg){
		silent = arg;
	}
}
