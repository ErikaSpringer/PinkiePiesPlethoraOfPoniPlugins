package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public class PSQuitEvent extends PlayerQuitEvent {

	private final boolean isReallyQuitting;
	
	public PSQuitEvent(Player who, String quitMessage, boolean isReallyQuitting) {
		super(who, quitMessage);
		this.isReallyQuitting = isReallyQuitting;
	}

	public boolean getQuitting() {
		return isReallyQuitting;
	}

}
