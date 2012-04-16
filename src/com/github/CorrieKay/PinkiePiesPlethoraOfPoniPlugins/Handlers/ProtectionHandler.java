package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.TrapDoor;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks.*;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;

public class ProtectionHandler extends PoniCommandExecutor implements Listener{
	Mane instance;
	private boolean waterFlow = true;
	private boolean lavaFlow = false;
	boolean creeperBuff = false;
	private HashMap<Block,Integer> lavaToNerf = new HashMap<Block,Integer>();
	public ProtectionHandler(Mane instance, String name){
		super(instance,name);
		this.instance = instance;
	}
	public HashMap<Block,Integer> getNerfedLava(){
		return lavaToNerf;
	}
	public void setNervedLava(HashMap<Block,Integer> derp){
		lavaToNerf = derp;
	}
	@EventHandler
	public void explosionHandler(EntityExplodeEvent event){//stops explosions from damaging the world
		event.setCancelled(true);
	}
	@EventHandler
	public void fireDamageControl(BlockBurnEvent event){//stops fire from damaging a block
		event.setCancelled(true);
	}
	@EventHandler
	public void fireDamageControl2(BlockSpreadEvent event){//prevents fire from spreading
		if(event.getNewState().getType()==Material.FIRE){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void fireDamageControl3(PlayerInteractEvent event){//stops a player from lighting shit on fire if theyre not an OP, but allows them to ignite netherrack still
		try {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getItem().getType() == Material.FLINT_AND_STEEL) {
					if (!event.getPlayer().isOp()) {
						if (event.getClickedBlock().getType() != Material.NETHERRACK) {
							event.setCancelled(true);
						}
					}
				}
			}
		} catch (NullPointerException e) {
			return;
		}
	}
	@EventHandler
	public void blockPlaceAlert(PlayerBucketEmptyEvent event){//alerts moderators to lava placement
		if(event.getBucket()==Material.LAVA_BUCKET){
			Player player = event.getPlayer();
			instance.getServer().broadcast(ChatColor.DARK_RED+player.getDisplayName()+ChatColor.DARK_RED+" has placed Lava!", "pppopp.blockwarn");
			Block lava = event.getBlockClicked().getRelative(event.getBlockFace());
			lavaToNerf.put(lava, 30);
		}
	}
	@EventHandler 
	public void pickupLava(PlayerBucketFillEvent event){
		if(event.getBlockClicked().getType()==Material.STATIONARY_LAVA){
			if(lavaToNerf.containsKey(event.getBlockClicked())){
				lavaToNerf.remove(event.getBlockClicked());
			}
		}
	}
	@EventHandler
	public void blockDestroyAlert(BlockBreakEvent event){// alerts moderators to various valuable blocks being placed
		Material type = event.getBlock().getType();
		if(type == Material.DIAMOND_BLOCK||
				type==Material.IRON_BLOCK||
				type==Material.GOLD_BLOCK||
				type==Material.LAPIS_BLOCK||
				type==Material.CHEST||
				type==Material.ENCHANTMENT_TABLE||
				type==Material.BREWING_STAND||
				type==Material.JUKEBOX){
			instance.getServer().broadcast(ChatColor.DARK_RED+event.getPlayer().getDisplayName()+ChatColor.DARK_RED+" has destroyed "+type.name().toLowerCase()+"!","pppopp.blockwarn");
		}
	}
	@EventHandler
	public void igniteMob(PlayerInteractEntityEvent event){//lights a mob on fire if you right click them with flint and steel
		if(event.getRightClicked() instanceof Player){
			return;
		}
		ItemStack inHand = event.getPlayer().getItemInHand();
		if(inHand.getType()==Material.FLINT_AND_STEEL){
			inHand.setDurability((short) (inHand.getDurability()+1));
			if(inHand.getDurability()>=65){
				inHand.setType(Material.AIR);
			}
			event.getRightClicked().setFireTicks(140);
		}
	}
	@EventHandler
	public void playerNoFallDamage(EntityDamageEvent event){//nerfs fall/fire/firetick damage
		if(event.getEntity() instanceof Player){
			if(event instanceof EntityDamageByEntityEvent){
				EntityDamageByEntityEvent entEvent = (EntityDamageByEntityEvent)event;
				if((entEvent.getDamager() instanceof Player) && (entEvent.getEntity() instanceof Player)){
					event.setCancelled(true);
				}
			}
			DamageCause cause = event.getCause();
			if(cause == DamageCause.FALL||cause == DamageCause.FIRE||cause == DamageCause.FIRE_TICK){
				event.setCancelled(true);
			}
			if(cause == DamageCause.LAVA){
				Player player = (Player)event.getEntity();
				if(lavaToNerf.containsKey(player.getLocation().getBlock())){
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void liquidFlowEvent(BlockFromToEvent event){
		if(event.getBlock().getType().equals(Material.STATIONARY_LAVA)){
			if(!lavaFlow){
				event.setCancelled(true);
				return;
			}
		} if(event.getBlock().getType().equals(Material.STATIONARY_WATER)){
			if(!waterFlow){
				event.setCancelled(true);
				return;
			}
		}
	}
	public void toggleWaterFlow(CommandSender player){
		waterFlow = !waterFlow;
		if(waterFlow){
			player.sendMessage(pinkieSays+"Let there be water!");
		} else player.sendMessage(pinkieSays+"The water party is over!");
	}
	public void toggleLavaFlow(CommandSender player){
		lavaFlow = !lavaFlow;
		if(lavaFlow){
			player.sendMessage(pinkieSays+"Let there be lava!");
		} else player.sendMessage(pinkieSays+"The lava party is over!");
	}
	@EventHandler
	public void onCombust(EntityCombustEvent event){
		if(event.getEntity() instanceof Player){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(event.getClickedBlock().getType()==Material.JUKEBOX){
				Block blockUp = event.getClickedBlock().getRelative(BlockFace.UP);
				if(blockUp.getType() == Material.TRAP_DOOR){
					TrapDoor trapdoor = new TrapDoor();
					trapdoor.setData(blockUp.getData());
					event.setCancelled(!trapdoor.isOpen());
					if(event.isCancelled()){
						pinkieSay("You need to open the lid before doing that, silly!",event.getPlayer());
					}
				}
			}
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		if(cmd.getName().equals("lavatoggle")){
			toggleLavaFlow(sender);
		}
		if(cmd.getName().equals("watertoggle")){
			toggleWaterFlow(sender);
		}
		return true;
	}
	@Override
	public void furtherInitialization() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new lavaNerfTimerDown(this), 0, 20);
	}
	@Override
	public PoniCommandExecutor getThis(){
		return this;
	}
}
