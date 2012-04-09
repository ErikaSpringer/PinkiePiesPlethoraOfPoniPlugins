package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;

public class InvisibilityHandler extends PoniCommandExecutor implements Listener{
	
	private final Mane instance;
	private final ArrayList<Player> invisiblePlayers = new ArrayList<Player>();
	private final ArrayList<Player> noPickup = new ArrayList<Player>();
	public InvisibilityHandler(Mane instance, String[] cmds){
		super(instance, cmds);
		this.instance = instance;
	}
	public void initialize(){
		
	}
	private void turnOn(Player player){
		invisiblePlayers.add(player);
		noPickup.add(player);
		for(Player player2 : Bukkit.getServer().getOnlinePlayers()){
			if(!player2.getName().equals("TheQueenOfPink")&&!player.equals(player2)){
				player2.hidePlayer(player);
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
