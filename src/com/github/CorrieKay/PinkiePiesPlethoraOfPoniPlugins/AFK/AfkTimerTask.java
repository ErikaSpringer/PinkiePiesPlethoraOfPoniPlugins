package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.AFK;

import java.util.TimerTask;

import org.bukkit.entity.Player;


public class AfkTimerTask extends TimerTask {

	private final AFKhandler afk;
	private final int time = 90;
	
	public AfkTimerTask(AFKhandler afk){
		this.afk=afk;
	}
	
	public void run() {
		for(Player player : afk.playerTick.keySet()){
			if (!afk.manuAFK.contains(player)) {
				int i = afk.playerTick.get(player) + 1;
				if (afk.playerTick.get(player)<time) {
					afk.playerTick.put(player, i);
				}
				if (i == time) {
					afk.setAfk(player, true);
				}
			}
		}
	}

}
