package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
	/**
	 * @Functions here deal with inventory translation, from stringList to inventory, and from inventory to StringList
	 */
	public static ItemStack[] toInventory(List<String> inventoryString){
		ItemStack[] items = new ItemStack[36];
		int number = 0;
		for(String stack : inventoryString){
			if(!stack.equals("air")){
				String[] bits = stack.split(" ");
				Material mat = Material.getMaterial(Integer.parseInt(bits[0]));
				int ammount = Integer.parseInt(bits[1]);
				short durability = Short.parseShort(bits[2]);
				byte data = Byte.parseByte(bits[3]);
				ItemStack item = new ItemStack(mat,ammount, durability, data);
				HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
				for(int i = 4 ; i<bits.length;i=i+2){
					enchantments.put(Enchantment.getById(Integer.parseInt(bits[i])), Integer.parseInt(bits[i+1]));
				}
				item.addUnsafeEnchantments(enchantments);
				items[number] = item;
			} else {
				items[number] = new ItemStack(Material.AIR);
			}
			number++;
		}
		return items;
	}
	public static List<String> toStringList(ItemStack[] inventory){
		List<String> stringInventory = new ArrayList<String>();
		for(ItemStack stack : inventory){
			if(stack == null){
				stringInventory.add("air");
			} else {
				String invToString = "";
				invToString += stack.getTypeId();
				invToString+=" "+stack.getAmount();
				invToString+=" "+stack.getDurability();
				invToString+=" "+stack.getData().getData();
				for(Enchantment enchant : stack.getEnchantments().keySet()){
					invToString+=" "+enchant.getId()+" "+stack.getEnchantmentLevel(enchant);
				}
				stringInventory.add(invToString);
			}
		}
		return stringInventory;
	}
}
