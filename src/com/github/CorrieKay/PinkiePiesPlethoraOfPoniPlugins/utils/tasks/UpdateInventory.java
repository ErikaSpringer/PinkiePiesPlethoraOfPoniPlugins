package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks;

import java.util.TimerTask;

import org.bukkit.entity.Player;

public class UpdateInventory extends TimerTask {

	private final Player inventory;
	private final Player looker;
	
	public UpdateInventory(Player inventory, Player looker){
		this.inventory = looker;
		this.looker = inventory;
	}
	@Override
	public void run() {
		inventory.getInventory().setContents(looker.getInventory().getContents());
	}

}
