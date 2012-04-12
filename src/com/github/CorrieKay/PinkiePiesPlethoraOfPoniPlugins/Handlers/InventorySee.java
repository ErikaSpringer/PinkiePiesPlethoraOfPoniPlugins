package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.LookitInventory;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;

public class InventorySee extends PoniCommandExecutor implements Listener{
	
	private final Mane instance;
	private HashMap<Player,Player> viewingInventoryLive = new HashMap<Player,Player>();
	private final String[] cmds;
	public InventorySee(Mane instance, String[] cmds){
		super(instance);
		this.instance = instance;
		this.cmds = cmds;
	}
	public void initialize(){
		super.registerCommands(cmds, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			return senderCant(sender);
		}
		Player player = (Player)sender;
		if(args.length == 0){
			return pinkieSay("Oh my.. It looks like you made a mistake... Theres no player selected, silly!", player);
		}
		ArrayList<Player> players = getPlayer(args[0],true);
		for(Player derp : players){
			player.sendMessage(derp.getName());
		}
		Player player2 = players.get(0);
		listenToInventory(player, player2);
		return true;
	}
	/**
	 * @Functions here deal with live inventory viewing.
	 */
	private void listenToInventory(Player listener, Player inventoryOwner){
		viewingInventoryLive.put(listener, inventoryOwner);
		viewingInventoryLive.put(inventoryOwner, listener);
		listener.sendMessage(pinkieSays+"Hey, youre watching "+inventoryOwner.getDisplayName()+pinkieColor+"\'s pockets!");
	}
	@EventHandler
	public void onInventoryEdit(InventoryClickEvent event){
		if(!(event.getWhoClicked() instanceof Player)){
			return;
		}
		Player player = (Player)event.getWhoClicked();
		if(viewingInventoryLive.containsKey(player)){
			Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, new LookitInventory(viewingInventoryLive.get(player),player), 1);
		}
	}
}
