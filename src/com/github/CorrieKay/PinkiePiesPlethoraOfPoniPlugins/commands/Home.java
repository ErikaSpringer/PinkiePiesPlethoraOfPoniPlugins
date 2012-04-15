package com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.Mane;
import com.github.CorrieKay.PinkiePiesPlethoraOfPoniPlugins.utils.PoniCommandExecutor;

public class Home extends PoniCommandExecutor {

	public Home(Mane instance, String[] cmds) {
		super(instance,cmds);
	}
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
			String[] arg3) {
		return false;
	}
	

}
