package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.AFK;

import java.util.TimerTask;

import org.bukkit.entity.Player;


public class AfkTimerTask extends TimerTask {

	AFKhandler afk;
	
	public AfkTimerTask(AFKhandler afk){
		this.afk=afk;
	}
	
	public void run() {
		for(Player player : afk.playerTick.keySet()){
			if (!afk.manuAFK.contains(player)) {
				int i = afk.playerTick.get(player) + 1;
				afk.playerTick.put(player, i);
				if (i == 90) {
					afk.setAfk(player, true);
				}
			}
		}
	}

}
