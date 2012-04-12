package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils;

import java.util.TimerTask;

import org.bukkit.entity.Player;

public class LookitInventory extends TimerTask {

	private final Player inventory;
	private final Player looker;
	
	public LookitInventory(Player inventory, Player looker){
		this.inventory = inventory;
		this.looker = looker;
	}
	@Override
	public void run() {
		inventory.getInventory().setContents(looker.getInventory().getContents());
	}

}
