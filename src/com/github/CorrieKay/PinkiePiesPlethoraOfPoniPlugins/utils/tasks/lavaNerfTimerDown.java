package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.tasks;

import java.util.HashMap;
import java.util.TimerTask;

import org.bukkit.block.Block;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Handlers.ProtectionHandler;

public class lavaNerfTimerDown extends TimerTask {

	private final ProtectionHandler ph;
	public lavaNerfTimerDown(ProtectionHandler ph){
		this.ph = ph;
	}
	@Override
	public void run() {
		HashMap<Block,Integer> derp = new HashMap<Block,Integer>(ph.getNerfedLava());
		for(Block block : ph.getNerfedLava().keySet()){
			int i = derp.get(block);
			i--;
			if(i ==0){
				derp.remove(block);
			} else {
				derp.put(block, i);
			}
		}ph.setNervedLava(derp);
	}

}
